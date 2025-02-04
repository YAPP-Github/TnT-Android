package co.kr.tnt.trainee.connect

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.designsystem.component.TnTTopBar
import co.kr.tnt.designsystem.component.TnTTopBarWithBackButton
import co.kr.tnt.designsystem.component.button.TnTBottomButton
import co.kr.tnt.designsystem.component.button.TnTTextButton
import co.kr.tnt.designsystem.component.button.model.ButtonSize
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.feature.trainee.connect.R
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectUiState
import co.kr.tnt.trainee.connect.component.CodeTextField
import co.kr.tnt.trainee.connect.model.InputState.VALID
import co.kr.tnt.core.ui.R as uiResource

@Composable
internal fun CodeEntryPage(
    state: TraineeConnectUiState,
    isSkippable: Boolean,
    onSkipClick: () -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    onCodeChanged: (String) -> Unit,
    onValidateClick: (String) -> Unit,
) {
    BackHandler {
        if (isSkippable) {
            onSkipClick()
        } else {
            onBackClick()
        }
    }

    Scaffold(
        topBar = {
            if (isSkippable) {
                TnTTopBar(
                    title = stringResource(uiResource.string.connect),
                    trailingComponent = {
                        Text(
                            text = stringResource(uiResource.string.skip),
                            color = TnTTheme.colors.neutralColors.Neutral400,
                            style = TnTTheme.typography.body2Medium,
                            modifier = Modifier.clickable {
                                onSkipClick()
                            },
                        )
                    },
                )
            } else {
                TnTTopBarWithBackButton(
                    title = stringResource(uiResource.string.connect),
                    onBackClick = onBackClick,
                )
            }
        },
        containerColor = TnTTheme.colors.commonColors.Common0,
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(Modifier.padding(top = 24.dp))
                Text(
                    text = stringResource(R.string.enter_invite_code_from_trainer),
                    color = TnTTheme.colors.neutralColors.Neutral950,
                    style = TnTTheme.typography.h2,
                    modifier = Modifier.padding(horizontal = 24.dp),
                )
                Spacer(Modifier.padding(top = 48.dp))
                CodeTextField(
                    value = state.inviteCode,
                    onValueChange = { onCodeChanged(it) },
                    modifier = Modifier.padding(horizontal = 20.dp),
                    isCodeValid = state.isCodeValid,
                    trailingComponent = {
                        TnTTextButton(
                            text = stringResource(R.string.verification),
                            size = ButtonSize.Small,
                            enabled = state.inviteCode.isNotBlank(),
                            onClick = { onValidateClick(state.inviteCode) },
                        )
                    },
                )
            }
            TnTBottomButton(
                text = stringResource(uiResource.string.next),
                enabled = state.isCodeValid == VALID,
                onClick = onNextClick,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CodeEntryPagePreview() {
    TnTTheme {
        CodeEntryPage(
            isSkippable = false,
            onSkipClick = {},
            onNextClick = {},
            state = TraineeConnectUiState(),
            onValidateClick = {},
            onCodeChanged = {},
            onBackClick = {},
        )
    }
}
