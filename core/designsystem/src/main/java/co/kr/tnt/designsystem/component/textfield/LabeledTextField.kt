package co.kr.tnt.designsystem.component.textfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.component.button.TnTTextButton
import co.kr.tnt.designsystem.component.button.model.ButtonSize
import co.kr.tnt.designsystem.component.button.model.ButtonType
import co.kr.tnt.designsystem.component.textfield.model.TnTTextFieldSize
import co.kr.tnt.designsystem.theme.TnTTheme

@Composable
fun TnTLabeledTextField2(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    showRequiredTitleBadge: Boolean = false,
    placeholder: String = "",
    size: TnTTextFieldSize = TnTTextFieldSize.SMALL,
    isWarning: Boolean = false,
    isEnable: Boolean = true,
    warningMessage: String? = null,
    maxLength: Int? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    focusRequester: FocusRequester? = null,
    requestFocusOnStart: Boolean = false,
    onClickTextField: (() -> Unit)? = null,
    trailing: @Composable (RowScope.() -> Unit)? = null,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (title != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    style = TnTTheme.typography.body1Bold,
                    color = TnTTheme.colors.neutralColors.Neutral900,
                )
                if (showRequiredTitleBadge) {
                    Text(
                        text = "*",
                        style = TnTTheme.typography.body1Bold,
                        color = TnTTheme.colors.redColors.Red500,
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        TnTTextField2(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = placeholder,
            size = size,
            isWarning = isWarning,
            isEnable = isEnable,
            keyboardType = keyboardType,
            focusRequester = focusRequester,
            requestFocusOnStart = requestFocusOnStart,
            trailing = trailing,
            onClick = onClickTextField,
        )
        if ((isWarning && warningMessage != null) || maxLength != null) {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (isWarning && warningMessage != null) {
                    Text(
                        text = warningMessage,
                        style = TnTTheme.typography.body2Medium,
                        color = TnTTheme.colors.redColors.Red500,
                        modifier = Modifier.padding(top = 6.dp),
                    )
                }
                if (maxLength != null) {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = stringResource(R.string.text_counter, value.length, maxLength),
                        style = TnTTheme.typography.label1Medium,
                        color = when {
                            isWarning -> TnTTheme.colors.redColors.Red500
                            else -> TnTTheme.colors.neutralColors.Neutral400
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun TnTLabeledTextFieldWithTextButton(
    value: String,
    onValueChange: (String) -> Unit,
    trailingButtonTitle: String,
    onClickTrailingButton: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    showRequiredTitleBadge: Boolean = false,
    placeholder: String = "",
    size: TnTTextFieldSize = TnTTextFieldSize.SMALL,
    isWarning: Boolean = false,
    isEnable: Boolean = true,
    warningMessage: String? = null,
    maxLength: Int? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    focusRequester: FocusRequester? = null,
    requestFocusOnStart: Boolean = false,
    trailingButtonSize: ButtonSize = ButtonSize.Small,
    trailingButtonType: ButtonType = ButtonType.Primary,
    trailingButtonEnabled: Boolean = true,
    onClickTextField: (() -> Unit)? = null,
) {
    TnTLabeledTextField2(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        title = title,
        showRequiredTitleBadge = showRequiredTitleBadge,
        placeholder = placeholder,
        size = size,
        isWarning = isWarning,
        isEnable = isEnable,
        warningMessage = warningMessage,
        maxLength = maxLength,
        trailing = {
            TnTTextButton(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .heightIn(max = trailingButtonSize.height)
                    .padding(end = 8.dp),
                text = trailingButtonTitle,
                size = trailingButtonSize,
                type = trailingButtonType,
                enabled = trailingButtonEnabled,
                onClick = onClickTrailingButton,
            )
        },
        keyboardType = keyboardType,
        focusRequester = focusRequester,
        requestFocusOnStart = requestFocusOnStart,
        onClickTextField = onClickTextField,
    )
}

@Preview(name = "Labeled TextField - Empty", showBackground = true)
@Composable
private fun TnTLabeledTextField2EmptyPreview() {
    TnTTheme {
        TnTLabeledTextField2(
            title = "이름",
            value = "",
            onValueChange = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(name = "Labeled TextField - With text", showBackground = true)
@Composable
private fun TnTLabeledTextField2WithTextPreview() {
    TnTTheme {
        TnTLabeledTextField2(
            title = "이름",
            value = "홍길동",
            onValueChange = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(name = "Labeled TextField - Required", showBackground = true)
@Composable
private fun TnTLabeledTextField2RequiredPreview() {
    TnTTheme {
        TnTLabeledTextField2(
            title = "이름",
            value = "",
            onValueChange = {},
            showRequiredTitleBadge = true,
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(name = "Labeled TextField - Error", showBackground = true)
@Composable
private fun TnTLabeledTextField2ErrorPreview() {
    TnTTheme {
        TnTLabeledTextField2(
            title = "이름",
            value = "잘못된 입력",
            onValueChange = {},
            isWarning = true,
            showRequiredTitleBadge = true,
            warningMessage = "올바른 이름을 입력해주세요",
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(name = "Labeled TextField - Large size", showBackground = true)
@Composable
private fun TnTLabeledTextField2LargePreview() {
    TnTTheme {
        TnTLabeledTextField2(
            title = "메모",
            value = "여러 줄의 텍스트를\n입력할 수 있습니다.",
            onValueChange = {},
            size = TnTTextFieldSize.LARGE,
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(name = "Labeled TextField - With Counter", showBackground = true)
@Composable
private fun TnTLabeledTextField2WithCounterEmptyPreview() {
    TnTTheme {
        var text by remember { mutableStateOf("") }

        TnTLabeledTextField2(
            title = "닉네임",
            value = text,
            onValueChange = { text = it },
            maxLength = 10,
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(name = "Labeled TextField - Counter + Required", showBackground = true)
@Composable
private fun TnTLabeledTextField2WithCounterTextPreview() {
    TnTTheme {
        var text by remember { mutableStateOf("홍길동") }

        TnTLabeledTextField2(
            title = "닉네임",
            value = text,
            onValueChange = { text = it },
            maxLength = 10,
            showRequiredTitleBadge = true,
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(name = "Labeled TextField - Counter + Error", showBackground = true)
@Composable
private fun TnTLabeledTextField2WithCounterErrorPreview() {
    TnTTheme {
        var text by remember { mutableStateOf("매우 긴 닉네임 매우 긴 닉네임") }

        TnTLabeledTextField2(
            title = "닉네임",
            value = text,
            onValueChange = { text = it },
            maxLength = 10,
            isWarning = true,
            showRequiredTitleBadge = true,
            warningMessage = "10자 이내로 입력해주세요",
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(name = "Labeled TextField - With Trailing Button", showBackground = true)
@Composable
private fun TnTLabeledTextField2WithTrailingButtonPreview() {
    TnTTheme {
        TnTLabeledTextFieldWithTextButton(
            title = "닉네임",
            value = "닉네임",
            onValueChange = {},
            trailingButtonTitle = "중복확인",
            onClickTrailingButton = { },
            trailingButtonSize = ButtonSize.Small,
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(name = "All States Overview", showBackground = true, heightDp = 1000)
@Composable
private fun TnTLabeledTextField2AllStatesPreview() {
    TnTTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Text("Basic", style = TnTTheme.typography.body1Bold)
            TnTLabeledTextField2(
                title = "이름",
                value = "홍길동",
                onValueChange = {},
            )

            Text("Required", style = TnTTheme.typography.body1Bold)
            TnTLabeledTextField2(
                title = "이메일",
                value = "",
                onValueChange = {},
                showRequiredTitleBadge = true,
            )

            Text("Error with message", style = TnTTheme.typography.body1Bold)
            TnTLabeledTextField2(
                title = "전화번호",
                value = "잘못된 형식",
                onValueChange = {},
                isWarning = true,
                showRequiredTitleBadge = true,
                warningMessage = "올바른 전화번호를 입력해주세요",
            )

            Text("With Counter", style = TnTTheme.typography.body1Bold)
            TnTLabeledTextField2(
                title = "닉네임",
                value = "홍길동",
                onValueChange = {},
                maxLength = 10,
                showRequiredTitleBadge = true,
            )

            Text("With Trailing Button", style = TnTTheme.typography.body1Bold)
            TnTLabeledTextFieldWithTextButton(
                title = "닉네임",
                value = "안녕하세요. 우리는 YAPP TnT 팀이에요. 반갑습니다.",
                onValueChange = {},
                trailingButtonTitle = "중복확인",
                onClickTrailingButton = {},
                trailingButtonSize = ButtonSize.Small,
            )

            Text("Large size", style = TnTTheme.typography.body1Bold)
            TnTLabeledTextField2(
                title = "메모",
                value = "여러 줄의\n텍스트 입력",
                onValueChange = {},
                size = TnTTextFieldSize.LARGE,
            )
        }
    }
}
