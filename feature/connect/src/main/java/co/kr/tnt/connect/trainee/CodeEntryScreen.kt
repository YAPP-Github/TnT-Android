package co.kr.tnt.connect.trainee

import TraineeConnectContract.TraineeConnectUiState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.designsystem.component.TnTPopupDialog
import co.kr.tnt.designsystem.component.TnTTopBar
import co.kr.tnt.designsystem.component.button.TnTBottomButton
import co.kr.tnt.designsystem.component.button.TnTTextButton
import co.kr.tnt.designsystem.component.button.model.ButtonSize
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.feature.connect.R

@Composable
internal fun CodeEntryScreen(
    state: TraineeConnectUiState,
    onSkipClick: () -> Unit,
    onNextClick: () -> Unit,
    onCodeChanged: () -> Unit,
    onValidateClick: (String) -> Unit,
) {
    var code by remember { mutableStateOf("") }
    var showDialog by rememberSaveable { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TnTTopBar(
                title = stringResource(R.string.connect),
                trailingComponent = {
                    Text(
                        text = stringResource(R.string.skip),
                        color = TnTTheme.colors.neutralColors.Neutral400,
                        style = TnTTheme.typography.body2Medium,
                        modifier = Modifier.clickable {
                            onSkipClick()
                        },
                    )
                },
            )
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
                    value = code,
                    onValueChange = {
                        code = it
                        onCodeChanged()
                    },
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verificationState = state.isCodeValid,
                    trailingComponent = {
                        TnTTextButton(
                            text = stringResource(R.string.verification),
                            size = ButtonSize.Small,
                            onClick = { onValidateClick(code) },
                        )
                    },
                )
            }
            TnTBottomButton(
                text = stringResource(R.string.next),
                enabled = state.isCodeValid == true,
                onClick = onNextClick,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
        if (showDialog) {
            TnTPopupDialog(
                title = stringResource(R.string.enter_invite_code_from_trainer),
                content = stringResource(R.string.not_connected_warning),
                leftButtonText = stringResource(R.string.next_time),
                rightButtonText = stringResource(R.string.confirm),
                onLeftButtonClick = {
                    showDialog = false
                    onSkipClick()
                },
                onRightButtonClick = { showDialog = false },
                onDismiss = { showDialog = false },
            )
        }
    }
}

@Composable
private fun CodeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    verificationState: Boolean? = null,
    trailingComponent: @Composable BoxScope.() -> Unit = {},
) {
    var isFocused by remember { mutableStateOf(false) }

    val lineColor = when {
        verificationState == true -> TnTTheme.colors.blueColors.Blue500
        verificationState == false -> TnTTheme.colors.redColors.Red500
        isFocused -> TnTTheme.colors.neutralColors.Neutral600
        else -> TnTTheme.colors.neutralColors.Neutral200
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row {
            Text(
                text = stringResource(R.string.my_invite_code),
                style = TnTTheme.typography.body1Bold,
                color = TnTTheme.colors.neutralColors.Neutral900,
            )
            Text(
                text = "*",
                style = TnTTheme.typography.body1Bold,
                color = TnTTheme.colors.redColors.Red500,
            )
        }
        Spacer(Modifier.padding(top = 8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                cursorBrush = SolidColor(TnTTheme.colors.neutralColors.Neutral900),
                textStyle = TnTTheme.typography.body1Medium.copy(
                    color = TnTTheme.colors.neutralColors.Neutral600,
                ),
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    }
                    .padding(8.dp),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = stringResource(R.string.enter_the_code),
                            style = TnTTheme.typography.body1Medium,
                            color = TnTTheme.colors.neutralColors.Neutral400,
                        )
                    }
                    innerTextField()
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                ),
            )
            Box(
                modifier = Modifier
                    .wrapContentSize(Alignment.Center)
                    .align(Alignment.CenterVertically)
                    .padding(vertical = 4.dp),
                content = trailingComponent,
            )
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = lineColor,
        )
        if (verificationState == true) {
            Text(
                text = stringResource(R.string.verification_success),
                style = TnTTheme.typography.body2Medium,
                color = TnTTheme.colors.blueColors.Blue500,
                modifier = Modifier.padding(top = 6.dp),
            )
        }
        if (verificationState == false) {
            Text(
                text = stringResource(R.string.verification_fail),
                style = TnTTheme.typography.body2Medium,
                color = TnTTheme.colors.redColors.Red500,
                modifier = Modifier.padding(top = 6.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CodeEntryScreenPreview() {
    TnTTheme {
        CodeEntryScreen(
            onSkipClick = {},
            onNextClick = {},
            state = TODO(),
            onValidateClick = {},
            onCodeChanged = TODO(),
        )
    }
}
