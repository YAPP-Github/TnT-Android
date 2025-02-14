package co.kr.tnt.trainee.notification

import co.kr.tnt.domain.model.NotificationInfo
import co.kr.tnt.trainee.notification.TraineeNotificationContract.TraineeNotificationEffect
import co.kr.tnt.trainee.notification.TraineeNotificationContract.TraineeNotificationUiEvent
import co.kr.tnt.trainee.notification.TraineeNotificationContract.TraineeNotificationUiState
import co.kr.tnt.ui.base.BaseViewModel
import co.kr.tnt.ui.model.NotificationState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class TraineeNotificationViewModel @Inject constructor() :
    BaseViewModel<TraineeNotificationUiState, TraineeNotificationUiEvent, TraineeNotificationEffect>(
        TraineeNotificationUiState(),
    ) {
        init {
            getNotification()
        }

        override suspend fun handleEvent(event: TraineeNotificationUiEvent) {
            when (event) {
                TraineeNotificationUiEvent.OnBackClick -> navigateToBack()
            }
        }

        private fun getNotification() {
            // TODO 알림 불러오기
            val sampleNotifications = emptyList<NotificationInfo>()
            updateState { copy(notifications = sampleNotifications.map(NotificationState::fromDomain)) }
        }

        private fun navigateToBack() {
            sendEffect(TraineeNotificationEffect.NavigateToPrevious)
        }
    }
