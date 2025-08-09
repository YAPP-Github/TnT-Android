package co.kr.tnt.trainee.notification

import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState
import co.kr.tnt.ui.model.NotificationState
import co.kr.tnt.ui.resource.DisplayText

internal class TraineeNotificationContract {
    data class TraineeNotificationUiState(
        val notifications: List<NotificationState> = emptyList(),
    ) : UiState

    sealed interface TraineeNotificationUiEvent : UiEvent {
        data object OnClickBack : TraineeNotificationUiEvent
    }

    sealed interface TraineeNotificationEffect : UiSideEffect {
        data class ShowToast(val message: DisplayText) : TraineeNotificationEffect
        data object NavigateToPrevious : TraineeNotificationEffect
    }
}
