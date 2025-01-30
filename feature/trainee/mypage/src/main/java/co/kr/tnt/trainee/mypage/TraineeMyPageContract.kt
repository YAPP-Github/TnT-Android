package co.kr.tnt.trainee.mypage

import android.content.Context
import co.kr.tnt.trainee.mypage.model.PopupType
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState

internal class TraineeMyPageContract {
    data class TraineeMyPageUiState(
        val image: String? = "",
        val name: String = "",
        val trainerName: String = "",
        val isConnected: Boolean = false,
        val isPushEnabled: Boolean = true,
        val appVersion: String = "0.0.0",
        val popupType: PopupType = PopupType.LOGOUT,
        val showFirstPopup: Boolean = false,
        val showSecondPopup: Boolean = false,
    ) : UiState

    sealed interface TraineeMyPageUiEvent : UiEvent {
        data object OnEditButtonClick : TraineeMyPageUiEvent
        data object OnConnectButtonClick : TraineeMyPageUiEvent
        data object OnDisconnectButtonClick : TraineeMyPageUiEvent
        data object ToggleNotification : TraineeMyPageUiEvent
        data class OnServiceTermClick(val context: Context) : TraineeMyPageUiEvent
        data class OnPrivacyClick(val context: Context) : TraineeMyPageUiEvent
        data object OnOpenSourceClick : TraineeMyPageUiEvent
        data object OnLogoutClick : TraineeMyPageUiEvent
        data object OnDeleteAccountClick : TraineeMyPageUiEvent
        data object OnConfirmFirstPopup : TraineeMyPageUiEvent
        data object OnDismissPopup : TraineeMyPageUiEvent
        data object OnBackClick : TraineeMyPageUiEvent
        data object OnConfirmSecondPopup : TraineeMyPageUiEvent
    }

    sealed interface TraineeMyPageEffect : UiSideEffect {
        data class ShowToast(val message: String) : TraineeMyPageEffect
        data object NavigateToConnect : TraineeMyPageEffect
        data object NavigateToPrevious : TraineeMyPageEffect
        data object NavigateToLogin : TraineeMyPageEffect
    }
}
