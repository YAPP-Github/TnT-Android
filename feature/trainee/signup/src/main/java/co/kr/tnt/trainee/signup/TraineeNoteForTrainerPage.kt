package co.kr.tnt.trainee.signup

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.designsystem.component.TnTOutlinedTextField
import co.kr.tnt.designsystem.component.TnTTopBarWithBackButton
import co.kr.tnt.designsystem.component.button.TnTBottomButton
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.feature.trainee.signup.R
import co.kr.tnt.trainee.signup.TraineeSignUpContract.TraineeSignUpUiState
import co.kr.tnt.trainee.signup.component.ProgressSteps
import co.kr.tnt.core.ui.R as uiResource

private const val MAX_LENGTH = 100

@Composable
internal fun TraineeNoteForTrainerPage(
    state: TraineeSignUpUiState,
    onCautionChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    BackHandler { onBackClick() }

    Scaffold(
        topBar = { TnTTopBarWithBackButton(onBackClick = onBackClick) },
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
                    title = stringResource(R.string.caution_that_trainer_must_know),
                    subTitle = stringResource(R.string.let_the_trainer_know),
                )
                Spacer(Modifier.padding(top = 48.dp))
                TnTOutlinedTextField(
                    value = state.traineeState.caution.toString(),
                    onValueChange = { newValue ->
                        if (newValue.length <= MAX_LENGTH) {
                            onCautionChange(newValue)
                        }
                    },
                    modifier = Modifier.padding(horizontal = 20.dp),
                    maxLength = 100,
                )
            }
            TnTBottomButton(
                text = stringResource(uiResource.string.next),
                modifier = Modifier.align(Alignment.BottomCenter),
                onClick = onNextClick,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TraineeNoteForTrainerPagePreview() {
    TnTTheme {
        TraineeNoteForTrainerPage(
            onBackClick = {},
            onNextClick = {},
            state = TODO(),
            onCautionChange = {},
        )
    }
}
