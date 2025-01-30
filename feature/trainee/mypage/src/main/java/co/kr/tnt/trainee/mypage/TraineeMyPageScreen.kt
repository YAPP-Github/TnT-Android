package co.kr.tnt.trainee.mypage

import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.designsystem.component.TnTIconPopupDialog
import co.kr.tnt.designsystem.component.TnTProfileImage
import co.kr.tnt.designsystem.component.TnTSingleButtonPopupDialog
import co.kr.tnt.designsystem.component.TnTSwitch
import co.kr.tnt.designsystem.component.button.TnTTextButton
import co.kr.tnt.designsystem.component.button.model.ButtonSize
import co.kr.tnt.designsystem.component.button.model.ButtonType
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.feature.trainee.mypage.R
import co.kr.tnt.trainee.mypage.TraineeMyPageContract.TraineeMyPageEffect
import co.kr.tnt.trainee.mypage.TraineeMyPageContract.TraineeMyPageUiEvent
import co.kr.tnt.trainee.mypage.TraineeMyPageContract.TraineeMyPageUiState
import co.kr.tnt.trainee.mypage.model.PopupType
import co.kr.tnt.ui.component.TnTMyPageButton
import co.kr.tnt.ui.model.DefaultUserProfile
import co.kr.tnt.core.ui.R as uiResource

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
        onOpenSourceClick = { viewModel.setEvent(TraineeMyPageUiEvent.OnOpenSourceClick) },
        onLogoutClick = { viewModel.setEvent(TraineeMyPageUiEvent.OnLogoutClick) },
        onDeleteAccountClick = { viewModel.setEvent(TraineeMyPageUiEvent.OnDeleteAccountClick) },
        onDismissPopup = { viewModel.setEvent(TraineeMyPageUiEvent.OnDismissPopup) },
        onConfirmFirstPopup = { viewModel.setEvent(TraineeMyPageUiEvent.OnConfirmFirstPopup) },
        onConfirmSecondPopup = { viewModel.setEvent(TraineeMyPageUiEvent.OnConfirmSecondPopup) },
        onDismissWebView = { viewModel.setEvent(TraineeMyPageUiEvent.OnWebViewBackClick) },
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
    if (state.showWebView) {
        WebViewScreen(
            url = state.url,
            onBackPress = onDismissWebView,
        )
    } else {
        BackHandler { onBackClick() }

        Scaffold(containerColor = TnTTheme.colors.neutralColors.Neutral50) { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(innerPadding),
            ) {
                TnTProfileImage(
                    defaultImage = painterResource(DefaultUserProfile.Trainee.image),
                    imageSize = 132.dp,
                    showEditButton = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                )
                Text(
                    text = state.name,
                    color = TnTTheme.colors.neutralColors.Neutral950,
                    style = TnTTheme.typography.h2,
                )
                Spacer(Modifier.height(8.dp))
                TnTTextButton(
                    text = stringResource(uiResource.string.modifying_personal_info),
                    size = ButtonSize.Small,
                    type = ButtonType.Gray,
                    onClick = onEditButtonClick,
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                ) {
                    if (state.isConnected.not()) {
                        TnTMyPageButton(
                            text = stringResource(R.string.connect_with_trainer),
                            height = 47.dp,
                            onClick = onConnectButtonClick,
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(TnTTheme.colors.commonColors.Common0)
                            .padding(horizontal = 20.dp),
                    ) {
                        Text(
                            text = stringResource(uiResource.string.app_push_notification),
                            color = TnTTheme.colors.neutralColors.Neutral700,
                            style = TnTTheme.typography.body2Medium,
                        )
                        TnTSwitch(
                            checked = state.isPushEnabled,
                            onCheckedChange = onPushNotificationToggle,
                        )
                    }
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(TnTTheme.colors.commonColors.Common0)
                            .padding(vertical = 12.dp),
                    ) {
                        TnTMyPageButton(
                            text = stringResource(uiResource.string.terms_of_service),
                            height = 40.dp,
                            onClick = onServiceTermClick,
                        )
                        TnTMyPageButton(
                            text = stringResource(uiResource.string.privacy_policy),
                            height = 40.dp,
                            onClick = onPrivacyClick,
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .padding(horizontal = 20.dp, vertical = 8.dp),
                        ) {
                            Text(
                                text = stringResource(uiResource.string.app_version),
                                color = TnTTheme.colors.neutralColors.Neutral700,
                                style = TnTTheme.typography.body2Medium,
                            )
                            Text(
                                text = state.appVersion,
                                color = TnTTheme.colors.neutralColors.Neutral400,
                                style = TnTTheme.typography.body2Medium,
                            )
                        }
                        TnTMyPageButton(
                            text = stringResource(uiResource.string.open_source_license),
                            height = 40.dp,
                            onClick = onOpenSourceClick,
                        )
                    }
                    if (state.isConnected) {
                        TnTMyPageButton(
                            text = stringResource(R.string.disconnect_with_trainer),
                            height = 47.dp,
                            onClick = onDisconnectButtonClick,
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(TnTTheme.colors.commonColors.Common0)
                            .padding(vertical = 12.dp),
                    ) {
                        Text(
                            text = stringResource(uiResource.string.logout),
                            color = TnTTheme.colors.neutralColors.Neutral700,
                            style = TnTTheme.typography.body2Medium,
                            modifier = Modifier
                                .padding(horizontal = 20.dp, vertical = 12.dp)
                                .clickable(onClick = onLogoutClick),
                        )
                        Text(
                            text = stringResource(uiResource.string.delete_account),
                            color = TnTTheme.colors.neutralColors.Neutral700,
                            style = TnTTheme.typography.body2Medium,
                            modifier = Modifier
                                .padding(horizontal = 20.dp, vertical = 12.dp)
                                .clickable(onClick = onDeleteAccountClick),
                        )
                    }
                }
            }
        }
    }

    if (state.showFirstPopup) {
        TnTIconPopupDialog(
            title = if (state.popupType == PopupType.DISCONNECT) {
                stringResource(state.popupType.firstPopupTitle, state.trainerName)
            } else {
                stringResource(state.popupType.firstPopupTitle)
            },
            content = stringResource(state.popupType.firstPopupContent),
            leftButtonText = stringResource(uiResource.string.cancel),
            rightButtonText = stringResource(uiResource.string.ok),
            onLeftButtonClick = onDismissPopup,
            onRightButtonClick = onConfirmFirstPopup,
            onDismiss = onDismissPopup,
        )
    }

    if (state.showSecondPopup) {
        TnTSingleButtonPopupDialog(
            title = if (state.popupType == PopupType.DISCONNECT) {
                stringResource(state.popupType.secondPopupTitle, state.trainerName)
            } else {
                stringResource(state.popupType.secondPopupTitle)
            },
            content = stringResource(state.popupType.secondPopupContent),
            buttonText = stringResource(uiResource.string.ok),
            onButtonClick = onConfirmSecondPopup,
            onDismiss = onDismissPopup,
        )
    }
}

