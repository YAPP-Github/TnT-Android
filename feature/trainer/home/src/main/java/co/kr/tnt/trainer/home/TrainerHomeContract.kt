package co.kr.tnt.trainer.home

import co.kr.tnt.domain.model.PtSession
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState
import co.kr.tnt.ui.model.SnackbarType
import co.kr.tnt.ui.resource.DisplayText
import java.time.LocalDate
import java.time.YearMonth

internal class TrainerHomeContract {
    data class TrainerHomeUiState(
        val selectedDay: LocalDate = LocalDate.now(),
        val dailyPtSessionCount: Map<LocalDate, Int> = mapOf(),
        val selectedDayPtSessions: List<PtSession>? = null,
        val dialogState: DialogState = DialogState.NONE,
        val isDialogHiddenForThreeDays: Boolean = false,
    ) : UiState {
        enum class DialogState {
            NONE,
            HOME_CONNECT,
            ADD_PT_CONNECT,
        }
    }

    sealed interface TrainerHomeUiEvent : UiEvent {
        data object OnScreen : TrainerHomeUiEvent
        data object OnClickNotification : TrainerHomeUiEvent
        data class OnChangeVisibleMonth(val yearMonth: YearMonth) : TrainerHomeUiEvent
        data class OnClickDay(val day: LocalDate) : TrainerHomeUiEvent
        data object OnClickAddPtSession : TrainerHomeUiEvent
        data class OnClickPtSessionComplete(val ptSession: PtSession) : TrainerHomeUiEvent
        data object OnConfirmConnectDialog : TrainerHomeUiEvent
        data object OnChangeHideDialogOption : TrainerHomeUiEvent
        data object OnDismissDialog : TrainerHomeUiEvent
    }

    sealed interface TrainerHomeSideEffect : UiSideEffect {
        data object NavigateToNotification : TrainerHomeSideEffect
        data object NavigateToAddPtSession : TrainerHomeSideEffect
        data object NavigateToInvite : TrainerHomeSideEffect
        data class ShowToast(
            val message: DisplayText,
            val type: SnackbarType = SnackbarType.WARNING,
        ) : TrainerHomeSideEffect
    }
}
