package co.kr.tnt.trainee.mypage

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.trainee.mypage.TraineeMyPageContract.TraineeMyPageEffect
import co.kr.tnt.trainee.mypage.TraineeMyPageContract.TraineeMyPageUiEvent
import co.kr.tnt.trainee.mypage.TraineeMyPageContract.TraineeMyPageUiState

@Composable
internal fun TraineeMyPageRoute(
    navigateToPrevious: () -> Unit,
    navigateToConnect: () -> Unit,
    navigateToLogin: () -> Unit,
    viewModel: TraineeMyPageViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TraineeMyPageScreen(
        state = uiState,
        onBackClick = { viewModel.setEvent(TraineeMyPageUiEvent.OnBackClick) },
        onEditButtonClick = { viewModel.setEvent(TraineeMyPageUiEvent.OnEditButtonClick) },
        onConnectButtonClick = { viewModel.setEvent(TraineeMyPageUiEvent.OnConnectButtonClick) },
        onDisconnectButtonClick = { viewModel.setEvent(TraineeMyPageUiEvent.OnDisconnectButtonClick) },
        onPushNotificationToggle = { viewModel.setEvent(TraineeMyPageUiEvent.ToggleNotification) },
        onServiceTermClick = { viewModel.setEvent(TraineeMyPageUiEvent.OnServiceTermClick) },
        onPrivacyClick = { viewModel.setEvent(TraineeMyPageUiEvent.OnPrivacyClick) },
        onDismissWebView = { viewModel.setEvent(TraineeMyPageUiEvent.OnWebViewBackClick) },
        onOpenSourceClick = { viewModel.setEvent(TraineeMyPageUiEvent.OnOpenSourceClick) },
        onLogoutClick = { viewModel.setEvent(TraineeMyPageUiEvent.OnLogoutClick) },
        onDeleteAccountClick = { viewModel.setEvent(TraineeMyPageUiEvent.OnDeleteAccountClick) },
        onDismissPopup = { viewModel.setEvent(TraineeMyPageUiEvent.OnDismissPopup) },
        onConfirmFirstPopup = { viewModel.setEvent(TraineeMyPageUiEvent.OnConfirmFirstPopup) },
        onConfirmSecondPopup = { viewModel.setEvent(TraineeMyPageUiEvent.OnConfirmSecondPopup) },
    )

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                TraineeMyPageEffect.NavigateToPrevious -> navigateToPrevious()
                TraineeMyPageEffect.NavigateToConnect -> navigateToConnect()
                TraineeMyPageEffect.NavigateToLogin -> navigateToLogin()
                is TraineeMyPageEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@Composable
private fun TraineeMyPageScreen(
    state: TraineeMyPageUiState,
    onBackClick: () -> Unit,
    onEditButtonClick: () -> Unit,
    onConnectButtonClick: () -> Unit,
    onDisconnectButtonClick: () -> Unit,
    onPushNotificationToggle: () -> Unit,
    onServiceTermClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onOpenSourceClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    onConfirmFirstPopup: () -> Unit,
    onConfirmSecondPopup: () -> Unit,
    onDismissPopup: () -> Unit,
    onDismissWebView: () -> Unit,
) {
    when (state.page) {
        TraineeMyPageContract.TraineeMyPagePage.MyPage -> TraineeMyPageMainPage(
            state = state,
            onBackClick = onBackClick,
            onEditButtonClick = onEditButtonClick,
            onConnectButtonClick = onConnectButtonClick,
            onDisconnectButtonClick = onDisconnectButtonClick,
            onPushNotificationToggle = onPushNotificationToggle,
            onServiceTermClick = onServiceTermClick,
            onPrivacyClick = onPrivacyClick,
            onOpenSourceClick = onOpenSourceClick,
            onLogoutClick = onLogoutClick,
            onDeleteAccountClick = onDeleteAccountClick,
            onConfirmFirstPopup = onConfirmFirstPopup,
            onConfirmSecondPopup = onConfirmSecondPopup,
            onDismissPopup = onDismissPopup,
        )
        TraineeMyPageContract.TraineeMyPagePage.WebView -> TraineeMyPageWebViewPage(
            url = state.url,
            onBackPress = onDismissWebView,
        )
    }
}
