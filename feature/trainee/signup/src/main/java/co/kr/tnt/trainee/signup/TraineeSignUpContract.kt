package co.kr.tnt.trainee.signup

import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState

internal class TraineeSignUpContract {
    data class TraineeSignUpUiState(
        val page: TraineeSignUpPage = TraineeSignUpPage.ProfileSetUp,
        val name: String = "",
    ) : UiState

    sealed interface TraineeSignUpUiEvent : UiEvent {
        data object OnNextClick : TraineeSignUpUiEvent
        data object OnBackClick : TraineeSignUpUiEvent
    }

    sealed interface TraineeSignUpEffect : UiSideEffect {
        data object NavigateToBack : TraineeSignUpEffect
        data object NavigateToConnect : TraineeSignUpEffect
    }

    enum class TraineeSignUpPage {
        ProfileSetUp,
        BasicInfo,
        PTPurpose,
        NoteForTrainer,
        SignUpComplete,
        ;

        companion object {
            fun getPreviousPage(currentPage: TraineeSignUpPage): TraineeSignUpPage {
                return when (currentPage) {
                    BasicInfo -> ProfileSetUp
                    PTPurpose -> BasicInfo
                    NoteForTrainer -> PTPurpose
                    SignUpComplete -> NoteForTrainer
                    else -> error("No previous page defined for $currentPage")
                }
            }

            fun getNextPage(currentPage: TraineeSignUpPage): TraineeSignUpPage {
                return when (currentPage) {
                    ProfileSetUp -> BasicInfo
                    BasicInfo -> PTPurpose
                    PTPurpose -> NoteForTrainer
                    NoteForTrainer -> SignUpComplete
                    else -> error("No next page defined for $currentPage")
                }
            }
        }
    }
}
