package co.kr.tnt.trainee.connect

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.designsystem.component.TnTIconPopupDialog
import co.kr.tnt.designsystem.component.TnTTopBar
import co.kr.tnt.designsystem.component.TnTTopBarWithBackButton
import co.kr.tnt.designsystem.component.button.TnTBottomButton
import co.kr.tnt.designsystem.component.button.TnTTextButton
import co.kr.tnt.designsystem.component.button.model.ButtonSize
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.feature.trainee.connect.R
import co.kr.tnt.navigation.model.ScreenMode
import co.kr.tnt.trainee.connect.component.CodeTextField
import co.kr.tnt.trainee.connect.model.InputState
import co.kr.tnt.core.designsystem.R as uiResource
import co.kr.tnt.core.ui.R as coreR

@Composable
internal fun CodeEntryPage(
    showDialog: Boolean,
    inviteCode: String,
    inputState: InputState,
    screenMode: ScreenMode,
    onSkipClick: () -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    onChangeInviteCode: (code: String) -> Unit,
    onValidateClick: (code: String) -> Unit,
    onCancelClick: () -> Unit,
    onDismissPopup: () -> Unit,
) {
    BackHandler {
        when (screenMode) {
            ScreenMode.BACK -> onBackClick()
            ScreenMode.SKIP -> onSkipClick()
            ScreenMode.CLOSE -> onBackClick()
        }
    }

    Scaffold(
        topBar = {
            when (screenMode) {
                ScreenMode.BACK -> {
                    TnTTopBarWithBackButton(
                        title = stringResource(coreR.string.connect),
                        onBackClick = onBackClick,
                    )
                }
                ScreenMode.SKIP -> {
                    TnTTopBar(
                        title = stringResource(coreR.string.connect),
                        trailingComponent = {
                            Text(
                                text = stringResource(coreR.string.skip),
                                color = TnTTheme.colors.neutralColors.Neutral400,
                                style = TnTTheme.typography.body2Medium,
                                modifier = Modifier.clickable {
                                    onSkipClick()
                                },
                            )
                        },
                    )
                }
                ScreenMode.CLOSE -> {
                    TnTTopBar(
                        title = stringResource(coreR.string.connect),
                        trailingComponent = {
                            IconButton(
                                onClick = onBackClick,
                            ) {
                                Icon(
                                    painter = painterResource(uiResource.drawable.ic_delete),
                                    contentDescription = null,
                                )
                            }
                        },
                    )
                }
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
                    value = inviteCode,
                    onValueChange = onChangeInviteCode,
                    modifier = Modifier.padding(horizontal = 20.dp),
                    isCodeValid = inputState,
                    trailingComponent = {
                        TnTTextButton(
                            text = stringResource(R.string.verification),
                            size = ButtonSize.Small,
                            enabled = inviteCode.isNotBlank(),
                            onClick = { onValidateClick(inviteCode) },
                        )
                    },
                )
            }
            TnTBottomButton(
                text = stringResource(coreR.string.next),
                enabled = inputState.isValid,
                onClick = onNextClick,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }

    if (showDialog) {
        TnTIconPopupDialog(
            title = stringResource(R.string.stop_connecting_trainer),
            content = stringResource(R.string.warning_reconnect_needed),
            leftButtonText = stringResource(coreR.string.cancel),
            rightButtonText = stringResource(coreR.string.ok),
            onLeftButtonClick = onCancelClick,
            onRightButtonClick = onDismissPopup,
            onDismiss = onDismissPopup,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CodeEntryPagePreview() {
    TnTTheme {
        CodeEntryPage(
            showDialog = false,
            inputState = InputState.FOCUS,
            inviteCode = "23A4SDA31",
            screenMode = ScreenMode.CLOSE,
            onSkipClick = {},
            onNextClick = {},
            onValidateClick = {},
            onChangeInviteCode = {},
            onBackClick = {},
            onDismissPopup = {},
            onCancelClick = {},
        )
    }
}
