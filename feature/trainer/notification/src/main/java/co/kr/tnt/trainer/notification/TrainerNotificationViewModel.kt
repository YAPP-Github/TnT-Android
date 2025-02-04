package co.kr.tnt.trainer.notification

import co.kr.tnt.domain.model.NotificationInfo
import co.kr.tnt.domain.model.NotificationType
import co.kr.tnt.trainer.notification.TrainerNotificationContract.TrainerNotificationEffect
import co.kr.tnt.trainer.notification.TrainerNotificationContract.TrainerNotificationUiEvent
import co.kr.tnt.trainer.notification.TrainerNotificationContract.TrainerNotificationUiState
import co.kr.tnt.ui.base.BaseViewModel
import co.kr.tnt.ui.model.NotificationState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
internal class TrainerNotificationViewModel @Inject constructor() :
    BaseViewModel<TrainerNotificationUiState, TrainerNotificationUiEvent, TrainerNotificationEffect>(
        TrainerNotificationUiState(),
    ) {
        init {
            getNotification()
        }

        override suspend fun handleEvent(event: TrainerNotificationUiEvent) {
            when (event) {
                TrainerNotificationUiEvent.OnBackClick -> navigateToBack()
                TrainerNotificationUiEvent.OnLinkNotificationClick -> navigateToConnect()
            }
        }

        private fun getNotification() {
            // TODO 알림 불러오기
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

            val sampleNotifications = listOf(
                NotificationInfo(
                    type = NotificationType.CONNECT_COMPLETE,
                    title = "트레이니 연결 완료",
                    contents = "김회원 회원과 연결되었어요",
                    time = LocalDateTime.parse("2025-02-03T23:59:00", formatter),
                    isChecked = false,
                ),
                NotificationInfo(
                    type = NotificationType.CONNECT_COMPLETE,
                    title = "트레이니 연결 완료",
                    contents = "김돌돌 회원과 연결되었어요",
                    time = LocalDateTime.parse("2025-02-03T11:12:00", formatter),
                    isChecked = false,
                ),
                NotificationInfo(
                    type = NotificationType.DISCONNECT,
                    title = "트레이니 연결 해제",
                    contents = "박헬린 회원이 연결을 끊었어요",
                    time = LocalDateTime.parse("2025-02-03T23:12:00", formatter),
                    isChecked = true,
                ),
            )
            updateState { copy(notifications = sampleNotifications.map(NotificationState::fromDomain)) }
        }

        private fun navigateToBack() {
            sendEffect(TrainerNotificationEffect.NavigateToPrevious)
        }

        private fun navigateToConnect() {
            sendEffect(TrainerNotificationEffect.NavigateToConnect)
        }
    }
