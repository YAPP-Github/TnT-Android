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
import co.kr.tnt.core.ui.R
import co.kr.tnt.designsystem.component.TnTIconPopupDialog
import co.kr.tnt.designsystem.component.TnTLabeledTextField
import co.kr.tnt.designsystem.component.TnTProfileImage
import co.kr.tnt.designsystem.component.TnTTopBarWithBackButton
import co.kr.tnt.designsystem.component.button.TnTBottomButton
import co.kr.tnt.designsystem.snackbar.LocalSnackbar
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.trainer.modifymyinfo.TrainerModifyMyInfoContract.TrainerModifyMyInfoEffect
import co.kr.tnt.trainer.modifymyinfo.TrainerModifyMyInfoContract.TrainerModifyMyInfoUiEvent
import co.kr.tnt.trainer.modifymyinfo.TrainerModifyMyInfoContract.TrainerModifyMyInfoUiState
import co.kr.tnt.trainer.modifymyinfo.TrainerModifyMyInfoContract.TrainerModifyMyInfoUiState.DialogState
import co.kr.tnt.ui.model.DefaultUserProfile
import co.kr.tnt.ui.utils.convertToAllowedImageFormat
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

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
                title = "정보 수정을 종료할까요?",
                content = "수정 사항이 저장되지 않아요!",
                leftButtonText = stringResource(R.string.cancel),
                rightButtonText = stringResource(R.string.ok),
                onLeftButtonClick = { viewModel.setEvent(TrainerModifyMyInfoUiEvent.OnDismissDialog) },
                onRightButtonClick = { viewModel.setEvent(TrainerModifyMyInfoUiEvent.OnClickDialogConfirm) },
                onDismiss = { viewModel.setEvent(TrainerModifyMyInfoUiEvent.OnDismissDialog) },
            )
        }
    }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
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
            TnTLabeledTextField(
                title = "이름",
                value = state.name,
                isSingleLine = true,
                isRequired = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                onValueChange = onNameChange,
            )
            Spacer(modifier = Modifier.weight(1f))
            TnTBottomButton(
                text = "완료",
                onClick = onClickComplete,
            )
        }
    }
}
