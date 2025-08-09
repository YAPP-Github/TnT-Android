package co.kr.tnt.trainee.home

import co.kr.tnt.domain.model.DailyRecord
import co.kr.tnt.domain.model.trainee.TraineeDailyRecordStatus
import co.kr.tnt.domain.model.trainee.TraineePtSession
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState
import co.kr.tnt.ui.resource.DisplayText
import java.time.LocalDate
import java.time.YearMonth

internal class TraineeHomeContract {
    data class TraineeHomeUiState(
        val selectedDay: LocalDate = LocalDate.now(),
        val recordedDates: TraineeDailyRecordStatus? = null,
        val ptSessions: TraineePtSession? = null,
        val recordList: List<DailyRecord> = emptyList(),
        val isConnected: Boolean = false,
        val isDialogHiddenForThreeDays: Boolean = false,
        val showConnectDialog: Boolean = false,
    ) : UiState

    sealed interface TraineeHomeUiEvent : UiEvent {
        data object OnScreen : TraineeHomeUiEvent
        data class OnChangeVisibleMonth(val yearMonth: YearMonth) : TraineeHomeUiEvent
        data class OnClickPtSessionCard(val ptSessionId: Long) : TraineeHomeUiEvent
        data object OnClickExerciseRecord : TraineeHomeUiEvent
        data object OnClickMealRecord : TraineeHomeUiEvent
        data class OnClickDay(val date: LocalDate) : TraineeHomeUiEvent
        data object OnChangeHideDialogOption : TraineeHomeUiEvent
        data object OnConfirmConnectDialog : TraineeHomeUiEvent
        data object OnDismissDialog : TraineeHomeUiEvent
    }

    sealed interface TraineeHomeEffect : UiSideEffect {
        data object NavigateToConnect : TraineeHomeEffect
        data object NavigateToExerciseRecord : TraineeHomeEffect
        data object NavigateToMealRecord : TraineeHomeEffect
        data class ShowToast(val message: DisplayText) : TraineeHomeEffect
    }
}
