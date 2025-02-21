package co.kr.tnt.trainer.home

import androidx.lifecycle.viewModelScope
import co.kr.tnt.domain.model.PtSession
import co.kr.tnt.domain.model.trainer.TrainerDailyPtSessionCount
import co.kr.tnt.domain.repository.ConnectRepository
import co.kr.tnt.domain.repository.TrainerRepository
import co.kr.tnt.trainer.home.TrainerHomeContract.TrainerHomeSideEffect
import co.kr.tnt.trainer.home.TrainerHomeContract.TrainerHomeUiEvent
import co.kr.tnt.trainer.home.TrainerHomeContract.TrainerHomeUiState
import co.kr.tnt.trainer.home.TrainerHomeContract.TrainerHomeUiState.DialogState
import co.kr.tnt.ui.base.BaseViewModel
import co.kr.tnt.ui.model.SnackbarType
import com.kizitonwose.calendar.core.yearMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

const val DIALOG_HIDE_DURATION_HOURS = 72

@HiltViewModel
internal class TrainerHomeViewModel @Inject constructor(
    private val trainerRepository: TrainerRepository,
    private val connectRepository: ConnectRepository,
) :
    BaseViewModel<TrainerHomeUiState, TrainerHomeUiEvent, TrainerHomeSideEffect>(TrainerHomeUiState()) {
        private val cachedMonthlyPtSessionCounts: ConcurrentHashMap<YearMonth, List<TrainerDailyPtSessionCount>> =
            ConcurrentHashMap()
        private val cachedDailyPtSession: ConcurrentHashMap<LocalDate, List<PtSession>> = ConcurrentHashMap()

        init {
            selectDay(LocalDate.now())
        }

        override suspend fun handleEvent(event: TrainerHomeUiEvent) {
            when (event) {
                TrainerHomeUiEvent.OnScreen -> refresh()
                TrainerHomeUiEvent.OnClickNotification -> sendEffect(TrainerHomeSideEffect.NavigateToNotification)
                is TrainerHomeUiEvent.OnChangeVisibleMonth -> getMonthlySessionCounts(event.yearMonth)
                is TrainerHomeUiEvent.OnClickDay -> selectDay(event.day)
                TrainerHomeUiEvent.OnClickAddPtSession -> showConnectDialog(false)

                is TrainerHomeUiEvent.OnClickPtSessionComplete -> completePtSession(event.ptSession)
                TrainerHomeUiEvent.OnChangeHideDialogOption -> toggleDialogHiddenState()
                TrainerHomeUiEvent.OnConfirmConnectDialog -> handleDialogConfirm()
                TrainerHomeUiEvent.OnDismissDialog -> dismissDialog()
            }
        }

        private fun getMonthlySessionCounts(yearMonth: YearMonth) {
            // 현재 달을 기준으로 2개월 전부터 2개월 후까지의 데이터를 한 번에 요청합니다.
            val targetRange = -2L..2L

            viewModelScope.launch {
                supervisorScope {
                    runCatching {
                        val results = targetRange.map { operationValue ->
                            async {
                                val targetMonth = yearMonth.plusMonths(operationValue)

                                cachedMonthlyPtSessionCounts.getOrElse(targetMonth) {
                                    trainerRepository.getMonthlyPtSessionCounts(targetMonth).also {
                                        cachedMonthlyPtSessionCounts[targetMonth] = it
                                    }
                                }
                            }
                        }.awaitAll()

                        results.forEach(::updateMonthlyPtSessionCounts)
                    }.onFailure {
                        sendEffect(TrainerHomeSideEffect.ShowToast("서버 요청에 실패했어요"))
                    }
                }
            }
        }

        private fun selectDay(day: LocalDate) {
            updateState { copy(selectedDay = day) }

            viewModelScope.launch {
                runCatching {
                    cachedDailyPtSession.getOrElse(day) {
                        trainerRepository.getDailyPtSessions(day).sessions.also {
                            cachedDailyPtSession[day] = it
                        }
                    }
                }.onSuccess { selectedDayPtSessions ->
                    updateState { copy(selectedDayPtSessions = selectedDayPtSessions) }
                }.onFailure {
                    updateState { copy(selectedDayPtSessions = null) }
                    sendEffect(TrainerHomeSideEffect.ShowToast("서버 요청에 실패했어요"))
                }
            }
        }

        private fun updateMonthlyPtSessionCounts(monthlyPtSessionCounts: List<TrainerDailyPtSessionCount>) {
            val updatedDailyPtSessionCount = currentState.dailyPtSessionCount.toMutableMap()

            monthlyPtSessionCounts.forEach {
                updatedDailyPtSessionCount[it.date] = it.count
            }

            updateState { copy(dailyPtSessionCount = updatedDailyPtSessionCount) }
        }

        private fun completePtSession(ptSession: PtSession) {
            viewModelScope.launch {
                runCatching {
                    trainerRepository.postCompleteSession(ptSession.id)
                }.onSuccess {
                    getDailyPtSessions(currentState.selectedDay)
                    sendEffect(
                        TrainerHomeSideEffect.ShowToast(
                            message = "PT 수업을 완료했어요",
                            type = SnackbarType.SUCCESS,
                        ),
                    )
                }.onFailure { throwable ->
                    sendEffect(TrainerHomeSideEffect.ShowToast(throwable.message ?: "서버 요청에 실패했어요"))
                }
            }
        }

        private fun getDailyPtSessions(day: LocalDate) {
            viewModelScope.launch {
                runCatching {
                    trainerRepository.getDailyPtSessions(day)
                }.onSuccess {
                    cachedDailyPtSession[day] = it.sessions
                    if (day == currentState.selectedDay) {
                        updateState { copy(selectedDayPtSessions = cachedDailyPtSession[day]) }
                    }
                }.onFailure {
                    sendEffect(TrainerHomeSideEffect.ShowToast("서버 요청에 실패했어요"))
                }
            }
        }

        private fun refresh() {
            cachedMonthlyPtSessionCounts.clear()
            cachedDailyPtSession.clear()
            selectDay(currentState.selectedDay)
            getMonthlySessionCounts(currentState.selectedDay.yearMonth)
            showConnectDialog(true)
        }

        private fun showConnectDialog(triggeredByHome: Boolean) {
            val currentDateTime = LocalDateTime.now()

            viewModelScope.launch {
                runCatching {
                    trainerRepository.getActiveMembers()
                }.onSuccess { result ->
                    if (result.isNotEmpty()) {
                        updateState { copy(dialogState = DialogState.NONE) }
                        if (triggeredByHome.not()) {
                            sendEffect(TrainerHomeSideEffect.NavigateToAddPtSession)
                        }
                        return@launch
                    }
                }

                val lastHiddenDate = connectRepository.getExplicitDeniedConnectDate().firstOrNull()
                val isHidden = lastHiddenDate != null &&
                    Duration.between(lastHiddenDate, currentDateTime).toHours() < DIALOG_HIDE_DURATION_HOURS

                if (isHidden.not() && triggeredByHome) {
                    updateState { copy(dialogState = DialogState.HOME_CONNECT) }
                } else if (triggeredByHome.not()) {
                    updateState { copy(dialogState = DialogState.ADD_PT_CONNECT) }
                }
            }
        }

        private fun toggleDialogHiddenState() {
            updateState { copy(isDialogHiddenForThreeDays = !isDialogHiddenForThreeDays) }
        }

        private fun handleDialogConfirm() {
            if (currentState.isDialogHiddenForThreeDays) {
                updateCurrentDateTime()
            }
            val effect = when (currentState.dialogState) {
                DialogState.HOME_CONNECT -> TrainerHomeSideEffect.NavigateToInvite
                DialogState.ADD_PT_CONNECT -> TrainerHomeSideEffect.NavigateToInvite
                else -> return
            }
            updateState {
                copy(dialogState = DialogState.NONE, isDialogHiddenForThreeDays = false)
            }
            sendEffect(effect)
        }

        private fun updateCurrentDateTime() {
            val currentDateTime = LocalDateTime.now()
            viewModelScope.launch {
                connectRepository.updateExplicitDeniedConnectDate(currentDateTime)
            }
        }

        private fun dismissDialog() {
            if (currentState.isDialogHiddenForThreeDays) {
                updateCurrentDateTime()
            }
            updateState {
                copy(
                    dialogState = DialogState.NONE,
                    isDialogHiddenForThreeDays = false,
                )
            }
        }
    }
