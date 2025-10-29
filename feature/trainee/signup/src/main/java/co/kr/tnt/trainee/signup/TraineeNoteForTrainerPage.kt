package co.kr.tnt.trainee.signup

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.core.ui.R.string.core_next
import co.kr.tnt.core.ui.R.string.core_text_length_warning
import co.kr.tnt.designsystem.component.TnTOutlinedTextField
import co.kr.tnt.designsystem.component.TnTTopBarWithBackButton
import co.kr.tnt.designsystem.component.button.TnTBottomButton
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.domain.UserProfilePolicy
import co.kr.tnt.feature.trainee.signup.R
import co.kr.tnt.trainee.signup.TraineeSignUpContract.TraineeSignUpUiState
import co.kr.tnt.trainee.signup.component.ProgressSteps
import co.kr.tnt.ui.extensions.clearFocusOnTap

@Composable
internal fun TraineeNoteForTrainerPage(
    state: TraineeSignUpUiState,
    onChangeCaution: (String) -> Unit,
    onClickBack: () -> Unit,
    onClickNext: () -> Unit,
) {
    BackHandler { onClickBack() }

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
                    currentStep = 4,
                    totalSteps = 4,
                    title = stringResource(R.string.caution_that_trainer_must_know),
                    subTitle = stringResource(R.string.let_the_trainer_know),
                )
                Spacer(Modifier.padding(top = 48.dp))
                TnTOutlinedTextField(
                    value = state.caution ?: "",
                    onValueChange = { newValue ->
                        onChangeCaution(newValue)
                    },
                    modifier = Modifier.padding(horizontal = 20.dp),
                    isError = state.isCautionNoteValid.not(),
                    warningMessage = stringResource(
                        core_text_length_warning,
                        UserProfilePolicy.USER_CAUTION_MAX_LENGTH,
                    ),
                    maxLength = UserProfilePolicy.USER_CAUTION_MAX_LENGTH,
                )
            }
            TnTBottomButton(
                text = stringResource(core_next),
                modifier = Modifier.align(Alignment.BottomCenter),
                enabled = state.isCautionNoteValid,
                onClick = onClickNext,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TraineeNoteForTrainerPagePreview() {
    TnTTheme {
        TraineeNoteForTrainerPage(
            state = TraineeSignUpUiState(),
            onClickBack = {},
            onClickNext = {},
            onChangeCaution = {},
        )
    }
}
