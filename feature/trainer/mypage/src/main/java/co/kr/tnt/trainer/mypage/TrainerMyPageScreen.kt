package co.kr.tnt.trainer.mypage

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import co.kr.tnt.domain.model.trainer.TrainerManagementMemberCount
import co.kr.tnt.feature.trainer.mypage.R
import co.kr.tnt.trainer.mypage.TrainerMyPageContract.TrainerMyPageSideEffect
import co.kr.tnt.trainer.mypage.TrainerMyPageContract.TrainerMyPageUiEvent
import co.kr.tnt.trainer.mypage.TrainerMyPageContract.TrainerMyPageUiState
import co.kr.tnt.trainer.mypage.TrainerMyPageContract.TrainerMyPageUiState.DialogState
import co.kr.tnt.ui.component.TnTMyPageButton
import co.kr.tnt.ui.extensions.getAppVersion
import co.kr.tnt.ui.extensions.moveToAppSetting
import co.kr.tnt.ui.model.DefaultUserProfile
import co.kr.tnt.ui.permission.PermissionRequestDialog
import co.kr.tnt.ui.permission.TnTPermission
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import co.kr.tnt.core.designsystem.R as designSystemR
import co.kr.tnt.core.ui.R as coreR

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun TrainerMyPageRoute(
    navigateToLogin: () -> Unit,
    navigateToWebView: (String) -> Unit,
    viewModel: TrainerMyPageViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val toast = LocalSnackbar.current
    val context = LocalContext.current
    val permissionState = rememberMultiplePermissionsState(TnTPermission.NOTIFICATION.values)

    TrainerMyPageScreen(
        state = state,
        appVersion = context.getAppVersion(),
        onTogglePushNotification = {
            viewModel.setEvent(
                TrainerMyPageUiEvent.OnTogglePushNotification(
                    isGrantedPermission = TnTPermission.NOTIFICATION.isRequireGranted(permissionState),
                    shouldShowRationale = permissionState.shouldShowRationale,
                ),
            )
        },
        onClickTermsOfService = { viewModel.setEvent(TrainerMyPageUiEvent.OnClickTermsOfService) },
        onClickPrivacy = { viewModel.setEvent(TrainerMyPageUiEvent.OnClickPrivacy) },
        onClickOpenSourceLicense = { viewModel.setEvent(TrainerMyPageUiEvent.OnClickOpenSourceLicense) },
        onClickLogout = { viewModel.setEvent(TrainerMyPageUiEvent.OnClickLogout) },
        onClickDeleteAccount = { viewModel.setEvent(TrainerMyPageUiEvent.OnClickDeleteAccount) },
    )

    Dialog(
        dialogState = state.dialogState,
        onClickConfirm = { viewModel.setEvent(TrainerMyPageUiEvent.OnClickDialogConfirm) },
        onDismissDialog = { viewModel.setEvent(TrainerMyPageUiEvent.OnDismissDialog) },
    )

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                TrainerMyPageSideEffect.NavigateToLogin -> navigateToLogin()
                is TrainerMyPageSideEffect.NavigateToWebView -> navigateToWebView(effect.url)
                is TrainerMyPageSideEffect.ShowToast -> toast.show(effect.message)

                is TrainerMyPageSideEffect.RequestPermission -> {
                    if (effect.isExplicitlyDenied) {
                        context.moveToAppSetting()
                        return@collect
                    }

                    permissionState.launchMultiplePermissionRequest()
                }
                TrainerMyPageSideEffect.NavigateToOpenSourceLicense ->
                    context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
            }
        }
    }
}

@Composable
private fun TrainerMyPageScreen(
    state: TrainerMyPageUiState,
    appVersion: String,
    onTogglePushNotification: () -> Unit,
    onClickTermsOfService: () -> Unit,
    onClickPrivacy: () -> Unit,
    onClickOpenSourceLicense: () -> Unit,
    onClickLogout: () -> Unit,
    onClickDeleteAccount: () -> Unit,
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(state.user.image)
            .placeholder(designSystemR.drawable.img_default)
            .error(DefaultUserProfile.Trainer.image)
            .build(),
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(TnTTheme.colors.neutralColors.Neutral50)
            .verticalScroll(rememberScrollState()),
    ) {
        // TODO 컴포넌트 개선
        TnTProfileImage(
            image = painter,
            defaultImage = painterResource(DefaultUserProfile.Trainer.image),
            imageSize = 132.dp,
            showEditButton = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
        )
        Text(
            text = state.user.name,
            style = TnTTheme.typography.h2,
            color = TnTTheme.colors.neutralColors.Neutral950,
        )
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            ManagementMemberCount(
                title = "관리 중인 회원",
                count = state.managementMemberCount.activeCount,
            )
            Spacer(modifier = Modifier.width(8.dp))
            ManagementMemberCount(
                title = "함께 했던 회원",
                count = state.managementMemberCount.cumulativeCount,
            )
        }
        Spacer(Modifier.height(16.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
        ) {
            TnTMyPageButton(
                text = stringResource(coreR.string.app_push_notification),
                verticalPadding = 12.dp,
                enabled = false,
                onClick = { },
                trailingComponent = {
                    TnTSwitch(
                        checked = state.isEnablePushNotification,
                        onClick = onTogglePushNotification,
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
                    onClick = onTogglePushNotification,
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
                    onClick = onClickOpenSourceLicense,
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
private fun ManagementMemberCount(
    title: String,
    count: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(TnTTheme.colors.neutralColors.Neutral100)
            .padding(
                vertical = 16.dp,
                horizontal = 40.dp,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            title,
            style = TnTTheme.typography.label1Bold,
            color = TnTTheme.colors.neutralColors.Neutral500,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(28.dp),
                painter = painterResource(designSystemR.drawable.ic_bomb),
                contentDescription = null,
                tint = Color.Unspecified,
            )
            Text(
                count.toString(),
                style = TnTTheme.typography.body1Medium,
                color = TnTTheme.colors.neutralColors.Neutral950,
            )
        }
    }
}

@Composable
private fun Dialog(
    dialogState: DialogState,
    onClickConfirm: () -> Unit,
    onDismissDialog: () -> Unit,
) {
    when (dialogState) {
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
private fun TrainerMyPageScreenPreview() {
    TnTTheme {
        TrainerMyPageScreen(
            state = TrainerMyPageUiState(
                user = User.Trainer(
                    id = "1",
                    name = "김헬짱",
                    image = null,
                ),
                isEnablePushNotification = false,
                managementMemberCount = TrainerManagementMemberCount(
                    activeCount = 23,
                    cumulativeCount = 26,
                ),
            ),
            appVersion = "1.0.0",
            onTogglePushNotification = { },
            onClickTermsOfService = { },
            onClickPrivacy = { },
            onClickOpenSourceLicense = { },
            onClickLogout = { },
            onClickDeleteAccount = { },
        )
    }
}
