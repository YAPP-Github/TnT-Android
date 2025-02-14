package co.kr.tnt.trainee.mypage

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import co.kr.tnt.designsystem.component.TnTIconPopupDialog
import co.kr.tnt.designsystem.component.TnTProfileImage
import co.kr.tnt.designsystem.component.TnTSingleButtonPopupDialog
import co.kr.tnt.designsystem.component.TnTSwitch
import co.kr.tnt.designsystem.snackbar.LocalSnackbar
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.domain.model.User
import co.kr.tnt.feature.trainee.mypage.R
import co.kr.tnt.trainee.mypage.TraineeMyPageContract.TraineeMyPageEffect
import co.kr.tnt.trainee.mypage.TraineeMyPageContract.TraineeMyPageUiEvent
import co.kr.tnt.trainee.mypage.TraineeMyPageContract.TraineeMyPageUiState
import co.kr.tnt.trainee.mypage.TraineeMyPageContract.TraineeMyPageUiState.DialogState
import co.kr.tnt.ui.component.TnTMyPageButton
import co.kr.tnt.ui.extensions.getAppVersion
import co.kr.tnt.ui.extensions.moveToAppSetting
import co.kr.tnt.ui.model.DefaultUserProfile
import co.kr.tnt.ui.permission.PermissionRequestDialog
import co.kr.tnt.ui.permission.TnTPermission
import co.kr.tnt.ui.utils.throttled
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import java.time.LocalDate
import co.kr.tnt.core.ui.R as coreR

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun TraineeMyPageRoute(
    padding: PaddingValues,
    navigateToConnect: (Boolean) -> Unit,
    navigateToLogin: () -> Unit,
    navigateToWebView: (url: String) -> Unit,
    viewModel: TraineeMyPageViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val snackbar = LocalSnackbar.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val permissionState = rememberMultiplePermissionsState(TnTPermission.NOTIFICATION.values)

    TraineeMyPageScreen(
        state = uiState,
        padding = padding,
        appVersion = context.getAppVersion(),
        onClickConnect = { viewModel.setEvent(TraineeMyPageUiEvent.OnClickConnect) },
        onTogglePushNotification = {
            viewModel.setEvent(
                TraineeMyPageUiEvent.OnToggleNotification(
                    isGrantedPermission = TnTPermission.NOTIFICATION.isRequireGranted(permissionState),
                    shouldShowRationale = permissionState.shouldShowRationale,
                ),
            )
        },
        onClickTermsOfService = { viewModel.setEvent(TraineeMyPageUiEvent.OnClickTermsOfService) },
        onClickPrivacy = { viewModel.setEvent(TraineeMyPageUiEvent.OnClickPrivacy) },
        onClickOpenSource = { viewModel.setEvent(TraineeMyPageUiEvent.OnClickOpenSource) },
        onClickLogout = { viewModel.setEvent(TraineeMyPageUiEvent.OnClickLogout) },
        onClickDeleteAccount = { viewModel.setEvent(TraineeMyPageUiEvent.OnClickDeleteAccount) },
    )

    Dialog(
        state = uiState,
        onClickConfirm = { viewModel.setEvent(TraineeMyPageUiEvent.OnClickDialogConfirm) },
        onDismissDialog = { viewModel.setEvent(TraineeMyPageUiEvent.OnDismissDialog) },
    )

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                TraineeMyPageEffect.NavigateToConnect -> navigateToConnect(false)
                TraineeMyPageEffect.NavigateToLogin -> navigateToLogin()
                is TraineeMyPageEffect.ShowToast -> snackbar.show(effect.message)
                is TraineeMyPageEffect.NavigateToWebView -> navigateToWebView(effect.url)
                is TraineeMyPageEffect.RequestPermission -> {
                    if (effect.isExplicitlyDenied) {
                        context.moveToAppSetting()
                        return@collect
                    }

                    permissionState.launchMultiplePermissionRequest()
                }

                TraineeMyPageEffect.NavigateToOpenSourceLicense ->
                    context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
            }
        }
    }
}

