package co.kr.tnt.trainer.mypage

import co.kr.tnt.domain.model.User
import co.kr.tnt.domain.model.trainer.TrainerManagementMemberCount
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState

internal class TrainerMyPageContract {
    data class TrainerMyPageUiState(
        val user: User = User.Trainer.EMPTY,
        val isEnablePushNotification: Boolean = false,
        val dialogState: DialogState = DialogState.NONE,
        val managementMemberCount: TrainerManagementMemberCount = TrainerManagementMemberCount.ZERO,
    ) : UiState {
        enum class DialogState {
            NONE,
            LOGOUT_CONFIRM,
            LOGOUT,
            DELETE_ACCOUNT_CONFIRM,
            DELETE_ACCOUNT,
        }
    }

    sealed interface TrainerMyPageUiEvent : UiEvent {
        data object OnTogglePushNotification : TrainerMyPageUiEvent
        data object OnClickServiceTerm : TrainerMyPageUiEvent
        data object OnClickPrivacy : TrainerMyPageUiEvent
        data object OnClickOpenSourceLicense : TrainerMyPageUiEvent
        data object OnClickLogout : TrainerMyPageUiEvent
        data object OnClickDeleteAccount : TrainerMyPageUiEvent
        data object OnClickDialogConfirm : TrainerMyPageUiEvent
        data object OnDismissDialog : TrainerMyPageUiEvent
    }

    sealed interface TrainerMyPageSideEffect : UiSideEffect {
        data object NavigateToLogin : TrainerMyPageSideEffect
        data class NavigateToWebView(val url: String) : TrainerMyPageSideEffect
        data class ShowToast(val message: String) : TrainerMyPageSideEffect
    }
}
