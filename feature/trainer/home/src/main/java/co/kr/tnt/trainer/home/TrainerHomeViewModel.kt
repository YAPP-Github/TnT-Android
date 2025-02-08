package co.kr.tnt.trainer.home

import co.kr.tnt.trainer.home.TrainerHomeContract.TrainerHomeSideEffect
import co.kr.tnt.trainer.home.TrainerHomeContract.TrainerHomeUiEvent
import co.kr.tnt.trainer.home.TrainerHomeContract.TrainerHomeUiState
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class TrainerHomeViewModel @Inject constructor() :
    BaseViewModel<TrainerHomeUiState, TrainerHomeUiEvent, TrainerHomeSideEffect>(
        TrainerHomeUiState(),
    ) {
        override suspend fun handleEvent(event: TrainerHomeUiEvent) {
            when (event) {
                TrainerHomeUiEvent.OnClickNotification -> sendEffect(TrainerHomeSideEffect.NavigateToNotification)
                is TrainerHomeUiEvent.OnChangeVisibleMonth -> handleChangeVisibleMonth()
                is TrainerHomeUiEvent.OnClickDay -> updateState { copy(selectedDay = event.day) }
            }
        }

        private fun handleChangeVisibleMonth() {
            // TODO
        }
    }
