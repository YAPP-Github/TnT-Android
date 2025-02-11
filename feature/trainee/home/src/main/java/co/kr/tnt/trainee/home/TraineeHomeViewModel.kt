package co.kr.tnt.trainee.home

import androidx.lifecycle.viewModelScope
import co.kr.tnt.domain.repository.TraineeRepository
import co.kr.tnt.trainee.home.TraineeHomeContract.TraineeHomeEffect
import co.kr.tnt.trainee.home.TraineeHomeContract.TraineeHomeUiEvent
import co.kr.tnt.trainee.home.TraineeHomeContract.TraineeHomeUiState
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
internal class TraineeHomeViewModel @Inject constructor(
    private val traineeRepository: TraineeRepository,
) : BaseViewModel<TraineeHomeUiState, TraineeHomeUiEvent, TraineeHomeEffect>(
        TraineeHomeUiState(),
    ) {
    override suspend fun handleEvent(event: TraineeHomeUiEvent) {
        when (event) {
            is TraineeHomeUiEvent.OnChangeVisibleMonth -> updateCalenderState(event.yearMonth)
            is TraineeHomeUiEvent.OnClickDay -> selectDate(event.date)
            is TraineeHomeUiEvent.OnClickPtSessionCard -> checkSessionRecord(event.ptSessionId)
            TraineeHomeUiEvent.OnClickExerciseRecord -> sendEffect(TraineeHomeEffect.NavigateToExerciseRecord)
            TraineeHomeUiEvent.OnClickMealRecord -> sendEffect(TraineeHomeEffect.NavigateToMealRecord)
        }
    }

    private fun updateCalenderState(visibleYearMonth: YearMonth) {
        // TODO : 주간 캘린더 일정 여부 확인 API 연동
        viewModelScope.launch {
            val result = traineeRepository.getDailyDataStatus(visibleYearMonth)
            updateState { copy(dailyRecordStatus = result) }
        }
    }

    @Suppress("UnusedParameter")
    private fun checkSessionRecord(ptSessionId: String) {
        // TODO: pt 수업 기록 확인 화면 이동
    }

    private fun selectDate(date: LocalDate) {
        // TODO : 선택된 날짜의 PT 수업, 기록 불러오기 API 연동
        viewModelScope.launch {
            val result = traineeRepository.getTraineeDailyRecord(date)
            updateState {
                copy(
                    selectedDay = date,
                    ptSessions = result.ptSession,
                    recordList = result.record,
                )
            }
        }
    }
}
