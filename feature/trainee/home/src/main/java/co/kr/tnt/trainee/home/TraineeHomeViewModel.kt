package co.kr.tnt.trainee.home

import androidx.lifecycle.viewModelScope
import co.kr.tnt.domain.model.trainee.TraineeDailyRecordStatus
import co.kr.tnt.domain.repository.TraineeRepository
import co.kr.tnt.trainee.home.TraineeHomeContract.TraineeHomeEffect
import co.kr.tnt.trainee.home.TraineeHomeContract.TraineeHomeUiEvent
import co.kr.tnt.trainee.home.TraineeHomeContract.TraineeHomeUiState
import co.kr.tnt.ui.base.BaseViewModel
import com.kizitonwose.calendar.core.yearMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

@HiltViewModel
internal class TraineeHomeViewModel @Inject constructor(
    private val traineeRepository: TraineeRepository,
) : BaseViewModel<TraineeHomeUiState, TraineeHomeUiEvent, TraineeHomeEffect>(
        TraineeHomeUiState(),
    ) {
    private val cachedMonthlyRecordState: ConcurrentHashMap<YearMonth, TraineeDailyRecordStatus> =
        ConcurrentHashMap()

    override suspend fun handleEvent(event: TraineeHomeUiEvent) {
        when (event) {
            TraineeHomeUiEvent.OnScreen -> refresh()
            is TraineeHomeUiEvent.OnChangeVisibleMonth -> handleChangeVisibleMonth(event.yearMonth)
            is TraineeHomeUiEvent.OnClickDay -> selectDate(event.date)
            is TraineeHomeUiEvent.OnClickPtSessionCard -> checkSessionRecord(event.ptSessionId)
            TraineeHomeUiEvent.OnClickExerciseRecord -> sendEffect(TraineeHomeEffect.NavigateToExerciseRecord)
            TraineeHomeUiEvent.OnClickMealRecord -> sendEffect(TraineeHomeEffect.NavigateToMealRecord)
        }
    }

    private fun handleChangeVisibleMonth(visibleYearMonth: YearMonth) {
        if (cachedMonthlyRecordState.containsKey(visibleYearMonth)) {
            cachedMonthlyRecordState[visibleYearMonth]?.let { dates ->
                updateMonthlyRecordStatus(dates)
            }
            return
        }
        val startDate = visibleYearMonth.minusMonths(1).atDay(20).toString()
        val endDate = visibleYearMonth.plusMonths(1).atDay(7).toString()

        viewModelScope.launch {
            runCatching {
                traineeRepository.getWeeklyRecordedDate(startDate, endDate).also { result ->
                    val mergedData = mergeCachedData(visibleYearMonth, result)

                    cachedMonthlyRecordState[visibleYearMonth] = mergedData
                    updateMonthlyRecordStatus(mergedData)
                }
            }.onFailure {
                sendEffect(TraineeHomeEffect.ShowToast("서버 요청에 실패했어요"))
            }
        }
    }

    private fun mergeCachedData(
        yearMonth: YearMonth,
        newData: TraineeDailyRecordStatus,
    ): TraineeDailyRecordStatus {
        val existingData = cachedMonthlyRecordState[yearMonth]

        return if (existingData == null) {
            newData
        } else {
            TraineeDailyRecordStatus(
                dates = (existingData.dates + newData.dates).distinct(),
            )
        }
    }

    private fun updateMonthlyRecordStatus(monthlyRecordStatus: TraineeDailyRecordStatus) {
        updateState { copy(recordedDates = monthlyRecordStatus) }
    }

    @Suppress("UnusedParameter")
    private fun checkSessionRecord(ptSessionId: Long) {
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

    private fun refresh() {
        cachedMonthlyRecordState.clear()
        handleChangeVisibleMonth(currentState.selectedDay.yearMonth)
        selectDate(currentState.selectedDay)
    }
}
