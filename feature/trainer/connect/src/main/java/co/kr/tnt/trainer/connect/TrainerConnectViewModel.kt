package co.kr.tnt.trainer.connect

import co.kr.tnt.domain.model.User
import androidx.lifecycle.viewModelScope
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
        // TODO 화면에 보여줄 프로필 정보 불러오기
        updateState {
            copy(
                trainerState = User.Trainer(
                    id = "trainer",
                    name = "김헬짱",
                    image = null,
                ),
                traineeState = User.Trainee(
                    id = "trainee",
                    name = "김회원",
                    image = "https://buly.kr/3j7VVqN",
                    birthday = null,
                    age = 25,
                    weight = 100.0,
                    height = 165,
                    ptPurpose = listOf("체중 감량", "자세 교정"),
                    caution = "발목이 안좋아서 발목에 무리가는 행동을 하면 안돼요. 잘 부탁드려요!",
                ),
            )
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