@Composable
fun WebViewScreen(url: String, onBackPress: () -> Unit) {
    var webView: WebView? = null

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.builtInZoomControls = true
                settings.displayZoomControls = false
                settings.cacheMode = WebSettings.LOAD_NO_CACHE
                webViewClient = WebViewClient()
                loadUrl(url)
                webView = this
            }
        },
        modifier = Modifier.fillMaxSize(),
    )

    BackHandler {
        if (webView?.canGoBack() == true) {
            webView?.goBack()
        } else {
            onBackPress()
        }
    }
}

@Preview
@Composable
private fun TraineeMyPagePreview() {
    TnTTheme {
        TraineeMyPageScreen(
            state = TraineeMyPageUiState(
                image = null,
                name = "김회원",
                isConnected = true,
                isPushEnabled = true,
                appVersion = "0.0.0",
            ),
            onEditButtonClick = {},
            onConnectButtonClick = {},
            onPushNotificationToggle = {},
            onServiceTermClick = {},
            onPrivacyClick = {},
            onOpenSourceClick = {},
            onLogoutClick = {},
            onDeleteAccountClick = {},
            onDisconnectButtonClick = {},
            onBackClick = {},
            onConfirmFirstPopup = {},
            onDismissPopup = {},
            onConfirmSecondPopup = {},
            onDismissWebView = {},
        )
    }
}
