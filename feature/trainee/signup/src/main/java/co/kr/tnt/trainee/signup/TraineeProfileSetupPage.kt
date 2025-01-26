package co.kr.tnt.trainee.signup

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.designsystem.component.TnTLabeledTextFieldWithCounter
import co.kr.tnt.designsystem.component.TnTTopBarWithBackButton
import co.kr.tnt.designsystem.component.button.TnTBottomButton
import co.kr.tnt.designsystem.component.image.TnTProfileImage
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.domain.IMAGE_MAX_SIZE
import co.kr.tnt.feature.trainee.signup.R
import co.kr.tnt.trainee.signup.component.ProgressSteps
import co.kr.tnt.ui.coil.ResizeTransformation
import co.kr.tnt.ui.model.DefaultUserProfile
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import co.kr.tnt.core.ui.R as uiResource

@Composable
fun TraineeProfileSetupPage(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    BackHandler { onBackClick() }

    val context = LocalContext.current

    // TODO 상태 관리 따로 빼기
    val maxLength = 15
    var text by remember { mutableStateOf("") }
    val isWarning by remember { derivedStateOf { text.length > maxLength } }
    var profileImage by remember { mutableStateOf<Uri?>(null) }

    val pickMediaLauncher = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            profileImage = uri
        }
    }
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(profileImage)
            .transformations(ResizeTransformation(IMAGE_MAX_SIZE))
            .build(),
    )

    Scaffold(
        topBar = { TnTTopBarWithBackButton(onBackClick = { onBackClick() }) },
        containerColor = TnTTheme.colors.commonColors.Common0,
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
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
                    image = profileImage?.let { painter },
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
                    title = stringResource(uiResource.string.name),
                    value = text,
                    onValueChange = { newValue ->
                        val filteredText = validateInput(newValue)
                        text = filteredText
                    },
                    modifier = Modifier.padding(horizontal = 20.dp),
                    placeholder = stringResource(R.string.enter_your_name),
                    maxLength = maxLength,
                    isSingleLine = true,
                    showWarning = isWarning,
                    isRequired = true,
                    warningMessage = stringResource(R.string.text_length_warning, maxLength),
                )
            }
            TnTBottomButton(
                text = stringResource(uiResource.string.next),
                enabled = text.isNotBlank() && !isWarning,
                onClick = { onNextClick() },
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

/**
 * 입력 값을 검사해 한글/영어/공백만 허용하고 특수문자는 제거
 */
private fun validateInput(input: String): String {
    return input.filter { it.isLetter() || it.isWhitespace() }
}

@Preview(showBackground = true)
@Composable
private fun TraineeProfileSetupPagePreview() {
    TnTTheme {
        TraineeProfileSetupPage(
            onBackClick = {},
            onNextClick = {},
        )
    }
}
