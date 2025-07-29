package co.kr.tnt.trainee.signup

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.designsystem.component.TnTLabeledTextFieldWithCounter
import co.kr.tnt.designsystem.component.TnTProfileImage
import co.kr.tnt.designsystem.component.TnTTopBarWithBackButton
import co.kr.tnt.designsystem.component.button.TnTBottomButton
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.domain.UserProfilePolicy
import co.kr.tnt.feature.trainee.signup.R
import co.kr.tnt.trainee.signup.TraineeSignUpContract.TraineeSignUpUiState
import co.kr.tnt.trainee.signup.component.ProgressSteps
import co.kr.tnt.ui.coil.ResizeTransformation
import co.kr.tnt.ui.extensions.clearFocusOnTap
import co.kr.tnt.ui.model.DefaultUserProfile
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import co.kr.tnt.core.ui.R as coreR

@Composable
internal fun TraineeProfileSetupPage(
    state: TraineeSignUpUiState,
    onProfileImageSelect: (Uri) -> Unit,
    onChangeName: (String) -> Unit,
    onClickBack: () -> Unit,
    onClickNext: () -> Unit,
) {
    BackHandler { onClickBack() }

    val context = LocalContext.current
    val pickMediaLauncher = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            onProfileImageSelect(uri)
        }
    }
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(state.image)
            .transformations(ResizeTransformation(UserProfilePolicy.USER_IMAGE_MAX_SIZE))
            .build(),
    )

    Scaffold(
        topBar = { TnTTopBarWithBackButton(onBackClick = onClickBack) },
        containerColor = TnTTheme.colors.commonColors.Common0,
        modifier = Modifier.clearFocusOnTap(),
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(innerPadding)
                    .imePadding()
                    .verticalScroll(rememberScrollState()),
            ) {
                ProgressSteps(
                    currentStep = 1,
                    totalSteps = 4,
                    title = stringResource(R.string.what_is_your_name),
                )
                Spacer(Modifier.padding(top = 48.dp))
                TnTProfileImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    defaultImage = painterResource(DefaultUserProfile.Trainee.image),
                    image = state.image?.let { painter },
                    onEditClick = {
                        pickMediaLauncher.launch(
                            PickVisualMediaRequest(
                                mediaType = PickVisualMedia.ImageOnly,
                            ),
                        )
                    },
                )
                Spacer(Modifier.padding(top = 60.dp))
                TnTLabeledTextFieldWithCounter(
                    title = stringResource(coreR.string.name),
                    value = state.name,
                    onValueChange = { newValue ->
                        onChangeName(newValue)
                    },
                    modifier = Modifier.padding(horizontal = 20.dp),
                    placeholder = stringResource(R.string.enter_your_name),
                    maxLength = UserProfilePolicy.USER_NAME_MAX_LENGTH,
                    isSingleLine = true,
                    showWarning = !state.isNameValid,
                    isRequired = true,
                    warningMessage = stringResource(
                        coreR.string.text_length_and_format_warning,
                        UserProfilePolicy.USER_NAME_MAX_LENGTH,
                    ),
                )
            }
            TnTBottomButton(
                text = stringResource(coreR.string.next),
                enabled = state.name.isNotBlank() && state.isNameValid,
                onClick = onClickNext,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TraineeProfileSetupPagePreview() {
    TnTTheme {
        TraineeProfileSetupPage(
            state = TraineeSignUpUiState(),
            onClickBack = {},
            onClickNext = {},
            onProfileImageSelect = {},
            onChangeName = {},
        )
    }
}
