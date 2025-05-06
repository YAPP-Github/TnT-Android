package co.kr.tnt.trainer.notification

import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState
import co.kr.tnt.ui.model.NotificationState
import co.kr.tnt.ui.resource.DisplayText

internal class TrainerNotificationContract {
    data class TrainerNotificationUiState(
        val trainerId: String = "",
        val traineeId: String = "",
        val notifications: List<NotificationState> = emptyList(),
    ) : UiState

    sealed interface TrainerNotificationUiEvent : UiEvent {
        data object OnBackClick : TrainerNotificationUiEvent
        data object OnLinkNotificationClick : TrainerNotificationUiEvent
    }

    sealed interface TrainerNotificationEffect : UiSideEffect {
        data class ShowToast(val message: DisplayText) : TrainerNotificationEffect
        data object NavigateToPrevious : TrainerNotificationEffect
        data object NavigateToConnect : TrainerNotificationEffect
    }
}
