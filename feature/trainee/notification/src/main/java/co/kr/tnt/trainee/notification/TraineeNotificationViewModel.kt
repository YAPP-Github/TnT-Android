package co.kr.tnt.trainee.notification

import co.kr.tnt.domain.model.NotificationInfo
import co.kr.tnt.domain.model.NotificationType
import co.kr.tnt.trainee.notification.TraineeNotificationContract.TraineeNotificationEffect
import co.kr.tnt.trainee.notification.TraineeNotificationContract.TraineeNotificationUiEvent
import co.kr.tnt.trainee.notification.TraineeNotificationContract.TraineeNotificationUiState
import co.kr.tnt.ui.base.BaseViewModel
import co.kr.tnt.ui.model.toUiStateList
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

            val sampleNotifications = listOf(
                NotificationInfo(
                    type = NotificationType.DISCONNECT,
                    title = "트레이너 연결 해제",
                    contents = "박헬린 트레이너가 연결을 끊었어요",
                    time = LocalDateTime.parse("2025-02-03T23:12:00", formatter),
                    isChecked = false,
                ),
                NotificationInfo(
                    type = NotificationType.DISCONNECT,
                    title = "트레이너 연결 해제",
                    contents = "김헬스 트레이너가 연결을 끊었어요",
                    time = LocalDateTime.parse("2025-02-03T23:03:00", formatter),
                    isChecked = true,
                ),
                NotificationInfo(
                    type = NotificationType.DISCONNECT,
                    title = "트레이너 연결 해제",
                    contents = "김피티 트레이너가 연결을 끊었어요",
                    time = LocalDateTime.parse("2025-02-02T22:29:00", formatter),
                    isChecked = true,
                ),
            )
            updateState { copy(notifications = sampleNotifications.toUiStateList()) }
        }

        private fun navigateToBack() {
            sendEffect(TraineeNotificationEffect.NavigateToPrevious)
        }
    }
