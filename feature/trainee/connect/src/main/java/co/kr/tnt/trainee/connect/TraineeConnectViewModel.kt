package co.kr.tnt.trainee.connect

import co.kr.tnt.domain.model.User
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectPage
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectSideEffect
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectUiEvent
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectUiState
import co.kr.tnt.trainee.connect.model.InputState.INVALID
import co.kr.tnt.trainee.connect.model.InputState.VALID
import co.kr.tnt.trainee.connect.model.PTSessionFormData
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class TraineeConnectViewModel @Inject constructor() :
    BaseViewModel<TraineeConnectUiState, TraineeConnectUiEvent, TraineeConnectSideEffect>(
        TraineeConnectUiState(),
    ) {
        init {
            initProfile()
        }

        override suspend fun handleEvent(event: TraineeConnectUiEvent) {
            when (event) {
                is TraineeConnectUiEvent.UpdateTrainerProfile -> updateTrainerProfile(event.profile)
                is TraineeConnectUiEvent.UpdateTraineeProfile -> updateTraineeProfile(event.profile)
                is TraineeConnectUiEvent.UpdatePTSessionData -> updatePTSessionForm(event.data)
                is TraineeConnectUiEvent.OnCodeValidateClick -> validateCode(event.code)
                is TraineeConnectUiEvent.OnCodeChanged -> resetCode(event.code)
                TraineeConnectUiEvent.OnBackClick -> navigateToBack()
                TraineeConnectUiEvent.OnNextClick -> navigateToNext()
                TraineeConnectUiEvent.OnSkipClick -> navigateToHome()
            }
        }

        private fun initProfile() {
            // TODO 연결 완료 화면에 보여줄 프로필 정보 불러오기
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

        private fun validateCode(code: String) {
            // TODO 코드 유효성 확인
            val isValid = if (code.length == 8) {
                VALID
            } else {
                INVALID
            }
            updateState { copy(inviteCode = code, isCodeValid = isValid) }
        }

        private fun resetCode(code: String) {
            updateState { copy(inviteCode = code, isCodeValid = null) }
        }

        private fun updatePTSessionForm(data: PTSessionFormData) {
            updateState {
                copy(
                    completedSession = data.completedSession,
                    totalSession = data.totalSession,
                    selectedStartDate = data.selectedStartDate,
                )
            }
            navigateToNext()
        }

        private fun updateTrainerProfile(profile: User.Trainer) {
            updateState { copy(trainerState = profile) }
        }

        private fun updateTraineeProfile(profile: User.Trainee) {
            updateState { copy(traineeState = profile) }
        }

        private fun navigateToNext() {
            val nextPage = when (currentState.page) {
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
