package co.kr.tnt.trainer.modifymyinfo

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.core.ui.R.string.core_cancel
import co.kr.tnt.core.ui.R.string.core_complete
import co.kr.tnt.core.ui.R.string.core_confirm_modify_info_exit
import co.kr.tnt.core.ui.R.string.core_name
import co.kr.tnt.core.ui.R.string.core_name_placeholder
import co.kr.tnt.core.ui.R.string.core_ok
import co.kr.tnt.core.ui.R.string.core_text_length_and_format_warning
import co.kr.tnt.core.ui.R.string.core_unsaved_changes_warning
import co.kr.tnt.designsystem.component.TnTIconPopupDialog
import co.kr.tnt.designsystem.component.TnTLabeledTextFieldWithCounter
import co.kr.tnt.designsystem.component.TnTProfileImage
import co.kr.tnt.designsystem.component.TnTTopBarWithBackButton
import co.kr.tnt.designsystem.component.button.TnTBottomButton
import co.kr.tnt.designsystem.snackbar.LocalSnackbar
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.domain.UserProfilePolicy
import co.kr.tnt.trainer.modifymyinfo.TrainerModifyMyInfoContract.TrainerModifyMyInfoEffect
import co.kr.tnt.trainer.modifymyinfo.TrainerModifyMyInfoContract.TrainerModifyMyInfoUiEvent
import co.kr.tnt.trainer.modifymyinfo.TrainerModifyMyInfoContract.TrainerModifyMyInfoUiState
import co.kr.tnt.trainer.modifymyinfo.TrainerModifyMyInfoContract.TrainerModifyMyInfoUiState.DialogState
import co.kr.tnt.ui.model.DefaultUserProfile
import co.kr.tnt.ui.utils.convertToAllowedImageFormat
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun TrainerModifyMyInfoRoute(
    viewModel: TrainerModifyMyInfoViewModel = hiltViewModel(),
    navigateToPrevious: () -> Unit,
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbar = LocalSnackbar.current

    TrainerModifyMyInfoScreen(
        state = state,
        onClickBack = { viewModel.setEvent(TrainerModifyMyInfoUiEvent.OnClickBack) },
        onNameChange = { name -> viewModel.setEvent(TrainerModifyMyInfoUiEvent.OnNameChange(name)) },
        onClickComplete = { viewModel.setEvent(TrainerModifyMyInfoUiEvent.OnClickComplete) },
        onProfileImageSelect = { uri ->
            val profileImageFile = uri.convertToAllowedImageFormat(context)
            viewModel.setEvent(TrainerModifyMyInfoUiEvent.OnProfileImageSelect(profileImageFile))
        },
    )

    when (state.dialogState) {
        DialogState.NONE -> Unit
        DialogState.CONFIRM_EXIT -> {
            TnTIconPopupDialog(
                title = stringResource(core_confirm_modify_info_exit),
                content = stringResource(core_unsaved_changes_warning),
                leftButtonText = stringResource(core_cancel),
                rightButtonText = stringResource(core_ok),
                onLeftButtonClick = { viewModel.setEvent(TrainerModifyMyInfoUiEvent.OnDismissDialog) },
                onRightButtonClick = { viewModel.setEvent(TrainerModifyMyInfoUiEvent.OnClickDialogConfirm) },
                onDismiss = { viewModel.setEvent(TrainerModifyMyInfoUiEvent.OnDismissDialog) },
            )
        }
    }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                TrainerModifyMyInfoEffect.NavigateToPrevious -> navigateToPrevious()
                is TrainerModifyMyInfoEffect.ShowToast -> snackbar.show(effect.message)
            }
        }
    }
}

@Composable
private fun TrainerModifyMyInfoScreen(
    state: TrainerModifyMyInfoUiState,
    onClickBack: () -> Unit,
    onNameChange: (name: String) -> Unit,
    onClickComplete: () -> Unit,
    onProfileImageSelect: (uri: Uri) -> Unit,
) {
    BackHandler { onClickBack() }

    val pickMediaLauncher = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        uri?.let(onProfileImageSelect)
    }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(state.profileImage)
            .placeholder(DefaultUserProfile.Trainer.image)
            .error(DefaultUserProfile.Trainer.image)
            .build(),
    )

    Scaffold(
        topBar = {
            TnTTopBarWithBackButton(
                title = "내 정보 수정",
                onBackClick = onClickBack,
            )
        },
        containerColor = TnTTheme.colors.commonColors.Common0,
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding),
        ) {
            TnTProfileImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                image = painter,
                defaultImage = painterResource(DefaultUserProfile.Trainer.image),
                imageSize = 132.dp,
                onEditClick = {
                    pickMediaLauncher.launch(
                        PickVisualMediaRequest(
                            mediaType = PickVisualMedia.ImageOnly,
                        ),
                    )
                },
            )
            Spacer(modifier = Modifier.height(48.dp))
            TnTLabeledTextFieldWithCounter(
                title = stringResource(core_name),
                value = state.name,
                onValueChange = { newValue ->
                    onNameChange(newValue)
                },
                modifier = Modifier.padding(horizontal = 20.dp),
                placeholder = stringResource(core_name_placeholder),
                maxLength = UserProfilePolicy.USER_NAME_MAX_LENGTH,
                isSingleLine = true,
                showWarning = state.isValidName.not(),
                isRequired = true,
                warningMessage = stringResource(
                    core_text_length_and_format_warning,
                    UserProfilePolicy.USER_NAME_MAX_LENGTH,
                ),
            )
            Spacer(modifier = Modifier.weight(1f))
            TnTBottomButton(
                text = stringResource(core_complete),
                enabled = state.isEnableComplete,
                onClick = onClickComplete,
            )
        }
    }
}
