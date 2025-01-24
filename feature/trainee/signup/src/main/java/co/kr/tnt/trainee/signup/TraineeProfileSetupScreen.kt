package co.kr.tnt.trainee.signup

import android.net.Uri
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.designsystem.component.TnTLabeledTextFieldWithCounter
import co.kr.tnt.designsystem.component.TnTTopBar
import co.kr.tnt.designsystem.component.button.TnTBottomButton
import co.kr.tnt.designsystem.component.image.TnTProfileImage
import co.kr.tnt.designsystem.component.image.model.ProfileType
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.feature.trainee.signup.R
import co.kr.tnt.trainee.signup.component.ProgressSteps
import coil.compose.rememberAsyncImagePainter

@Composable
fun TraineeProfileSetupScreen() {
    // TODO 상태 관리 따로 빼기
    val maxLength = 15
    var text by remember { mutableStateOf("") }
    val isWarning by remember { derivedStateOf { text.length > maxLength } }
    var profileImage by remember { mutableStateOf<Uri?>(null) }

    val pickMediaLauncher = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        profileImage = uri
    }
    val painter = rememberAsyncImagePainter(profileImage)

    Scaffold(
        // TODO 버튼 클릭 시 트레이너/트레이니 화면으로 이동
        topBar = { TnTTopBar(onBackClick = {}) },
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
                    modifier = Modifier.fillMaxWidth(),
                    image = profileImage?.let { painter },
                    type = ProfileType.Trainee,
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
                    title = stringResource(R.string.name),
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
            // TODO 트레이니 기본 정보 입력 화면으로 이동
            TnTBottomButton(
                text = stringResource(R.string.next),
                enabled = text.isNotBlank() && !isWarning,
                onClick = { },
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
private fun TraineeProfileSetupScreenPreview() {
    TnTTheme {
        TraineeProfileSetupScreen()
    }
}
