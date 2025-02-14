package co.kr.tnt.trainee.home

import androidx.lifecycle.viewModelScope
import co.kr.tnt.domain.model.trainee.TraineeDailyRecordStatus
import co.kr.tnt.domain.repository.ConnectRepository
import co.kr.tnt.domain.repository.TraineeRepository
import co.kr.tnt.trainee.home.TraineeHomeContract.TraineeHomeEffect
import co.kr.tnt.trainee.home.TraineeHomeContract.TraineeHomeUiEvent
import co.kr.tnt.trainee.home.TraineeHomeContract.TraineeHomeUiState
import co.kr.tnt.ui.base.BaseViewModel
import com.kizitonwose.calendar.core.yearMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

const val DIALOG_HIDE_DURATION_HOURS = 72

@HiltViewModel
internal class TraineeHomeViewModel @Inject constructor(
    private val traineeRepository: TraineeRepository,
    private val connectRepository: ConnectRepository,
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
            TraineeHomeUiEvent.OnChangeHideDialogOption -> toggleDialogHiddenState()
            TraineeHomeUiEvent.OnConfirmConnectDialog -> handleConfirmDialog()
            TraineeHomeUiEvent.OnDismissDialog -> dismissDialog()
        }
    }

    private fun handleChangeVisibleMonth(visibleYearMonth: YearMonth) {
        if (cachedMonthlyRecordState.containsKey(visibleYearMonth)) {
            cachedMonthlyRecordState[visibleYearMonth]?.let { dates ->
                updateMonthlyRecordStatus(dates)
            }
            return
        }
        val startDate = visibleYearMonth.atDay(1).minusDays(7).toString()
        val endDate = visibleYearMonth.atEndOfMonth().plusDays(7).toString()

        viewModelScope.launch {
            runCatching {
                traineeRepository.getWeeklyRecordedDate(startDate, endDate).also { result ->
                    val mergedData = mergeCachedData(visibleYearMonth, result)

                    cachedMonthlyRecordState[visibleYearMonth] = mergedData
                    updateMonthlyRecordStatus(mergedData)
                }
            }.onFailure {
                sendEffect(TraineeHomeEffect.ShowToast("서버 요청에 실패했어요."))
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
        viewModelScope.launch {
            runCatching {
                traineeRepository.getDailyRecord(date)
            }.onSuccess { result ->
                updateState {
                    copy(
                        selectedDay = date,
                        ptSessions = result.ptSession,
                        recordList = result.record,
                    )
                }
            }.onFailure {
                sendEffect(TraineeHomeEffect.ShowToast("서버 요청에 실패했어요."))
            }
        }
    }

    private fun toggleDialogHiddenState() {
        updateState { copy(isDialogHiddenForThreeDays = !isDialogHiddenForThreeDays) }
    }

    private fun handleConfirmDialog() {
        if (currentState.isDialogHiddenForThreeDays) {
            updateCurrentDateTime()
        }
        updateState {
            copy(
                showConnectDialog = false,
                isDialogHiddenForThreeDays = false,
            )
        }
        sendEffect(TraineeHomeEffect.NavigateToConnect)
    }

    private fun dismissDialog() {
        if (currentState.isDialogHiddenForThreeDays) {
            updateCurrentDateTime()
        }
        updateState {
            copy(
                showConnectDialog = false,
                isDialogHiddenForThreeDays = false,
            )
        }
    }

    private fun updateCurrentDateTime() {
        val currentDateTime = LocalDateTime.now()
        viewModelScope.launch {
            connectRepository.updateExplicitDeniedConnectDate(currentDateTime)
        }
    }

    private fun refresh() {
        cachedMonthlyRecordState.clear()
        handleChangeVisibleMonth(currentState.selectedDay.yearMonth)
        selectDate(currentState.selectedDay)
        showConnectDialog()
    }

    private fun showConnectDialog() {
        val currentDateTime = LocalDateTime.now()

        viewModelScope.launch {
            runCatching {
                traineeRepository.getMyInfo()
            }.onSuccess { result ->
                updateState { copy(isConnected = result.isConnected) }
                if (result.isConnected) {
                    return@launch
                }
            }.onFailure {
                sendEffect(TraineeHomeEffect.ShowToast("서버 요청에 실패했어요."))
            }

            val lastHiddenDate = connectRepository.getExplicitDeniedConnectDate().firstOrNull()
            val isHidden = lastHiddenDate != null &&
                Duration.between(lastHiddenDate, currentDateTime).toHours() < DIALOG_HIDE_DURATION_HOURS

            if (isHidden.not()) {
                updateState { copy(showConnectDialog = true) }
            }
        }
    }
}
