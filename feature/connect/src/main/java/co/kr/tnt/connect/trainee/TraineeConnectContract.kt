import co.kr.tnt.connect.model.TraineeProfile
import co.kr.tnt.connect.model.TrainerProfile
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState

internal class TraineeConnectContract {
    data class TraineeConnectUiState(
        val page: TraineeConnectPage = TraineeConnectPage.CodeEntry,
        val inviteCode: String? = "",
        val isCodeValid: Boolean? = false,
        val trainerState: TrainerProfile? = TrainerProfile(),
        val traineeState: TraineeProfile? = TraineeProfile(),
    ) : UiState

    sealed interface TraineeConnectUiEvent : UiEvent {
        data class UpdateTrainerProfile(val profile: TrainerProfile) : TraineeConnectUiEvent
        data class UpdateTraineeProfile(val profile: TraineeProfile) : TraineeConnectUiEvent
        data class OnValidateClick(val code: String) : TraineeConnectUiEvent
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
                    else -> throw IllegalStateException("No previous page defined for $currentPage")
                }
            }

            fun getNextPage(currentPage: TraineeConnectPage): TraineeConnectPage {
                return when (currentPage) {
                    CodeEntry -> PTSessionForm
                    PTSessionForm -> TraineeConnectComplete
                    else -> throw IllegalStateException("No previous page defined for $currentPage")
                }
            }
        }
    }
}
