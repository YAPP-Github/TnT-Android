package co.kr.tnt.connect.trainee

import TraineeConnectContract.TraineeConnectPage
import TraineeConnectContract.TraineeConnectSideEffect
import TraineeConnectContract.TraineeConnectUiEvent
import TraineeConnectContract.TraineeConnectUiState
import co.kr.tnt.connect.model.PTSessionFormData
import co.kr.tnt.connect.model.TraineeProfile
import co.kr.tnt.connect.model.TrainerProfile
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
                TraineeConnectUiEvent.OnCodeChanged -> clearValidation()
                TraineeConnectUiEvent.OnBackClick -> navigateToBack()
                TraineeConnectUiEvent.OnNextClick -> navigateToNext()
                TraineeConnectUiEvent.OnSkipClick -> navigateToHome()
            }
        }

        private fun initProfile() {
            // TODO 연결 완료 화면에 보여줄 프로필 정보 불러오기
            updateState {
                copy(
                    trainerState = TrainerProfile(
                        name = "김헬짱",
                        image = null,
                    ),
                    traineeState = TraineeProfile(
                        name = "김회원",
                        image = "https://buly.kr/3j7VVqN",
                    ),
                )
            }
        }

        private fun validateCode(code: String) {
            // TODO 코드 유효성 확인
            val isValid = code.length == 8
            updateState { copy(inviteCode = code, isCodeValid = isValid) }
        }

        private fun clearValidation() {
            updateState { copy(inviteCode = "", isCodeValid = null) }
        }

        private fun updatePTSessionForm(data: PTSessionFormData) {
            updateState {
                copy(
                    completedSession = data.completedSession,
                    totalSession = data.totalSession,
                    startDate = data.startDate,
                )
            }
            navigateToNext()
        }

        private fun updateTrainerProfile(profile: TrainerProfile) {
            updateState { copy(trainerState = profile) }
        }

        private fun updateTraineeProfile(profile: TraineeProfile) {
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
