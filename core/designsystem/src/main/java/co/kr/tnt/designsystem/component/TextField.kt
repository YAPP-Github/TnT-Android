package co.kr.tnt.designsystem.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.component.button.TnTTextButton
import co.kr.tnt.designsystem.component.button.model.ButtonSize
import co.kr.tnt.designsystem.theme.TnTTheme

@Composable
fun TnTTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    isSingleLine: Boolean = false,
    showWarning: Boolean = false,
    warningMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    trailingComponent: @Composable BoxScope.() -> Unit = {},
) {
    var isFocused by remember { mutableStateOf(false) }

    val lineColor = when {
        showWarning -> TnTTheme.colors.mainColors.Red500
        isFocused -> TnTTheme.colors.neutralColors.Neutral600
        else -> TnTTheme.colors.neutralColors.Neutral200
    }

    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = isSingleLine,
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
                    if (value.isEmpty() && placeholder != null) {
                        Text(
                            text = placeholder,
                            style = TnTTheme.typography.body1Medium,
                            color = TnTTheme.colors.neutralColors.Neutral400,
                        )
                    }
                    innerTextField()
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
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

        if (showWarning && !warningMessage.isNullOrEmpty()) {
            Text(
                text = warningMessage,
                style = TnTTheme.typography.body2Medium,
                color = TnTTheme.colors.mainColors.Red500,
                modifier = Modifier.padding(top = 6.dp),
            )
        }
    }
}

@Composable
fun TnTLabeledTextField(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    maxLength: Int = 15,
    isSingleLine: Boolean = false,
    showWarning: Boolean = false,
    showCounter: Boolean = true,
    isRequired: Boolean = false,
    warningMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    trailingComponent: @Composable BoxScope.() -> Unit = {},
) {
    val counterColor = when (showWarning) {
        true -> TnTTheme.colors.mainColors.Red500
        false -> TnTTheme.colors.neutralColors.Neutral400
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(bottom = 8.dp, end = 4.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = title,
                style = TnTTheme.typography.body1Bold,
                color = TnTTheme.colors.neutralColors.Neutral900,
            )
            if (isRequired) {
                Text(
                    text = "*",
                    style = TnTTheme.typography.body1Bold,
                    color = TnTTheme.colors.mainColors.Red500,
                )
            }
            Spacer(Modifier.weight(1f))

            if (showCounter) {
                Text(
                    text = stringResource(R.string.text_counter, value.length, maxLength),
                    style = TnTTheme.typography.label1Medium,
                    color = counterColor,
                )
            }
        }

        TnTTextField(
            value = value,
            placeholder = placeholder,
            onValueChange = onValueChange,
            isSingleLine = isSingleLine,
            showWarning = showWarning,
            warningMessage = warningMessage,
            keyboardType = keyboardType,
            trailingComponent = trailingComponent,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun TnTOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    maxLength: Int = 15,
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    var isFocused by remember { mutableStateOf(false) }

    val borderWidth = if (isError || isFocused) 2.dp else 1.dp

    val borderColor = when {
        isError -> TnTTheme.colors.mainColors.Red500
        isFocused -> TnTTheme.colors.neutralColors.Neutral900
        else -> TnTTheme.colors.neutralColors.Neutral300
    }

    val counterColor = when (isError) {
        true -> TnTTheme.colors.mainColors.Red500
        false -> TnTTheme.colors.neutralColors.Neutral300
    }

    Column(modifier = modifier.fillMaxWidth()) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                }
                .border(
                    width = borderWidth,
                    color = borderColor,
                    shape = RoundedCornerShape(8.dp),
                )
                .defaultMinSize(minHeight = 128.dp),
            cursorBrush = SolidColor(TnTTheme.colors.neutralColors.Neutral900),
            textStyle = TnTTheme.typography.body1Medium.copy(
                color = TnTTheme.colors.neutralColors.Neutral800,
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                ) {
                    if (value.isEmpty() && placeholder != null) {
                        Text(
                            text = placeholder,
                            style = TnTTheme.typography.body1Medium,
                            color = TnTTheme.colors.neutralColors.Neutral400,
                        )
                    }
                    innerTextField()
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
            ),
        )
        Text(
            text = "${value.length}/$maxLength",
            style = TnTTheme.typography.label2Medium,
            color = counterColor,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .align(Alignment.End),
        )
    }
}

@Preview(showBackground = true, heightDp = 100)
@Composable
private fun TnTTextFieldPreview() {
    TnTTheme {
        val maxLength = 15
        var text by remember { mutableStateOf("") }
        var warningState by remember { mutableStateOf(false) }

        warningState = text.length > maxLength

        TnTTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = "내용을 입력해주세요",
            showWarning = warningState,
            isSingleLine = true,
            warningMessage = "${maxLength}자 이내로 입력해주세요",
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            trailingComponent = {
                TnTTextButton(
                    text = "텍스트",
                    size = ButtonSize.Small,
                    onClick = { },
                )
            },
        )
    }
}

@Preview(showBackground = true, heightDp = 120)
@Composable
private fun TnTLabeledTextFieldPreview() {
    TnTTheme {
        val maxLength = 15
        var text by remember { mutableStateOf("") }
        var warningState by remember { mutableStateOf(false) }

        warningState = text.length > maxLength

        TnTLabeledTextField(
            title = "제목",
            value = text,
            onValueChange = { text = it },
            placeholder = "내용을 입력해주세요",
            maxLength = maxLength,
            showWarning = warningState,
            isSingleLine = true,
            isRequired = false,
            warningMessage = "${maxLength}자 이내로 입력해주세요",
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            trailingComponent = {
                TnTTextButton(
                    text = "텍스트",
                    size = ButtonSize.Small,
                    onClick = { },
                )
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TnTOutlinedTextFieldPreview() {
    TnTTheme {
        val maxLength = 100
        var text by remember { mutableStateOf("") }
        var warningState by remember { mutableStateOf(false) }

        warningState = text.length > maxLength

        TnTOutlinedTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = "내용을 입력해주세요",
            maxLength = maxLength,
            isError = warningState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
        )
    }
}
