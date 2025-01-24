package co.kr.tnt.connect.trainer

import TrainerConnectContract.TrainerConnectPage
import TrainerConnectContract.TrainerConnectSideEffect
import TrainerConnectContract.TrainerConnectUiEvent
import TrainerConnectContract.TrainerConnectUiState
import co.kr.tnt.connect.model.TraineeProfile
import co.kr.tnt.connect.model.TrainerProfile
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class TrainerConnectViewModel @Inject constructor() :
    BaseViewModel<TrainerConnectUiState, TrainerConnectUiEvent, TrainerConnectSideEffect>(
        TrainerConnectUiState(),
    ) {
        init {
            initProfile()
        }

        override suspend fun handleEvent(event: TrainerConnectUiEvent) {
            when (event) {
                is TrainerConnectUiEvent.OnRegenerateClick -> regenerateCode()
                TrainerConnectUiEvent.OnNextClick -> navigateToNext()
                TrainerConnectUiEvent.OnBackClick -> navigateToBack()
                TrainerConnectUiEvent.OnSkipClick -> navigateToHome()
            }
        }

        private fun initProfile() {
            // TODO 화면에 보여줄 프로필 정보 불러오기
            updateState {
                copy(
                    trainerState = TrainerProfile(
                        name = "김헬짱",
                        image = null,
                    ),
                    traineeState = TraineeProfile(
                        name = "김회원",
                        image = "https://buly.kr/3j7VVqN",
                        age = 25,
                        weight = 165F,
                        height = 52F,
                        ptPurpose = "체중 감량, 자세 교정",
                        caution = "발목이 안좋아서 발목에 무리가는 행동을 하면 안돼요. 잘 부탁드려요!",
                    ),
                )
            }
        }

        fun generateCode() {
            // TODO 코드 발급
            val code = "12345678"
            updateState { copy(inviteCode = code) }
        }

        private fun regenerateCode() {
            // TODO 코드 재발급
            val newCode = "87654321"
            updateState { copy(inviteCode = newCode) }
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
                TrainerConnectPage.TrainerConnectComplete -> {
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
