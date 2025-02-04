package co.kr.tnt.trainer.notification

import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState
import co.kr.tnt.ui.model.NotificationState

internal class TrainerNotificationContract {
    data class TrainerNotificationUiState(
        val notifications: List<NotificationState> = emptyList(),
    ) : UiState

    sealed interface TrainerNotificationUiEvent : UiEvent {
        data object OnBackClick : TrainerNotificationUiEvent
        data object OnLinkNotificationClick : TrainerNotificationUiEvent
    }

    sealed interface TrainerNotificationEffect : UiSideEffect {
        data class ShowToast(val message: String) : TrainerNotificationEffect
        data object NavigateToPrevious : TrainerNotificationEffect
        data object NavigateToConnect : TrainerNotificationEffect
    }
}
