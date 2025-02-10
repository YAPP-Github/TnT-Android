package co.kr.tnt.trainee.home

import co.kr.tnt.domain.model.DailyRecord
import co.kr.tnt.domain.model.trainee.TraineeDailyRecordStatus
import co.kr.tnt.domain.model.trainee.TraineePtSession
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState
import java.time.LocalDate
import java.time.YearMonth

class TraineeHomeContract {
    data class TraineeHomeUiState(
        val selectedDay: LocalDate = LocalDate.now(),
        val dailyRecordStatus: List<TraineeDailyRecordStatus> = emptyList(),
        val ptSessions: TraineePtSession? = null,
        val recordList: List<DailyRecord> = emptyList(),
    ) : UiState

    sealed interface TraineeHomeUiEvent : UiEvent {
        data class OnChangeVisibleMonth(val yearMonth: YearMonth) : TraineeHomeUiEvent
        data class OnClickPtSessionCard(val ptSessionId: String) : TraineeHomeUiEvent
        data class OnClickDay(val date: LocalDate) : TraineeHomeUiEvent
    }

    sealed interface TraineeHomeEffect : UiSideEffect {
        data class ShowToast(val message: String) : TraineeHomeEffect
    }
}
