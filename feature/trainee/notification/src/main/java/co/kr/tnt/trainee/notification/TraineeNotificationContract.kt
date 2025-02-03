package co.kr.tnt.trainee.notification

import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState
import co.kr.tnt.ui.model.NotificationState

internal class TraineeNotificationContract {
    data class TraineeNotificationUiState(
        val notifications: List<NotificationState> = emptyList(),
    ) : UiState

    sealed interface TraineeNotificationUiEvent : UiEvent {
        data object OnBackClick : TraineeNotificationUiEvent
    }

    sealed interface TraineeNotificationEffect : UiSideEffect {
        data class ShowToast(val message: String) : TraineeNotificationEffect
        data object NavigateToPrevious : TraineeNotificationEffect
    }
}
