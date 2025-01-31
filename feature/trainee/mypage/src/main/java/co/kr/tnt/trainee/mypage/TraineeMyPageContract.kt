package co.kr.tnt.trainee.mypage

import co.kr.tnt.trainee.mypage.TraineeMyPageContract.TraineeMyPagePage.MyPage
import co.kr.tnt.trainee.mypage.model.DialogState
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState

internal class TraineeMyPageContract {
    data class TraineeMyPageUiState(
        val page: TraineeMyPagePage = MyPage,
        val image: String? = "",
        val name: String = "",
        val trainerName: String = "",
        val isConnected: Boolean = false,
        val isPushEnabled: Boolean = true,
        val appVersion: String = "0.0.0",
        val dialogState: DialogState = DialogState.LOGOUT,
        val url: String = "",
        val showWebView: Boolean = false,
        val showWarningDialog: Boolean = false,
        val showCompleteDialog: Boolean = false,
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
        data object OnConfirmFirstPopup : TraineeMyPageUiEvent
        data object OnDismissPopup : TraineeMyPageUiEvent
        data object OnBackClick : TraineeMyPageUiEvent
        data object OnConfirmSecondPopup : TraineeMyPageUiEvent
        data object OnWebViewBackClick : TraineeMyPageUiEvent
    }

    sealed interface TraineeMyPageEffect : UiSideEffect {
        data class ShowToast(val message: String) : TraineeMyPageEffect
        data object NavigateToConnect : TraineeMyPageEffect
        data object NavigateToPrevious : TraineeMyPageEffect
        data object NavigateToLogin : TraineeMyPageEffect
    }

    enum class TraineeMyPagePage {
        MyPage,
        WebView,
        ;

        companion object {
            fun getPreviousPage(currentPage: TraineeMyPagePage): TraineeMyPagePage {
                return when (currentPage) {
                    WebView -> MyPage
                    else -> error("No previous page defined for $currentPage")
                }
            }

            fun getNextPage(currentPage: TraineeMyPagePage): TraineeMyPagePage {
                return when (currentPage) {
                    MyPage -> WebView
                    else -> error("No next page defined for $currentPage")
                }
            }
        }
    }
}
