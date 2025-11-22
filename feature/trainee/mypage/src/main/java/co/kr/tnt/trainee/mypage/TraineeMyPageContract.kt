package co.kr.tnt.trainee.mypage

import co.kr.tnt.domain.model.User
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState
import co.kr.tnt.ui.resource.DisplayText

internal class TraineeMyPageContract {
    data class TraineeMyPageUiState(
        val user: User.Trainee = User.Trainee.EMPTY,
        val trainerName: String = "",
        val isEnablePushNotification: Boolean = true,
        val isLoading: Boolean = false,
        val dialogState: DialogState = DialogState.NONE,
    ) : UiState {
        enum class DialogState {
            NONE,
            LOGOUT_CONFIRM,
            LOGOUT,
            DELETE_ACCOUNT_CONFIRM,
            DELETE_ACCOUNT,
            SHOULD_ALLOW_PERMISSION,
        }
    }

    sealed interface TraineeMyPageUiEvent : UiEvent {
        data class OnToggleNotification(
            val isGrantedPermission: Boolean,
            val shouldShowRationale: Boolean,
        ) : TraineeMyPageUiEvent

        data object OnClickModifyMyInfo : TraineeMyPageUiEvent
        data object OnClickConnect : TraineeMyPageUiEvent
        data object OnClickTermsOfService : TraineeMyPageUiEvent
        data object OnClickPrivacy : TraineeMyPageUiEvent
        data object OnClickOpenSource : TraineeMyPageUiEvent
        data object OnClickLogout : TraineeMyPageUiEvent
        data object OnClickDeleteAccount : TraineeMyPageUiEvent
        data object OnClickDialogConfirm : TraineeMyPageUiEvent
        data object OnDismissDialog : TraineeMyPageUiEvent
    }

    sealed interface TraineeMyPageEffect : UiSideEffect {
        data class ShowToast(val message: DisplayText) : TraineeMyPageEffect
        data object NavigateToModifyMyInfo : TraineeMyPageEffect
        data object NavigateToConnect : TraineeMyPageEffect
        data object NavigateToLogin : TraineeMyPageEffect
        data class NavigateToWebView(val url: String) : TraineeMyPageEffect
        data object NavigateToOpenSourceLicense : TraineeMyPageEffect
        data class RequestPermission(val isExplicitlyDenied: Boolean) : TraineeMyPageEffect
    }
}
