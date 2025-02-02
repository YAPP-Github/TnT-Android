package co.kr.tnt.trainer.notification

import co.kr.tnt.trainer.notification.TrainerNotificationContract.TrainerNotificationEffect
import co.kr.tnt.trainer.notification.TrainerNotificationContract.TrainerNotificationUiEvent
import co.kr.tnt.trainer.notification.TrainerNotificationContract.TrainerNotificationUiState
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class TrainerNotificationViewModel @Inject constructor() :
    BaseViewModel<TrainerNotificationUiState, TrainerNotificationUiEvent, TrainerNotificationEffect>(
        TrainerNotificationUiState(),
    ) {
        override suspend fun handleEvent(event: TrainerNotificationUiEvent) {
            when (event) {
                TrainerNotificationUiEvent.OnBackClick -> navigateToBack()
                TrainerNotificationUiEvent.OnLinkNotificationClick -> navigateToConnect()
            }
        }

        private fun navigateToBack() {
            sendEffect(TrainerNotificationEffect.NavigateToPrevious)
        }

        private fun navigateToConnect() {
            sendEffect(TrainerNotificationEffect.NavigateToConnect)
        }
    }
