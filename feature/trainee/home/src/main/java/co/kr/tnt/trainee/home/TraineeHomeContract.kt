package co.kr.tnt.trainee.home

import co.kr.tnt.domain.model.RecordList
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState
import java.time.LocalDate
import java.time.YearMonth

class TraineeHomeContract {
    data class TraineeHomeUiState(
        val visibleYearMonth: YearMonth = YearMonth.now(),
        val selectedDate: LocalDate = LocalDate.now(),
        val markedDates: List<LocalDate> = emptyList(),
        val recordList: List<RecordList> = emptyList(),
    ) : UiState

    sealed interface TraineeHomeUiEvent : UiEvent {
        data object OnNextWeekClick : TraineeHomeUiEvent
        data object OnPreviousWeekClick : TraineeHomeUiEvent
        data class OnDayClick(val date: LocalDate) : TraineeHomeUiEvent
    }

    sealed interface TraineeHomeEffect : UiSideEffect {
        data class ShowToast(val message: String) : TraineeHomeEffect
    }
}
