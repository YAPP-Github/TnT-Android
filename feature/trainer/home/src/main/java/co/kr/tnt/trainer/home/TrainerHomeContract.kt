package co.kr.tnt.trainer.home

import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState
import java.time.LocalDate
import java.time.YearMonth

internal class TrainerHomeContract {
    data class TrainerHomeUiState(
        val selectedDay: LocalDate = LocalDate.now(),
        val dailyPtSessionCount: Map<LocalDate, Int> = mapOf(),
    ) : UiState

    sealed interface TrainerHomeUiEvent : UiEvent {
        data object OnClickNotification : TrainerHomeUiEvent
        data class OnChangeVisibleMonth(val yearMonth: YearMonth) : TrainerHomeUiEvent
        data class OnClickDay(val day: LocalDate) : TrainerHomeUiEvent
    }

    sealed interface TrainerHomeSideEffect : UiSideEffect {
        data object NavigateToNotification : TrainerHomeSideEffect
        data class ShowToast(val message: String) : TrainerHomeSideEffect
    }
}
