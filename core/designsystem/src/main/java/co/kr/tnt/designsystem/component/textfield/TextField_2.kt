package co.kr.tnt.designsystem.component.textfield

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import co.kr.tnt.designsystem.component.textfield.model.TnTTextFieldSize
import co.kr.tnt.designsystem.component.textfield.model.TnTTextFieldType
import co.kr.tnt.designsystem.theme.TnTTheme

@Composable
fun TnTTextField2(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    size: TnTTextFieldSize = TnTTextFieldSize.SMALL,
    isWarning: Boolean = false,
    isEnable: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    focusRequester: FocusRequester? = null,
    requestFocusOnStart: Boolean = false,
    onClick: (() -> Unit)? = null,
    trailing: @Composable (RowScope.() -> Unit)? = null,
) {
    val focusManager = LocalFocusManager.current
    var hasFocus by remember { mutableStateOf(false) }
    val actualFocusRequester = focusRequester ?: remember { FocusRequester() }

    val type = TnTTextFieldType.from(
        isWarning = isWarning,
        hasFocus = hasFocus,
    )
    val shape = RoundedCornerShape(8.dp)

    var textFieldValueState by remember {
        mutableStateOf(
            TextFieldValue(
                text = value,
                selection = TextRange(value.length),
            ),
        )
    }

    LaunchedEffect(value) {
        if (textFieldValueState.text != value) {
            textFieldValueState = TextFieldValue(
                text = value,
                selection = TextRange(value.length),
            )
        }
    }

    LaunchedEffect(requestFocusOnStart) {
        if (requestFocusOnStart) {
            actualFocusRequester.requestFocus()
        }
    }

    BasicTextField(
        value = textFieldValueState,
        onValueChange = { newValue ->
            textFieldValueState = newValue
            onValueChange(newValue.text)
        },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = size.minHeight)
            .clip(shape)
            .border(
                width = type.borderWidth,
                color = type.borderColor,
                shape = shape,
            )
            .clickable(
                enabled = onClick != null,
                onClick = { onClick?.invoke() },
            )
            .focusRequester(actualFocusRequester)
            .onFocusChanged { focusState ->
                if (focusState.isFocused && !hasFocus) {
                    // 포커스 획득 시 커서 우측 종단 이동 처리
                    textFieldValueState = textFieldValueState.copy(
                        selection = TextRange(textFieldValueState.text.length),
                    )
                }
                hasFocus = focusState.isFocused
            },
        enabled = isEnable,
        textStyle = TnTTheme.typography.body1Medium.copy(
            color = TnTTheme.colors.neutralColors.Neutral800,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() },
        ),
        singleLine = size == TnTTextFieldSize.SMALL,
        cursorBrush = SolidColor(TnTTheme.colors.neutralColors.Neutral900),
        decorationBox = { innerTextField ->
            Row {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            vertical = 12.dp,
                            horizontal = 16.dp,
                        ),
                ) {
                    if (textFieldValueState.text.isEmpty() && placeholder.isNotEmpty()) {
                        Text(
                            text = placeholder,
                            style = TnTTheme.typography.body1Medium,
                            color = TnTTheme.colors.neutralColors.Neutral400,
                        )
                    }
                    innerTextField()
                }
                trailing?.invoke(this@Row)
            }
        },
    )
}

private data class TextFieldPreviewState(
    val name: String,
    val value: String,
    val isWarning: Boolean = false,
    val isEnable: Boolean = true,
    val size: TnTTextFieldSize = TnTTextFieldSize.SMALL,
    val requestFocus: Boolean = false,
)

private class TextFieldStateProvider : PreviewParameterProvider<TextFieldPreviewState> {
    override val values = sequenceOf(
        TextFieldPreviewState(
            name = "Empty",
            value = "",
        ),
        TextFieldPreviewState(
            name = "Default",
            value = "입력된 텍스트",
        ),
        TextFieldPreviewState(
            name = "Focused",
            value = "포커스된 상태",
            requestFocus = true,
        ),
        TextFieldPreviewState(
            name = "Warning",
            value = "잘못된 입력",
            isWarning = true,
        ),
        TextFieldPreviewState(
            name = "Disabled",
            value = "비활성화된 필드",
            isEnable = false,
        ),
        TextFieldPreviewState(
            name = "Large size - Default",
            value = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit,
                sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
                Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi,
                ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit.
            """.trimIndent(),
            size = TnTTextFieldSize.LARGE,
        ),
        TextFieldPreviewState(
            name = "Large size - Error",
            value = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            size = TnTTextFieldSize.LARGE,
            isWarning = true,
        ),
        TextFieldPreviewState(
            name = "Large size - Focused",
            value = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            size = TnTTextFieldSize.LARGE,
            requestFocus = true,
        ),
    )
}

@Preview(name = "TextField", showBackground = true)
@Composable
private fun TnTTextField2Preview(
    @PreviewParameter(TextFieldStateProvider::class) state: TextFieldPreviewState,
) {
    TnTTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = state.name,
                style = TnTTheme.typography.body2Medium,
                color = TnTTheme.colors.neutralColors.Neutral600,
            )
            TnTTextField2(
                value = state.value,
                onValueChange = {},
                size = state.size,
                isWarning = state.isWarning,
                isEnable = state.isEnable,
                requestFocusOnStart = state.requestFocus,
            )
        }
    }
}
