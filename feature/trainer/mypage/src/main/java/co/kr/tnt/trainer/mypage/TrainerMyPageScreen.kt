package co.kr.tnt.trainer.mypage

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import co.kr.tnt.core.ui.R.string.core_app_push_notification
import co.kr.tnt.core.ui.R.string.core_app_version
import co.kr.tnt.core.ui.R.string.core_cancel
import co.kr.tnt.core.ui.R.string.core_delete_account
import co.kr.tnt.core.ui.R.string.core_delete_account_complete_content
import co.kr.tnt.core.ui.R.string.core_delete_account_complete_title
import co.kr.tnt.core.ui.R.string.core_delete_account_title
import co.kr.tnt.core.ui.R.string.core_logout
import co.kr.tnt.core.ui.R.string.core_logout_complete_title
import co.kr.tnt.core.ui.R.string.core_logout_content
import co.kr.tnt.core.ui.R.string.core_logout_title
import co.kr.tnt.core.ui.R.string.core_modifying_personal_info
import co.kr.tnt.core.ui.R.string.core_ok
import co.kr.tnt.core.ui.R.string.core_open_source_license
import co.kr.tnt.core.ui.R.string.core_privacy_policy
import co.kr.tnt.core.ui.R.string.core_terms_of_service
import co.kr.tnt.designsystem.component.TnTIconPopupDialog
import co.kr.tnt.designsystem.component.TnTProfileImage
import co.kr.tnt.designsystem.component.TnTSingleButtonPopupDialog
import co.kr.tnt.designsystem.component.TnTSwitch
import co.kr.tnt.designsystem.component.button.TnTTextButton
import co.kr.tnt.designsystem.component.button.model.ButtonSize
import co.kr.tnt.designsystem.component.button.model.ButtonType
import co.kr.tnt.designsystem.snackbar.LocalSnackbar
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.domain.model.User
import co.kr.tnt.domain.model.trainer.TrainerManagementMemberCount
import co.kr.tnt.feature.trainer.mypage.R
import co.kr.tnt.trainer.mypage.TrainerMyPageContract.TrainerMyPageSideEffect
import co.kr.tnt.trainer.mypage.TrainerMyPageContract.TrainerMyPageUiEvent
import co.kr.tnt.trainer.mypage.TrainerMyPageContract.TrainerMyPageUiState
import co.kr.tnt.trainer.mypage.TrainerMyPageContract.TrainerMyPageUiState.DialogState
import co.kr.tnt.ui.component.TnTLoadingScreen
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
import kotlinx.coroutines.flow.collectLatest
import co.kr.tnt.core.designsystem.R as designSystemR

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun TrainerMyPageRoute(
    padding: PaddingValues,
    navigateToLogin: () -> Unit,
    navigateToModifyMyInfo: () -> Unit,
    navigateToWebView: (String) -> Unit,
    viewModel: TrainerMyPageViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val toast = LocalSnackbar.current
    val context = LocalContext.current
    val permissionState = rememberMultiplePermissionsState(TnTPermission.NOTIFICATION.values)

    TrainerMyPageScreen(
        state = state,
        padding = padding,
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
        onClickModifyMyInfo = { viewModel.setEvent(TrainerMyPageUiEvent.OnClickModifyMyInfo) },
        onClickLogout = { viewModel.setEvent(TrainerMyPageUiEvent.OnClickLogout) },
        onClickDeleteAccount = { viewModel.setEvent(TrainerMyPageUiEvent.OnClickDeleteAccount) },
    )

    Dialog(
        dialogState = state.dialogState,
        onClickConfirm = { viewModel.setEvent(TrainerMyPageUiEvent.OnClickDialogConfirm) },
        onDismissDialog = { viewModel.setEvent(TrainerMyPageUiEvent.OnDismissDialog) },
    )

    if (state.isLoading) {
        TnTLoadingScreen()
    }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                TrainerMyPageSideEffect.NavigateToLogin -> navigateToLogin()
                TrainerMyPageSideEffect.NavigateToModifyMyInfo -> navigateToModifyMyInfo()
                is TrainerMyPageSideEffect.NavigateToWebView -> navigateToWebView(effect.url)
                is TrainerMyPageSideEffect.ShowToast -> toast.show(effect.message.asString(context))

                is TrainerMyPageSideEffect.RequestPermission -> {
                    if (effect.isExplicitlyDenied) {
                        context.moveToAppSetting()
                        return@collectLatest
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
    padding: PaddingValues,
    appVersion: String,
    onTogglePushNotification: () -> Unit,
    onClickTermsOfService: () -> Unit,
    onClickPrivacy: () -> Unit,
    onClickOpenSourceLicense: () -> Unit,
    onClickModifyMyInfo: () -> Unit,
    onClickLogout: () -> Unit,
    onClickDeleteAccount: () -> Unit,
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(state.user.image)
            .placeholder(DefaultUserProfile.Trainer.image)
            .error(DefaultUserProfile.Trainer.image)
            .build(),
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
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
        Spacer(Modifier.height(8.dp))
        TnTTextButton(
            text = stringResource(core_modifying_personal_info),
            size = ButtonSize.Small,
            type = ButtonType.Gray,
            onClick = onClickModifyMyInfo,
        )
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            ManagementMemberCount(
                title = stringResource(R.string.managing_member),
                count = state.user.memberCounts.activeCount,
            )
            Spacer(modifier = Modifier.width(8.dp))
            ManagementMemberCount(
                title = stringResource(R.string.worked_together_member),
                count = state.user.memberCounts.totalCount,
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
                text = stringResource(core_app_push_notification),
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
                    text = stringResource(core_terms_of_service),
                    onClick = onClickTermsOfService,
                    verticalPadding = 8.dp,
                )
                TnTMyPageButton(
                    text = stringResource(core_privacy_policy),
                    onClick = onClickPrivacy,
                    verticalPadding = 8.dp,
                )
                TnTMyPageButton(
                    text = stringResource(core_app_version),
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
                    text = stringResource(core_open_source_license),
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
                    text = stringResource(core_logout),
                    onClick = onClickLogout,
                    verticalPadding = 8.dp,
                )
                TnTMyPageButton(
                    text = stringResource(core_delete_account),
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
                title = stringResource(core_logout_title),
                content = stringResource(core_logout_content),
                leftButtonText = stringResource(core_cancel),
                rightButtonText = stringResource(core_ok),
                onLeftButtonClick = onDismissDialog,
                onRightButtonClick = onClickConfirm,
                onDismiss = onDismissDialog,
            )
        }

        DialogState.LOGOUT -> {
            TnTSingleButtonPopupDialog(
                title = stringResource(core_logout_complete_title),
                content = stringResource(core_logout_content),
                buttonText = stringResource(core_ok),
                cancelable = false,
                onButtonClick = onClickConfirm,
                onDismiss = onDismissDialog,
            )
        }

        DialogState.DELETE_ACCOUNT_CONFIRM -> {
            TnTIconPopupDialog(
                title = stringResource(core_delete_account_title),
                content = stringResource(R.string.delete_account_content),
                leftButtonText = stringResource(core_cancel),
                rightButtonText = stringResource(core_ok),
                onLeftButtonClick = onDismissDialog,
                onRightButtonClick = onClickConfirm,
                onDismiss = onDismissDialog,
            )
        }

        DialogState.DELETE_ACCOUNT -> {
            TnTSingleButtonPopupDialog(
                title = stringResource(core_delete_account_complete_title),
                content = stringResource(core_delete_account_complete_content),
                buttonText = stringResource(core_ok),
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
                    memberCounts = TrainerManagementMemberCount(
                        activeCount = 23,
                        totalCount = 26,
                    ),
                ),
                isEnablePushNotification = false,
            ),
            padding = PaddingValues(),
            appVersion = "1.0.0",
            onTogglePushNotification = { },
            onClickTermsOfService = { },
            onClickPrivacy = { },
            onClickOpenSourceLicense = { },
            onClickLogout = { },
            onClickDeleteAccount = { },
            onClickModifyMyInfo = { },
        )
    }
}
