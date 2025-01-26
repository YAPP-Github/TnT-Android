package co.kr.tnt.trainee.connect

import co.kr.tnt.domain.model.UserType
import co.kr.tnt.trainee.connect.model.InputState
import co.kr.tnt.trainee.connect.model.PTSessionFormData
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState
import java.time.LocalDate

internal class TraineeConnectContract {
    data class TraineeConnectUiState(
        val page: TraineeConnectPage = TraineeConnectPage.CodeEntry,
        val inviteCode: String = "",
        val isCodeValid: InputState? = null,
        val completedSession: Int = 0,
        val totalSession: Int = 0,
        val selectedStartDate: LocalDate = LocalDate.now(),
        val trainerState: UserType.Trainer = UserType.Trainer(),
        val traineeState: UserType.Trainee = UserType.Trainee(),
    ) : UiState

    sealed interface TraineeConnectUiEvent : UiEvent {
        data class UpdatePTSessionData(val data: PTSessionFormData) : TraineeConnectUiEvent
        data class UpdateTrainerProfile(val profile: UserType.Trainer) : TraineeConnectUiEvent
        data class UpdateTraineeProfile(val profile: UserType.Trainee) : TraineeConnectUiEvent
        data class OnCodeValidateClick(val code: String) : TraineeConnectUiEvent
        data class OnCodeChanged(val code: String) : TraineeConnectUiEvent
        data object OnNextClick : TraineeConnectUiEvent
        data object OnBackClick : TraineeConnectUiEvent
        data object OnSkipClick : TraineeConnectUiEvent
    }

    sealed interface TraineeConnectSideEffect : UiSideEffect {
        data object NavigateToBack : TraineeConnectSideEffect
        data object NavigateToHome : TraineeConnectSideEffect
    }

    enum class TraineeConnectPage {
        CodeEntry,
        PTSessionForm,
        TraineeConnectComplete,
        ;

        companion object {
            fun getPreviousPage(currentPage: TraineeConnectPage): TraineeConnectPage {
                return when (currentPage) {
                    PTSessionForm -> CodeEntry
                    TraineeConnectComplete -> PTSessionForm
                    else -> error("No previous page defined for $currentPage")
                }
            }

            fun getNextPage(currentPage: TraineeConnectPage): TraineeConnectPage {
                return when (currentPage) {
                    CodeEntry -> PTSessionForm
                    PTSessionForm -> TraineeConnectComplete
                    else -> error("No next page defined for $currentPage")
                }
            }
        }
    }
}
