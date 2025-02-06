package co.kr.tnt.trainee.connect

import androidx.lifecycle.viewModelScope
import co.kr.tnt.domain.model.User
import co.kr.tnt.domain.repository.ConnectRepository
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectPage
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectSideEffect
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectUiEvent
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectUiState
import co.kr.tnt.trainee.connect.model.FormData
import co.kr.tnt.trainee.connect.model.InputState.INVALID
import co.kr.tnt.trainee.connect.model.InputState.VALID
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TraineeConnectViewModel @Inject constructor(
    private val connectRepository: ConnectRepository,
) :
    BaseViewModel<TraineeConnectUiState, TraineeConnectUiEvent, TraineeConnectSideEffect>(
            TraineeConnectUiState(),
        ) {
        override suspend fun handleEvent(event: TraineeConnectUiEvent) {
            when (event) {
                is TraineeConnectUiEvent.OnCodeValidateClick -> validateCode(event.code)
                is TraineeConnectUiEvent.OnCodeChanged -> resetCode(event.code)
                is TraineeConnectUiEvent.OnNextClick -> navigateToNext(event.data)
                TraineeConnectUiEvent.OnBackClick -> navigateToBack()
                TraineeConnectUiEvent.OnSkipClick -> navigateToHome()
            }
        }

        private fun validateCode(code: String) {
            viewModelScope.launch {
                runCatching {
                    connectRepository.verifyInviteCode(code)
                }.onSuccess { result ->
                    if (result) {
                        updateState { copy(inviteCode = code, isCodeValid = VALID) }
                    } else {
                        updateState { copy(inviteCode = code, isCodeValid = INVALID) }
                    }
                }.onFailure {
                    sendEffect(TraineeConnectSideEffect.ShowToast("서버 요청에 실패했어요"))
                }
            }
        }

        private fun resetCode(code: String) {
            updateState { copy(inviteCode = code, isCodeValid = null) }
        }

        private fun connectRequest(data: FormData) {
            updateState { copy(formData = data) }
            viewModelScope.launch {
                runCatching {
                    connectRepository.connectRequest(
                        invitationCode = currentState.inviteCode,
                        startDate = currentState.selectedStartDate.toString(),
                        totalPtCount = currentState.totalSession,
                        finishedPtCount = currentState.completedSession,
                    )
                }.onSuccess { result ->
                    val nextPage = TraineeConnectPage.getNextPage(currentState.page)
                    updateState {
                        copy(
                            formData = FormData.ProfileData(
                                trainer = User.Trainer(
                                    name = result.trainerName,
                                    image = result.trainerImage,
                                    id = "",
                                ),
                                trainee = User.Trainee(
                                    name = result.traineeName,
                                    image = result.traineeImage,
                                    id = "",
                                    birthday = null,
                                    age = 0,
                                    weight = 0.0,
                                    height = 0,
                                    ptPurpose = emptyList(),
                                    caution = null,
                                ),
                            ),
                            page = nextPage,
                        )
                    }
                }.onFailure {
                    sendEffect(TraineeConnectSideEffect.ShowToast("서버 요청에 실패했어요"))
                }
            }
        }

        private fun navigateToNext(data: FormData?) {
            val nextPage = when (currentState.page) {
                TraineeConnectPage.PTSessionForm -> {
                    data?.let { connectRequest(it) }
                    return
                }

                TraineeConnectPage.TraineeConnectComplete -> {
                    sendEffect(TraineeConnectSideEffect.NavigateToHome)
                    return
                }

                else -> TraineeConnectPage.getNextPage(currentState.page)
            }
            updateState { copy(page = nextPage) }
        }

        private fun navigateToBack() {
            val previousPage = when (currentState.page) {
                TraineeConnectPage.CodeEntry -> {
                    sendEffect(TraineeConnectSideEffect.NavigateToBack)
                    return
                }

                else -> TraineeConnectPage.getPreviousPage(currentState.page)
            }
            updateState { copy(page = previousPage) }
        }

        private fun navigateToHome() {
            sendEffect(TraineeConnectSideEffect.NavigateToHome)
        }
    }
