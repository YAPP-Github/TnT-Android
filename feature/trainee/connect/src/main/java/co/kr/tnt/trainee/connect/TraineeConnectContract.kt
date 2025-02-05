package co.kr.tnt.trainee.connect

import co.kr.tnt.domain.model.User
import co.kr.tnt.trainee.connect.model.FormData
import co.kr.tnt.trainee.connect.model.InputState
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState

internal class TraineeConnectContract {
    data class TraineeConnectUiState(
        val page: TraineeConnectPage = TraineeConnectPage.CodeEntry,
        val inviteCode: String = "",
        val isCodeValid: InputState? = null,
        val formData: FormData? = null,
    ) : UiState {
        val trainer: User.Trainer
            get() = (formData as? FormData.ProfileData)?.trainer ?: User.Trainer.EMPTY

        val trainee: User.Trainee
            get() = (formData as? FormData.ProfileData)?.trainee ?: User.Trainee.EMPTY
    }

    sealed interface TraineeConnectUiEvent : UiEvent {
        data class OnCodeValidateClick(val code: String) : TraineeConnectUiEvent
        data class OnCodeChanged(val code: String) : TraineeConnectUiEvent
        data class OnNextClick(val data: FormData?) : TraineeConnectUiEvent
        data object OnBackClick : TraineeConnectUiEvent
        data object OnSkipClick : TraineeConnectUiEvent
    }

    sealed interface TraineeConnectSideEffect : UiSideEffect {
        data object NavigateToBack : TraineeConnectSideEffect
        data object NavigateToHome : TraineeConnectSideEffect
        data class ShowToast(val message: String) : TraineeConnectSideEffect
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
