package co.kr.tnt.trainee.connect

import co.kr.tnt.trainee.connect.model.InputState
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState
import java.time.LocalDate

internal class TraineeConnectContract {
    data class TraineeConnectUiState(
        val page: TraineeConnectPage = TraineeConnectPage.CodeEntry,
        val inviteCodeInputState: InputState = InputState.UNFOCUSED,
        val inviteCode: String = "",
        val showDialog: Boolean = false,
        val sessionStartDate: LocalDate? = null,
        val completedSessionCount: String = "",
        val totalSessionCount: String = "",
        val trainerName: String = "",
        val trainerImage: String = "",
        val traineeName: String = "",
        val traineeImage: String = "",
        val isLoading: Boolean = false,
    ) : UiState

    sealed interface TraineeConnectUiEvent : UiEvent {
        data class OnCodeValidateClick(val code: String) : TraineeConnectUiEvent
        data class OnChangeInviteCode(val code: String) : TraineeConnectUiEvent
        data object OnChangeDialogState : TraineeConnectUiEvent
        data class OnChangeSessionStartDate(val date: LocalDate) : TraineeConnectUiEvent
        data class OnChangeCompletedSessionCount(val count: String) : TraineeConnectUiEvent
        data class OnChangeTotalSessionCount(val count: String) : TraineeConnectUiEvent
        data object OnNextClick : TraineeConnectUiEvent
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
            val firstPage = CodeEntry
            val lastPage = TraineeConnectComplete

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
