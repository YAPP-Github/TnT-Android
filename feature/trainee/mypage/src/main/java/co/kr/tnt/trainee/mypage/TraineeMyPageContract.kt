package co.kr.tnt.trainee.mypage

import co.kr.tnt.domain.model.User
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState

internal class TraineeMyPageContract {
    data class TraineeMyPageUiState(
        val user: User.Trainee = User.Trainee.EMPTY,
        val trainerName: String = "",
        val isEnablePushNotification: Boolean = true,
        val dialogState: DialogState = DialogState.NONE,
    ) : UiState {
        enum class DialogState {
            NONE,
            DISCONNECT_CONFIRM,
            DISCONNECT,
            LOGOUT_CONFIRM,
            LOGOUT,
            DELETE_ACCOUNT_CONFIRM,
            DELETE_ACCOUNT,
            SHOULD_ALLOW_PERMISSION,
        }
    }

    sealed interface TraineeMyPageUiEvent : UiEvent {
        data object OnClickConnect : TraineeMyPageUiEvent
        data object OnClickDisconnect : TraineeMyPageUiEvent
        data object ToggleNotification : TraineeMyPageUiEvent
        data object OnClickTermsOfService : TraineeMyPageUiEvent
        data object OnClickPrivacy : TraineeMyPageUiEvent
        data object OnClickOpenSource : TraineeMyPageUiEvent
        data object OnClickLogout : TraineeMyPageUiEvent
        data object OnClickDeleteAccount : TraineeMyPageUiEvent
        data object OnClickDialogConfirm : TraineeMyPageUiEvent
        data object OnDismissDialog : TraineeMyPageUiEvent
    }

    sealed interface TraineeMyPageEffect : UiSideEffect {
        data class ShowToast(val message: String) : TraineeMyPageEffect
        data object NavigateToConnect : TraineeMyPageEffect
        data object NavigateToLogin : TraineeMyPageEffect
        data class NavigateToWebView(val url: String) : TraineeMyPageEffect
    }
}
