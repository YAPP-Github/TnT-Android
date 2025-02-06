package co.kr.tnt.trainer.connect

import co.kr.tnt.domain.model.User
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState

internal class TrainerConnectContract {
    data class TrainerConnectUiState(
        val page: TrainerConnectPage = TrainerConnectPage.TrainerConnectComplete,
        val trainerState: User.Trainer = User.Trainer.EMPTY,
        val traineeState: User.Trainee = User.Trainee.EMPTY,
    ) : UiState

    sealed interface TrainerConnectUiEvent : UiEvent {
        data class OnFetchInitialData(
            val trainerId: String,
            val traineeId: String,
        ) : TrainerConnectUiEvent
        data object OnNextClick : TrainerConnectUiEvent
        data object OnBackClick : TrainerConnectUiEvent
    }

    sealed interface TrainerConnectSideEffect : UiSideEffect {
        data object NavigateToBack : TrainerConnectSideEffect
        data object NavigateToHome : TrainerConnectSideEffect
        data class ShowToast(val message: String) : TrainerConnectSideEffect
    }

    enum class TrainerConnectPage {
        TrainerConnectComplete,
        TraineeProfile,
        ;

        companion object {
            val firstPage = TrainerConnectComplete
            val lastPage = TraineeProfile

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