@Composable
private fun TraineeMyPageScreen(
    state: TraineeMyPageUiState,
    padding: PaddingValues,
    appVersion: String,
    onClickConnect: () -> Unit,
    onTogglePushNotification: () -> Unit,
    onClickTermsOfService: () -> Unit,
    onClickPrivacy: () -> Unit,
    onClickOpenSource: () -> Unit,
    onClickLogout: () -> Unit,
    onClickDeleteAccount: () -> Unit,
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(state.user.image)
            .placeholder(co.kr.tnt.core.designsystem.R.drawable.img_default)
            .error(DefaultUserProfile.Trainer.image)
            .build(),
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .background(TnTTheme.colors.neutralColors.Neutral50)
            .verticalScroll(rememberScrollState()),
    ) {
        TnTProfileImage(
            image = painter,
            defaultImage = painterResource(DefaultUserProfile.Trainee.image),
            imageSize = 132.dp,
            showEditButton = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
        )
        Text(
            text = state.user.name,
            color = TnTTheme.colors.neutralColors.Neutral950,
            style = TnTTheme.typography.h2,
        )
        Spacer(Modifier.height(16.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
        ) {
            if (state.user.isConnected.not()) {
                TnTMyPageButton(
                    text = stringResource(R.string.connect_with_trainer),
                    onClick = onClickConnect,
                    verticalPadding = 14.dp,
                )
            }
            TnTMyPageButton(
                text = stringResource(coreR.string.app_push_notification),
                verticalPadding = 12.dp,
                enabled = false,
                trailingComponent = {
                    TnTSwitch(
                        checked = state.isEnablePushNotification,
                        onClick = throttled(throttleTime = 500L) { onTogglePushNotification() },
                    )
                },
            )
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(TnTTheme.colors.commonColors.Common0)
                    .padding(vertical = 12.dp),
            ) {
                TnTMyPageButton(
                    text = stringResource(coreR.string.terms_of_service),
                    onClick = onClickTermsOfService,
                    verticalPadding = 8.dp,
                )
                TnTMyPageButton(
                    text = stringResource(coreR.string.privacy_policy),
                    onClick = onClickPrivacy,
                    verticalPadding = 8.dp,
                )
                TnTMyPageButton(
                    text = stringResource(coreR.string.app_version),
                    verticalPadding = 12.dp,
                    enabled = false,
                    trailingComponent = {
                        Text(
                            text = appVersion,
                            style = TnTTheme.typography.body2Medium,
                            color = TnTTheme.colors.neutralColors.Neutral400,
                        )
                    },
                )
                TnTMyPageButton(
                    text = stringResource(coreR.string.open_source_license),
                    onClick = onClickOpenSource,
                    verticalPadding = 8.dp,
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(TnTTheme.colors.commonColors.Common0)
                    .padding(vertical = 12.dp),
            ) {
                TnTMyPageButton(
                    text = stringResource(coreR.string.logout),
                    onClick = onClickLogout,
                    verticalPadding = 8.dp,
                )
                TnTMyPageButton(
                    text = stringResource(coreR.string.delete_account),
                    onClick = onClickDeleteAccount,
                    verticalPadding = 8.dp,
                )
            }
        }
    }
}

@Composable
private fun Dialog(
    state: TraineeMyPageUiState,
    onClickConfirm: () -> Unit,
    onDismissDialog: () -> Unit,
) {
    when (state.dialogState) {
        DialogState.NONE -> Unit
        DialogState.LOGOUT_CONFIRM -> {
            TnTIconPopupDialog(
                title = stringResource(coreR.string.logout_title),
                content = stringResource(coreR.string.logout_content),
                leftButtonText = stringResource(coreR.string.cancel),
                rightButtonText = stringResource(coreR.string.ok),
                onLeftButtonClick = onDismissDialog,
                onRightButtonClick = onClickConfirm,
                onDismiss = onDismissDialog,
            )
        }

        DialogState.LOGOUT -> {
            TnTSingleButtonPopupDialog(
                title = stringResource(coreR.string.logout_complete_title),
                content = stringResource(coreR.string.logout_content),
                buttonText = stringResource(coreR.string.ok),
                cancelable = false,
                onButtonClick = onClickConfirm,
                onDismiss = onDismissDialog,
            )
        }

        DialogState.DELETE_ACCOUNT_CONFIRM -> {
            TnTIconPopupDialog(
                title = stringResource(R.string.delete_account_title),
                content = stringResource(R.string.delete_account_content),
                leftButtonText = stringResource(coreR.string.cancel),
                rightButtonText = stringResource(coreR.string.ok),
                onLeftButtonClick = onDismissDialog,
                onRightButtonClick = onClickConfirm,
                onDismiss = onDismissDialog,
            )
        }

        DialogState.DELETE_ACCOUNT -> {
            TnTSingleButtonPopupDialog(
                title = stringResource(R.string.delete_account_complete_title),
                content = stringResource(R.string.delete_account_complete_content),
                buttonText = stringResource(coreR.string.ok),
                cancelable = false,
                onButtonClick = onClickConfirm,
                onDismiss = onDismissDialog,
            )
        }

        DialogState.SHOULD_ALLOW_PERMISSION -> {
            PermissionRequestDialog(
                permission = TnTPermission.NOTIFICATION,
                isPermanentlyDenied = true,
                onClickConfirm = onClickConfirm,
                onDismiss = onDismissDialog,
            )
        }
    }
}

@Preview
@Composable
private fun TraineeMyPageScreenPreview() {
    TnTTheme {
        TraineeMyPageScreen(
            state = TraineeMyPageUiState(
                user = User.Trainee(
                    id = "",
                    name = "김헬스",
                    image = null,
                    birthday = LocalDate.of(2001, 1, 1),
                    weight = 10.0,
                    height = 100,
                    ptPurpose = listOf("체중 감량"),
                    caution = "약해요",
                    isConnected = true,
                ),
                isEnablePushNotification = true,
            ),
            padding = PaddingValues(),
            appVersion = "1.0",
            onClickConnect = { },
            onTogglePushNotification = { },
            onClickTermsOfService = { },
            onClickPrivacy = { },
            onClickOpenSource = { },
            onClickLogout = { },
            onClickDeleteAccount = { },
        )
    }
}
