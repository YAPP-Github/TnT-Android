package co.kr.tnt.trainer.connect

import androidx.lifecycle.viewModelScope
import co.kr.tnt.domain.model.User
import co.kr.tnt.domain.repository.ConnectRepository
import co.kr.tnt.trainer.connect.TrainerConnectContract.TrainerConnectPage
import co.kr.tnt.trainer.connect.TrainerConnectContract.TrainerConnectSideEffect
import co.kr.tnt.trainer.connect.TrainerConnectContract.TrainerConnectUiEvent
import co.kr.tnt.trainer.connect.TrainerConnectContract.TrainerConnectUiState
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TrainerConnectViewModel @Inject constructor(
    private val connectRepository: ConnectRepository,
) : BaseViewModel<TrainerConnectUiState, TrainerConnectUiEvent, TrainerConnectSideEffect>(
        TrainerConnectUiState(),
    ) {
    override suspend fun handleEvent(event: TrainerConnectUiEvent) {
        when (event) {
            is TrainerConnectUiEvent.OnRegenerateClick -> regenerateCode()
            TrainerConnectUiEvent.OnNextClick -> navigateToNext()
            TrainerConnectUiEvent.OnBackClick -> navigateToBack()
            TrainerConnectUiEvent.OnSkipClick -> navigateToHome()
        }
    }

    fun setStartPage(startPage: TrainerConnectPage) {
        updateState { copy(page = startPage) }

        if (startPage == TrainerConnectPage.CodeGeneration) {
            generateCode()
        } else {
            initProfile()
        }
    }

    private fun generateCode() {
        viewModelScope.launch {
            runCatching {
                connectRepository.getInviteCode()
            }.onSuccess { result ->
                updateState { copy(inviteCode = result.invitationCode) }
            }.onFailure {
                // TODO 컴포넌트 사용
                sendEffect(TrainerConnectSideEffect.ShowToast("서버 요청에 실패했어요"))
            }
        }
    }

    private fun initProfile() {
        viewModelScope.launch {
            runCatching {
                connectRepository.getConnectedTraineeInfo()
            }.onSuccess { result ->
                updateState {
                    copy(
                        trainerState = User.Trainer(
                            id = "",
                            name = result.trainerName,
                            image = result.trainerImage,
                        ),
                        traineeState = User.Trainee(
                            id = "",
                            name = result.traineeName,
                            image = result.traineeImage,
                            birthday = null,
                            age = result.age,
                            weight = result.weight,
                            height = result.height.toInt(),
                            ptPurpose = listOf(result.ptGoal),
                            caution = result.cautionNote,
                        ),
                    )
                }
            }.onFailure {
                // TODO 컴포넌트 사용
                sendEffect(TrainerConnectSideEffect.ShowToast("서버 요청에 실패했어요"))
            }
        }
    }

    private fun regenerateCode() {
        viewModelScope.launch {
            runCatching {
                connectRepository.regenerateInviteCode()
            }.onSuccess { result ->
                updateState { copy(inviteCode = result.invitationCode) }
            }.onFailure {
                // TODO 컴포넌트 사용
                sendEffect(TrainerConnectSideEffect.ShowToast("서버 요청에 실패했어요"))
            }
        }
    }

    private fun navigateToNext() {
        val nextPage = when (currentState.page) {
            TrainerConnectPage.TraineeProfile -> {
                sendEffect(TrainerConnectSideEffect.NavigateToHome)
                return
            }

            else -> TrainerConnectPage.getNextPage(currentState.page)
        }
        updateState { copy(page = nextPage) }
    }

    private fun navigateToBack() {
        val previousPage = when (currentState.page) {
            TrainerConnectPage.CodeGeneration,
            TrainerConnectPage.TrainerConnectComplete,
            -> {
                sendEffect(TrainerConnectSideEffect.NavigateToBack)
                return
            }

            else -> TrainerConnectPage.getPreviousPage(currentState.page)
        }
        updateState { copy(page = previousPage) }
    }

    private fun navigateToHome() {
        sendEffect(TrainerConnectSideEffect.NavigateToHome)
    }
}
