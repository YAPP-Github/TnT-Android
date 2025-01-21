package co.kr.tnt.signup.trainee

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.component.TnTOutlinedTextField
import co.kr.tnt.designsystem.component.TnTTopBar
import co.kr.tnt.designsystem.component.button.TnTBottomButton
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.signup.trainee.component.ProgressSteps

private const val MAX_LENGTH = 100

@Composable
fun TraineeNoteForTrainerScreen() {
    // TODO 상태 관리 따로 빼기
    var text by remember { mutableStateOf("") }

    Scaffold(
        // TODO 버튼 클릭 시 트레이니 PT 목적 화면으로 이동
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
                    currentStep = 4,
                    totalSteps = 4,
                    title = stringResource(R.string.signup_pt_precaution_title),
                    subTitle = stringResource(R.string.signup_pt_precaution_subtitle),
                )
                Spacer(Modifier.padding(top = 48.dp))
                TnTOutlinedTextField(
                    value = text,
                    onValueChange = { newValue ->
                        if (newValue.length <= MAX_LENGTH) {
                            text = newValue
                        }
                    },
                    modifier = Modifier.padding(horizontal = 20.dp),
                    placeholder = stringResource(R.string.placeholder_content_input),
                    maxLength = 100,
                )
            }
            // TODO 트레이니 PT 목적 화면으로 이동
            TnTBottomButton(
                text = stringResource(R.string.next),
                modifier = Modifier.align(Alignment.BottomCenter),
                enabled = text.isNotBlank(),
                onClick = { },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TraineeNoteForTrainerScreenPreview() {
    TnTTheme {
        TraineeNoteForTrainerScreen()
    }
}
