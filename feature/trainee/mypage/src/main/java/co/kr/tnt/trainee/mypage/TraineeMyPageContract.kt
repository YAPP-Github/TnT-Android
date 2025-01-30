package co.kr.tnt.trainee.mypage

import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState

internal class TraineeMyPageContract {
    data class TraineeMyPageUiState(
        val image: String? = "",
        val name: String = "이름",
        val isConnected: Boolean = false,
        val isPushEnabled: Boolean = true,
        val appVersion: String = "0.0.0",
    ) : UiState

    sealed interface TraineeMyPageUiEvent : UiEvent {
        data object OnEditButtonClick : TraineeMyPageUiEvent
        data object OnConnectButtonClick : TraineeMyPageUiEvent
        data object OnDisconnectButtonClick : TraineeMyPageUiEvent
        data object ToggleNotification : TraineeMyPageUiEvent
        data object OnServiceTermClick : TraineeMyPageUiEvent
        data object OnPrivacyClick : TraineeMyPageUiEvent
        data object OnOpenSourceClick : TraineeMyPageUiEvent
        data object OnLogoutClick : TraineeMyPageUiEvent
        data object OnDeleteAccountClick : TraineeMyPageUiEvent
        data object OnBackClick : TraineeMyPageUiEvent
    }

    sealed interface TraineeMyPageEffect : UiSideEffect {
        data class ShowToast(val message: String) : TraineeMyPageEffect
        data object NavigateToConnect : TraineeMyPageEffect
        data object NavigateToPrevious : TraineeMyPageEffect
    }
}
