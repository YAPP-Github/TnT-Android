package co.kr.tnt.signup.trainer

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.component.TnTLabeledTextField
import co.kr.tnt.designsystem.component.TnTTopBar
import co.kr.tnt.designsystem.component.button.TnTBottomButton
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.signup.common.component.ProfileImageSection
import co.kr.tnt.ui.extensions.moveToAppSetting
import co.kr.tnt.ui.permission.PermissionRequestDialog
import co.kr.tnt.ui.permission.TnTPermission
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TrainerProfileSetupScreen() {
    val context = LocalContext.current

    // TODO 상태 관리 따로 빼기
    val maxLength = 15
    var text by remember { mutableStateOf("") }
    val isWarning by remember { derivedStateOf { text.length > maxLength } }

    var showPermissionRequestDialog by rememberSaveable { mutableStateOf(false) }
    val mediaPermissions = rememberMultiplePermissionsState(TnTPermission.MEDIA_ACCESS.values)

    Scaffold(
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
                // TODO 버튼 클릭 시 트레이너/트레이니 화면으로 이동
                Text(
                    text = stringResource(R.string.signup_set_name_title),
                    modifier = Modifier.padding(start = 24.dp),
                    color = TnTTheme.colors.neutralColors.Neutral950,
                    style = TnTTheme.typography.h2,
                )
                Spacer(Modifier.padding(top = 48.dp))
                ProfileImageSection(
                    modifier = Modifier.fillMaxWidth(),
                    defaultImage = R.drawable.img_default_profile_trainer,
                    onImageSelected = onImageSelected@{
                        if (TnTPermission.MEDIA_ACCESS.isAllGranted(mediaPermissions)) {
                            // TODO 이미지 피커 이동
                            return@onImageSelected
                        }

                        showPermissionRequestDialog = true
                    },
                )
                Spacer(Modifier.padding(top = 60.dp))
                TnTLabeledTextField(
                    title = stringResource(R.string.name),
                    value = text,
                    onValueChange = { newValue ->
                        val filteredText = validateInput(newValue)
                        text = filteredText
                    },
                    modifier = Modifier.padding(horizontal = 20.dp),
                    placeholder = stringResource(R.string.signup_set_name_placeholder),
                    maxLength = maxLength,
                    isSingleLine = true,
                    showWarning = isWarning,
                    isRequired = true,
                    warningMessage = "$maxLength" + stringResource(R.string.signup_warning_text_length),
                )
            }
            // TODO 트레이너 프로필 생성 완료 화면으로 이동
            TnTBottomButton(
                text = stringResource(R.string.next),
                modifier = Modifier.align(Alignment.BottomCenter),
                enabled = text.isNotBlank() && !isWarning,
                onClick = { },
            )
        }

        if (showPermissionRequestDialog) {
            PermissionRequestDialog(
                permission = TnTPermission.MEDIA_ACCESS,
                isPermanentlyDenied = mediaPermissions.shouldShowRationale,
                onOkButtonClick = onOkButtonClick@{ isPermanentlyDenied ->
                    showPermissionRequestDialog = false

                    if (isPermanentlyDenied.not()) {
                        mediaPermissions.launchMultiplePermissionRequest()
                        return@onOkButtonClick
                    }

                    context.moveToAppSetting()
                },
                onDismissRequest = {
                    showPermissionRequestDialog = false
                },
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
private fun TrainerProfileSetupScreenPreview() {
    TnTTheme {
        TrainerProfileSetupScreen()
    }
}
