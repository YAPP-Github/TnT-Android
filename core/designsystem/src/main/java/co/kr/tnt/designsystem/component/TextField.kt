package co.kr.tnt.designsystem.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.component.button.TnTTextButton
import co.kr.tnt.designsystem.component.button.model.ButtonSize
import co.kr.tnt.designsystem.theme.TnTTheme
import java.time.LocalDate

@Composable
fun TnTTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = stringResource(R.string.placeholder_content_input),
    enabled: Boolean = true,
    isSingleLine: Boolean = false,
    showWarning: Boolean = false,
    warningMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    trailingComponent: @Composable BoxScope.() -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var isFocused by remember { mutableStateOf(false) }

    val lineColor = when {
        showWarning -> TnTTheme.colors.redColors.Red500
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
                enabled = enabled,
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
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        isFocused = false
                    },
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
                color = TnTTheme.colors.redColors.Red500,
                modifier = Modifier.padding(top = 6.dp),
            )
        }
    }
}

@Composable
fun TnTLabeledTextFieldWithCounter(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = stringResource(R.string.placeholder_content_input),
    maxLength: Int = 15,
    isSingleLine: Boolean = false,
    showWarning: Boolean = false,
    isRequired: Boolean = false,
    warningMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    trailingComponent: @Composable BoxScope.() -> Unit = {},
) {
    val counterColor = when (showWarning) {
        true -> TnTTheme.colors.redColors.Red500
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
                    color = TnTTheme.colors.redColors.Red500,
                )
            }
            Spacer(Modifier.weight(1f))

            Text(
                text = stringResource(R.string.text_counter, value.length, maxLength),
                style = TnTTheme.typography.label1Medium,
                color = counterColor,
            )
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
fun TnTLabeledTextField(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = stringResource(R.string.placeholder_content_input),
    enabled: Boolean = true,
    isSingleLine: Boolean = false,
    showWarning: Boolean = false,
    isRequired: Boolean = false,
    warningMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    trailingComponent: @Composable BoxScope.() -> Unit = {},
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
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
                    color = TnTTheme.colors.redColors.Red500,
                )
            }
        }

        TnTTextField(
            value = value,
            placeholder = placeholder,
            onValueChange = onValueChange,
            isSingleLine = isSingleLine,
            showWarning = showWarning,
            enabled = enabled,
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
    placeholder: String? = stringResource(R.string.placeholder_content_input),
    maxLength: Int = 15,
    enabled: Boolean = true,
    minHeight: Dp = 128.dp,
    isError: Boolean = false,
    warningMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var isFocused by remember { mutableStateOf(false) }

    val borderWidth = if (isError || isFocused) 2.dp else 1.dp

    val borderColor = when {
        isError -> TnTTheme.colors.redColors.Red500
        isFocused -> TnTTheme.colors.neutralColors.Neutral900
        else -> TnTTheme.colors.neutralColors.Neutral300
    }

    val counterColor = when (isError) {
        true -> TnTTheme.colors.redColors.Red500
        false -> TnTTheme.colors.neutralColors.Neutral300
    }

    Column(modifier = modifier.fillMaxWidth()) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
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
                .defaultMinSize(minHeight = minHeight),
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
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    isFocused = false
                },
            ),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp),
        ) {
            if (isError && warningMessage.isNullOrEmpty().not()) {
                Text(
                    text = warningMessage ?: "",
                    style = TnTTheme.typography.body2Medium,
                    color = counterColor,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${value.length}/$maxLength",
                style = TnTTheme.typography.label2Medium,
                color = counterColor,
            )
        }
    }
}

@Composable
fun TnTSelectableTextField(
    title: String,
    value: String?,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    isReadOnly: Boolean = true,
    isSingleLine: Boolean = false,
    isRequired: Boolean = false,
    showWarning: Boolean = false,
    warningMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    shouldClearFocus: Boolean = false,
    onClick: () -> Unit,
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    val lineColor = when {
        showWarning -> TnTTheme.colors.redColors.Red500
        isFocused -> TnTTheme.colors.neutralColors.Neutral600
        else -> TnTTheme.colors.neutralColors.Neutral200
    }
    val iconColor = when {
        isFocused -> TnTTheme.colors.neutralColors.Neutral600
        else -> TnTTheme.colors.neutralColors.Neutral400
    }

    LaunchedEffect(shouldClearFocus) {
        if (shouldClearFocus) {
            focusManager.clearFocus()
            isFocused = false
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
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
                    color = TnTTheme.colors.redColors.Red500,
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = {
                        isFocused = true
                        onClick()
                    },
                ),
        ) {
            BasicTextField(
                value = value ?: "",
                onValueChange = onValueChange,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                        if (focusState.isFocused) {
                            onClick()
                        }
                    },
                readOnly = isReadOnly,
                textStyle = TnTTheme.typography.body1Medium.copy(
                    color = if (value?.isNotEmpty() == true) {
                        TnTTheme.colors.neutralColors.Neutral900
                    } else {
                        TnTTheme.colors.neutralColors.Neutral400
                    },
                ),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                cursorBrush = SolidColor(Color.Transparent),
                singleLine = isSingleLine,
                decorationBox = { innerTextField ->
                    if (value?.isEmpty() == true && placeholder != null) {
                        Text(
                            text = placeholder,
                            style = TnTTheme.typography.body1Medium,
                            color = TnTTheme.colors.neutralColors.Neutral400,
                        )
                    }
                    innerTextField()
                },
            )
            Box(
                modifier = Modifier
                    .wrapContentSize(Alignment.Center)
                    .align(Alignment.CenterVertically)
                    .padding(vertical = 4.dp),
                content = {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_down),
                        tint = iconColor,
                        contentDescription = null,
                    )
                },
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
                color = TnTTheme.colors.redColors.Red500,
                modifier = Modifier.padding(top = 6.dp),
            )
        }
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

@Preview(showBackground = true, heightDp = 120)
@Composable
private fun TnTLabeledTextFieldWithCounterPreview() {
    TnTTheme {
        val maxLength = 15
        var text by remember { mutableStateOf("") }
        var warningState by remember { mutableStateOf(false) }

        warningState = text.length > maxLength

        TnTLabeledTextFieldWithCounter(
            title = "제목",
            value = text,
            onValueChange = { text = it },
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
            maxLength = maxLength,
            isError = warningState,
            warningMessage = "에러",
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TnTClickableTextFieldPreview() {
    var date by remember { mutableStateOf("") }
    var shouldClearFocus by remember { mutableStateOf(false) }

    fun updateValue() {
        date = LocalDate.of(2025, 3, 3).toString()
        shouldClearFocus = true
    }

    fun onClickComponent() {
        shouldClearFocus = false
        updateValue()
    }

    TnTTheme {
        TnTSelectableTextField(
            title = "제목",
            value = date,
            placeholder = "2025-01-01",
            isRequired = true,
            isSingleLine = true,
            onClick = { onClickComponent() },
            onValueChange = { },
            shouldClearFocus = shouldClearFocus,
            modifier = Modifier.padding(10.dp),
        )
    }
}
