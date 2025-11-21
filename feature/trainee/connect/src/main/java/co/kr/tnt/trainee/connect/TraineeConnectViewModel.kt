package co.kr.tnt.trainee.connect

import androidx.lifecycle.viewModelScope
import co.kr.tnt.core.ui.R.string.core_failed_to_server_request
import co.kr.tnt.domain.repository.ConnectRepository
import co.kr.tnt.domain.repository.TraineeRepository
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectPage
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectSideEffect
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectUiEvent
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectUiState
import co.kr.tnt.trainee.connect.model.InputState.FOCUS
import co.kr.tnt.trainee.connect.model.InputState.INVALID
import co.kr.tnt.trainee.connect.model.InputState.VALID
import co.kr.tnt.ui.base.BaseViewModel
import co.kr.tnt.ui.resource.DisplayText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class TraineeConnectViewModel @Inject constructor(
    private val connectRepository: ConnectRepository,
    private val traineeRepository: TraineeRepository,
) :
    BaseViewModel<TraineeConnectUiState, TraineeConnectUiEvent, TraineeConnectSideEffect>(
            TraineeConnectUiState(),
        ) {
        override suspend fun handleEvent(event: TraineeConnectUiEvent) {
            when (event) {
                is TraineeConnectUiEvent.OnClickValidateCode -> validateInviteCode(event.code)
                is TraineeConnectUiEvent.OnChangeInviteCode -> handleInviteCodeChange(event.code)
                is TraineeConnectUiEvent.OnChangeSessionStartDate -> updateState { copy(sessionStartDate = event.date) }
                TraineeConnectUiEvent.OnChangeDialogState -> handleDialogState()
                is TraineeConnectUiEvent.OnChangeCompletedSessionCount -> updateState {
                    copy(completedSessionCount = event.count)
                }

                is TraineeConnectUiEvent.OnChangeTotalSessionCount -> updateState {
                    copy(totalSessionCount = event.count)
                }

                is TraineeConnectUiEvent.OnClickNext -> handleNextClick()
                TraineeConnectUiEvent.OnClickBack -> navigateToBack()
                TraineeConnectUiEvent.OnClickSkip -> navigateToHome()
            }
        }

        private fun validateInviteCode(code: String) {
            viewModelScope.launch {
                runCatching {
                    connectRepository.getVerifyInviteCode(code)
                }.onSuccess { result ->
                    if (result) {
                        updateState { copy(inviteCodeInputState = VALID) }
                    } else {
                        updateState { copy(inviteCodeInputState = INVALID) }
                    }
                }.onFailure {
                    sendEffect(
                        TraineeConnectSideEffect.ShowToast(
                            DisplayText.Resource(core_failed_to_server_request),
                        ),
                    )
                }
            }
        }

        private fun handleInviteCodeChange(inviteCode: String) {
            updateState {
                copy(
                    inviteCode = inviteCode,
                    inviteCodeInputState = FOCUS,
                )
            }
        }

        private fun handleNextClick() = when (currentState.page) {
            TraineeConnectPage.CodeEntry -> navigateToNext()
            TraineeConnectPage.PTSessionForm -> requestConnect()
            TraineeConnectPage.TraineeConnectComplete -> navigateToNext()
        }

        private fun requestConnect() {
            viewModelScope.launch {
                updateState { copy(isLoading = true) }
                runCatching {
                    currentState.run {
                        connectRepository.connectRequest(
                            invitationCode = inviteCode.trim(),
                            startDate = (sessionStartDate ?: LocalDate.now()).toString(),
                            completedSession = completedSessionCount.toInt(),
                            totalSession = totalSessionCount.toInt(),
                        )
                    }
                }.onSuccess { result ->
                    updateState {
                        copy(
                            trainerName = result.trainerName,
                            trainerImage = result.trainerImage,
                            traineeName = result.traineeName,
                            traineeImage = result.traineeImage,
                        )
                    }
                    refreshCachedUserInfo()
                    navigateToNext()
                }.onFailure {
                    sendEffect(
                        TraineeConnectSideEffect.ShowToast(
                            DisplayText.Resource(core_failed_to_server_request),
                        ),
                    )
                }.also {
                    updateState { copy(isLoading = false) }
                }
            }
        }

        private fun refreshCachedUserInfo() {
            viewModelScope.launch {
                traineeRepository.refreshCachedUserInfo()
            }
        }

        private fun navigateToBack() {
            if (currentState.page == TraineeConnectPage.firstPage) {
                handleDialogState()
                sendEffect(TraineeConnectSideEffect.NavigateToBack)
                return
            }

            if (currentState.page == TraineeConnectPage.lastPage) {
                navigateToHome()
                return
            }

            updateState { copy(page = TraineeConnectPage.getPreviousPage(currentState.page)) }
        }

        private fun handleDialogState() {
            if (currentState.inviteCodeInputState == VALID) {
                updateState { copy(showDialog = !showDialog) }
            } else {
                updateState { copy(showDialog = false) }
                sendEffect(TraineeConnectSideEffect.NavigateToBack)
                return
            }
        }

        private fun navigateToNext() {
            if (currentState.page == TraineeConnectPage.lastPage) {
                sendEffect(TraineeConnectSideEffect.NavigateToHome)
                return
            }

            updateState { copy(page = TraineeConnectPage.getNextPage(currentState.page)) }
        }

        private fun navigateToHome() {
            sendEffect(TraineeConnectSideEffect.NavigateToHome)
        }
    }
