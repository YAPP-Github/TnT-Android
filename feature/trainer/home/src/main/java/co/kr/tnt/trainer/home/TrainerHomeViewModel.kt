package co.kr.tnt.trainer.home

import androidx.lifecycle.viewModelScope
import co.kr.tnt.domain.model.PtSession
import co.kr.tnt.domain.model.trainer.TrainerDailyPtSessionCount
import co.kr.tnt.domain.repository.TrainerRepository
import co.kr.tnt.trainer.home.TrainerHomeContract.TrainerHomeSideEffect
import co.kr.tnt.trainer.home.TrainerHomeContract.TrainerHomeUiEvent
import co.kr.tnt.trainer.home.TrainerHomeContract.TrainerHomeUiState
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.time.LocalDate
import java.time.YearMonth
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import javax.inject.Inject

@HiltViewModel
internal class TrainerHomeViewModel @Inject constructor(
    private val trainerRepository: TrainerRepository,
) :
    BaseViewModel<TrainerHomeUiState, TrainerHomeUiEvent, TrainerHomeSideEffect>(TrainerHomeUiState()) {
        private val cachedMonthlyPtSessionCounts: ConcurrentMap<YearMonth, List<TrainerDailyPtSessionCount>> =
            ConcurrentHashMap()
        private val cachedDailyPtSession: ConcurrentMap<LocalDate, List<PtSession>> = ConcurrentHashMap()

        init {
            selectDay(LocalDate.now())
        }

        override suspend fun handleEvent(event: TrainerHomeUiEvent) {
            when (event) {
                TrainerHomeUiEvent.OnScreen -> refresh()
                TrainerHomeUiEvent.OnClickNotification -> sendEffect(TrainerHomeSideEffect.NavigateToNotification)
                is TrainerHomeUiEvent.OnChangeVisibleMonth -> handleChangeVisibleMonth(event.yearMonth)
                is TrainerHomeUiEvent.OnClickDay -> selectDay(event.day)
                TrainerHomeUiEvent.OnClickAddPtSession -> sendEffect(TrainerHomeSideEffect.NavigateToAddPtSession)
            }
        }

        private fun handleChangeVisibleMonth(yearMonth: YearMonth) {
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

        private fun refresh() {
            cachedMonthlyPtSessionCounts.clear()
            cachedDailyPtSession.clear()
            selectDay(currentState.selectedDay)
        }
    }
