import co.kr.tnt.connect.model.TraineeProfile
import co.kr.tnt.connect.model.TrainerProfile
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState

internal class TrainerConnectContract {
    data class TrainerConnectUiState(
        val page: TrainerConnectPage = TrainerConnectPage.TrainerConnectComplete,
        val inviteCode: String? = "",
        val trainerState: TrainerProfile = TrainerProfile(),
        val traineeState: TraineeProfile = TraineeProfile(),
    ) : UiState

    sealed interface TrainerConnectUiEvent : UiEvent {
        data object OnRegenerateClick : TrainerConnectUiEvent
        data object OnNextClick : TrainerConnectUiEvent
        data object OnBackClick : TrainerConnectUiEvent
        data object OnSkipClick : TrainerConnectUiEvent
    }

    sealed interface TrainerConnectSideEffect : UiSideEffect {
        data object NavigateToBack : TrainerConnectSideEffect
        data object NavigateToHome : TrainerConnectSideEffect
    }

    enum class TrainerConnectPage {
        CodeGeneration,

        TrainerConnectComplete,
        TraineeProfile,
        ;

        companion object {
            fun getPreviousPage(currentPage: TrainerConnectPage): TrainerConnectPage {
                return when (currentPage) {
                    TraineeProfile -> TrainerConnectComplete
                    else -> error("No previous page defined for $currentPage")
                }
            }

            fun getNextPage(currentPage: TrainerConnectPage): TrainerConnectPage {
                return when (currentPage) {
                    TrainerConnectComplete -> TraineeProfile
                    else -> error("No next page defined for $currentPage")
                }
            }
        }
    }
}
