package co.kr.tnt.trainer.home

import androidx.lifecycle.viewModelScope
import co.kr.tnt.domain.model.trainer.DailyPtSessionCount
import co.kr.tnt.domain.repository.TrainerRepository
import co.kr.tnt.trainer.home.TrainerHomeContract.TrainerHomeSideEffect
import co.kr.tnt.trainer.home.TrainerHomeContract.TrainerHomeUiEvent
import co.kr.tnt.trainer.home.TrainerHomeContract.TrainerHomeUiState
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
internal class TrainerHomeViewModel @Inject constructor(
    private val trainerRepository: TrainerRepository,
) :
    BaseViewModel<TrainerHomeUiState, TrainerHomeUiEvent, TrainerHomeSideEffect>(
            TrainerHomeUiState(),
        ) {
        override suspend fun handleEvent(event: TrainerHomeUiEvent) {
            when (event) {
                TrainerHomeUiEvent.OnClickNotification -> sendEffect(TrainerHomeSideEffect.NavigateToNotification)
                is TrainerHomeUiEvent.OnChangeVisibleMonth -> handleChangeVisibleMonth(event.yearMonth)
                is TrainerHomeUiEvent.OnClickDay -> updateState { copy(selectedDay = event.day) }
            }
        }

        private fun handleChangeVisibleMonth(yearMonth: YearMonth) {
            viewModelScope.launch {
                runCatching { trainerRepository.getMonthlyPtSessionCounts(yearMonth) }
                    .onSuccess(::updateMonthlyPtSessionCounts)
                    .onFailure {
                        sendEffect(TrainerHomeSideEffect.ShowToast("알 수 없는 오류가 발생하였습니다."))
                    }
            }
        }

        private fun updateMonthlyPtSessionCounts(monthlyPtSessionCounts: List<DailyPtSessionCount>) {
            val updatedDailyPtSessionCount = currentState.dailyPtSessionCount.toMutableMap()

            monthlyPtSessionCounts.forEach {
                updatedDailyPtSessionCount[it.date] = it.count
            }

            updateState { copy(dailyPtSessionCount = updatedDailyPtSessionCount) }
        }
    }
