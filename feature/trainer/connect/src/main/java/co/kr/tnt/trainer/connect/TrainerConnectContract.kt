package co.kr.tnt.trainer.connect

import co.kr.tnt.domain.model.UserType
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState

internal class TrainerConnectContract {
    data class TrainerConnectUiState(
        val page: TrainerConnectPage = TrainerConnectPage.CodeGeneration,
        val inviteCode: String = "",
        val trainerState: UserType.Trainer = UserType.Trainer(
            id = "",
            name = "",
            image = null,
        ),
        val traineeState: UserType.Trainee = UserType.Trainee(
            id = "",
            name = "",
            image = null,
            birthday = null,
            age = 0,
            weight = 0.0,
            height = 0,
            ptPurpose = emptyList(),
            caution = null,
        ),
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
