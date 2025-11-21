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
import co.kr.tnt.core.ui.R.string.core_cancel
import co.kr.tnt.core.ui.R.string.core_connect
import co.kr.tnt.core.ui.R.string.core_next
import co.kr.tnt.core.ui.R.string.core_ok
import co.kr.tnt.core.ui.R.string.core_skip
import co.kr.tnt.designsystem.component.TnTIconPopupDialog
import co.kr.tnt.designsystem.component.TnTTopBar
import co.kr.tnt.designsystem.component.TnTTopBarWithBackButton
import co.kr.tnt.designsystem.component.button.TnTBottomButton
import co.kr.tnt.designsystem.component.textfield.TnTLabeledTextFieldWithTextButton
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.feature.trainee.connect.R
import co.kr.tnt.navigation.model.ScreenMode
import co.kr.tnt.trainee.connect.model.InputState
import co.kr.tnt.ui.extensions.clearFocusOnTap
import co.kr.tnt.core.designsystem.R as uiResource

@Composable
internal fun CodeEntryPage(
    showDialog: Boolean,
    inviteCode: String,
    inputState: InputState,
    screenMode: ScreenMode,
    onClickSkip: () -> Unit,
    onClickBack: () -> Unit,
    onClickNext: () -> Unit,
    onChangeInviteCode: (code: String) -> Unit,
    onClickValidate: (code: String) -> Unit,
    onClickCancel: () -> Unit,
    onDismissDialog: () -> Unit,
) {
    BackHandler {
        when (screenMode) {
            ScreenMode.BACK -> onClickBack()
            ScreenMode.SKIP -> onClickSkip()
            ScreenMode.CLOSE -> onClickBack()
        }
    }

    Scaffold(
        topBar = {
            when (screenMode) {
                ScreenMode.BACK -> {
                    TnTTopBarWithBackButton(
                        title = stringResource(core_connect),
                        onBackClick = onClickBack,
                    )
                }

                ScreenMode.SKIP -> {
                    TnTTopBar(
                        title = stringResource(core_connect),
                        trailingComponent = {
                            Text(
                                text = stringResource(core_skip),
                                color = TnTTheme.colors.neutralColors.Neutral400,
                                style = TnTTheme.typography.body2Medium,
                                modifier = Modifier.clickable { onClickSkip() },
                            )
                        },
                    )
                }

                ScreenMode.CLOSE -> {
                    TnTTopBar(
                        title = stringResource(core_connect),
                        trailingComponent = {
                            IconButton(
                                onClick = onClickBack,
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
        modifier = Modifier.clearFocusOnTap(),
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
                Column {
                    TnTLabeledTextFieldWithTextButton(
                        value = inviteCode,
                        onValueChange = onChangeInviteCode,
                        trailingButtonTitle = stringResource(R.string.verification),
                        onClickTrailingButton = { onClickValidate(inviteCode) },
                        modifier = Modifier
                            .padding(horizontal = 20.dp),
                        title = stringResource(R.string.my_invite_code),
                        showRequiredTitleBadge = true,
                        placeholder = stringResource(R.string.enter_the_code),
                        isWarning = inputState == InputState.INVALID,
                        warningMessage = if (inputState == InputState.INVALID) {
                            stringResource(R.string.verification_fail)
                        } else {
                            null
                        },
                        trailingButtonEnabled = inviteCode.isNotBlank(),
                    )
                    if (inputState == InputState.VALID) {
                        Text(
                            text = stringResource(R.string.verification_success),
                            style = TnTTheme.typography.body2Medium,
                            color = TnTTheme.colors.blueColors.Blue500,
                            modifier = Modifier.padding(
                                start = 20.dp,
                                top = 6.dp,
                            ),
                        )
                    }
                }
            }
            TnTBottomButton(
                text = stringResource(core_next),
                enabled = inputState.isValid,
                onClick = onClickNext,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }

    if (showDialog) {
        TnTIconPopupDialog(
            title = stringResource(R.string.stop_connecting_trainer),
            content = stringResource(R.string.warning_reconnect_needed),
            leftButtonText = stringResource(core_cancel),
            rightButtonText = stringResource(core_ok),
            onLeftButtonClick = onClickCancel,
            onRightButtonClick = onDismissDialog,
            onDismiss = onDismissDialog,
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
            onClickSkip = { },
            onClickNext = { },
            onClickBack = { },
            onClickValidate = { },
            onChangeInviteCode = { },
            onDismissDialog = { },
            onClickCancel = { },
        )
    }
}
