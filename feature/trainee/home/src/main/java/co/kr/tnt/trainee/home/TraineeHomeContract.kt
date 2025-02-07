package co.kr.tnt.trainee.home

import co.kr.tnt.domain.model.TraineeHomeData
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState
import java.time.LocalDate

class TraineeHomeContract {
    data class TraineeHomeUiState(
        val selectedDate: LocalDate = LocalDate.now(),
        val markedDates: List<LocalDate> = emptyList(),
        val ptLessons: List<TraineeHomeData.PTLesson> = emptyList(),
        val recordList: List<TraineeHomeData.Record> = emptyList(),
    ) : UiState

    sealed interface TraineeHomeUiEvent : UiEvent {
        data object OnClickNextWeek : TraineeHomeUiEvent
        data object OnClickPreviousWeek : TraineeHomeUiEvent
        data class OnClickPtSessionCard(val ptSessionId: String) : TraineeHomeUiEvent
        data class OnClickDay(val date: LocalDate) : TraineeHomeUiEvent
    }

    sealed interface TraineeHomeEffect : UiSideEffect {
        data class ShowToast(val message: String) : TraineeHomeEffect
    }
}
