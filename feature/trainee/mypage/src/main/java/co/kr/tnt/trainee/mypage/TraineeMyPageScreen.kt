package co.kr.tnt.trainee.mypage

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.core.ui.R
import co.kr.tnt.designsystem.component.TnTIconPopupDialog
import co.kr.tnt.designsystem.component.TnTProfileImage
import co.kr.tnt.designsystem.component.TnTSingleButtonPopupDialog
import co.kr.tnt.designsystem.component.TnTSwitch
import co.kr.tnt.designsystem.component.button.TnTTextButton
import co.kr.tnt.designsystem.component.button.model.ButtonSize
import co.kr.tnt.designsystem.component.button.model.ButtonType
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.trainee.mypage.TraineeMyPageContract.TraineeMyPageEffect
import co.kr.tnt.trainee.mypage.TraineeMyPageContract.TraineeMyPageUiEvent
import co.kr.tnt.trainee.mypage.TraineeMyPageContract.TraineeMyPageUiState
import co.kr.tnt.trainee.mypage.model.DialogState
import co.kr.tnt.ui.component.TnTMyPageButton
import co.kr.tnt.ui.model.DefaultUserProfile

@Composable
internal fun TraineeMyPageRoute(
    navigateToPrevious: () -> Unit,
    navigateToConnect: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToWebView: (String) -> Unit,
    viewModel: TraineeMyPageViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TraineeMyPageScreen(
        state = uiState,
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
        onConfirmWarningDialog = { viewModel.setEvent(TraineeMyPageUiEvent.OnConfirmWarningDialog) },
        onConfirmCompleteDialog = { viewModel.setEvent(TraineeMyPageUiEvent.OnConfirmCompleteDialog) },
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

                is TraineeMyPageEffect.NavigateToWebView -> navigateToWebView(effect.url)
            }
        }
    }
}

@Composable
private fun TraineeMyPageScreen(
    state: TraineeMyPageUiState,
    onEditButtonClick: () -> Unit,
    onConnectButtonClick: () -> Unit,
    onDisconnectButtonClick: () -> Unit,
    onPushNotificationToggle: () -> Unit,
    onServiceTermClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onOpenSourceClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    onConfirmWarningDialog: () -> Unit,
    onConfirmCompleteDialog: () -> Unit,
    onDismissPopup: () -> Unit,
) {
    Scaffold(containerColor = TnTTheme.colors.neutralColors.Neutral50) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
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
                text = stringResource(R.string.modifying_personal_info),
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
                        text = stringResource(co.kr.tnt.feature.trainee.mypage.R.string.connect_with_trainer),
                        onClick = onConnectButtonClick,
                        verticalPadding = 14.dp,
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(TnTTheme.colors.commonColors.Common0)
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                ) {
                    Text(
                        text = stringResource(R.string.app_push_notification),
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
                        text = stringResource(R.string.terms_of_service),
                        onClick = onServiceTermClick,
                        verticalPadding = 8.dp,
                    )
                    TnTMyPageButton(
                        text = stringResource(R.string.privacy_policy),
                        onClick = onPrivacyClick,
                        verticalPadding = 8.dp,
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.app_version),
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
                        text = stringResource(R.string.open_source_license),
                        onClick = onOpenSourceClick,
                        verticalPadding = 8.dp,
                    )
                }
                if (state.isConnected) {
                    TnTMyPageButton(
                        text = stringResource(co.kr.tnt.feature.trainee.mypage.R.string.disconnect_with_trainer),
                        onClick = onDisconnectButtonClick,
                        verticalPadding = 14.dp,
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
                        text = stringResource(R.string.logout),
                        color = TnTTheme.colors.neutralColors.Neutral700,
                        style = TnTTheme.typography.body2Medium,
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                            .clickable(onClick = onLogoutClick),
                    )
                    Text(
                        text = stringResource(R.string.delete_account),
                        color = TnTTheme.colors.neutralColors.Neutral700,
                        style = TnTTheme.typography.body2Medium,
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                            .clickable(onClick = onDeleteAccountClick),
                    )
                }
            }
        }
    }

    if (state.showWarningDialog) {
        TnTIconPopupDialog(
            title = if (state.dialogState == DialogState.DISCONNECT) {
                stringResource(state.dialogState.warningDialogTitle, state.trainerName)
            } else {
                stringResource(state.dialogState.warningDialogTitle)
            },
            content = stringResource(state.dialogState.warningDialogContent),
            leftButtonText = stringResource(R.string.cancel),
            rightButtonText = stringResource(R.string.ok),
            onLeftButtonClick = onDismissPopup,
            onRightButtonClick = onConfirmWarningDialog,
            onDismiss = onDismissPopup,
        )
    }

    if (state.showCompleteDialog) {
        TnTSingleButtonPopupDialog(
            title = if (state.dialogState == DialogState.DISCONNECT) {
                stringResource(state.dialogState.completeDialogTitle, state.trainerName)
            } else {
                stringResource(state.dialogState.completeDialogTitle)
            },
            content = stringResource(state.dialogState.completeDialogContent),
            buttonText = stringResource(R.string.ok),
            onButtonClick = onConfirmCompleteDialog,
            onDismiss = onConfirmCompleteDialog,
        )
    }
}

@Preview
@Composable
private fun TraineeMyPageScreenPreview() {
    TnTTheme {
        TraineeMyPageScreen(
            state = TraineeMyPageUiState(
                image = null,
                name = "김회원",
                isConnected = false,
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
            onDismissPopup = {},
            onConfirmWarningDialog = {},
            onConfirmCompleteDialog = {},
        )
    }
}
