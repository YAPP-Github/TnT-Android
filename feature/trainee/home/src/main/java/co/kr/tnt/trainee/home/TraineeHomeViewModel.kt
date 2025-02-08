package co.kr.tnt.trainee.home

import co.kr.tnt.trainee.home.TraineeHomeContract.TraineeHomeEffect
import co.kr.tnt.trainee.home.TraineeHomeContract.TraineeHomeUiEvent
import co.kr.tnt.trainee.home.TraineeHomeContract.TraineeHomeUiState
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class TraineeHomeViewModel @Inject constructor() :
    BaseViewModel<TraineeHomeUiState, TraineeHomeUiEvent, TraineeHomeEffect>(
        TraineeHomeUiState(),
    ) {
        init {
            updateCalenderState()
        }

        override suspend fun handleEvent(event: TraineeHomeUiEvent) {
            when (event) {
                TraineeHomeUiEvent.OnClickNextWeek -> moveToNextWeek()
                TraineeHomeUiEvent.OnClickPreviousWeek -> moveToPreviousWeek()
                is TraineeHomeUiEvent.OnClickDay -> selectDate(event.date)
            }
        }

        // TODO : 주간 캘린더 API 연동
        private fun updateCalenderState() {
            val today = LocalDate.now()
            val list = List(10) {
                today.minusDays((0..30).random().toLong())
            }
            updateState { copy(markedDates = list) }
        }

        private fun selectDate(date: LocalDate) {
            updateState { copy(selectedDate = date) }
        }

        private fun moveToNextWeek() {
            selectDate(currentState.selectedDate.plusWeeks(1))
        }

        private fun moveToPreviousWeek() {
            selectDate(currentState.selectedDate.minusWeeks(1))
        }
    }
