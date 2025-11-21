package co.kr.tnt.designsystem.component.textfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.component.button.TnTTextButton
import co.kr.tnt.designsystem.component.button.model.ButtonSize
import co.kr.tnt.designsystem.component.button.model.ButtonType
import co.kr.tnt.designsystem.component.textfield.model.TnTTextFieldSize
import co.kr.tnt.designsystem.theme.TnTTheme

@Composable
fun TnTSelectableTextField(
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    size: TnTTextFieldSize = TnTTextFieldSize.SMALL,
    isWarning: Boolean = false,
    shouldClearFocus: Boolean = false,
    trailing: @Composable (RowScope.() -> Unit)? = null,
) {
    val focusManager = LocalFocusManager.current

    LaunchedEffect(shouldClearFocus) {
        if (shouldClearFocus) {
            focusManager.clearFocus()
        }
    }

    TnTTextField(
        value = value,
        onValueChange = { },
        modifier = modifier,
        placeholder = placeholder,
        size = size,
        isWarning = isWarning,
        isEnable = false,
        trailing = trailing ?: {
            DefaultTrailingIcon(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 8.dp),
            )
        },
        onClick = onClick,
    )
}

@Composable
fun TnTSelectableLabeledTextField(
    value: String,
    onClickTextField: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    showRequiredTitleBadge: Boolean = false,
    placeholder: String = "",
    size: TnTTextFieldSize = TnTTextFieldSize.SMALL,
    isWarning: Boolean = false,
    warningMessage: String? = null,
    trailing: @Composable (RowScope.() -> Unit)? = null,
) {
    TnTLabeledTextField(
        value = value,
        onValueChange = { },
        modifier = modifier,
        onClickTextField = onClickTextField,
        title = title,
        showRequiredTitleBadge = showRequiredTitleBadge,
        placeholder = placeholder,
        size = size,
        isWarning = isWarning,
        isEnable = false,
        warningMessage = warningMessage,
        trailing = trailing ?: {
            DefaultTrailingIcon(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 8.dp),
            )
        },
    )
}

@Composable
fun TnTSelectableLabeledTextFieldWithTextButton(
    value: String,
    onClickTextField: () -> Unit,
    trailingButtonTitle: String,
    onClickTrailingButton: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    showRequiredTitleBadge: Boolean = false,
    placeholder: String = "",
    size: TnTTextFieldSize = TnTTextFieldSize.SMALL,
    isWarning: Boolean = false,
    warningMessage: String? = null,
    trailingButtonSize: ButtonSize = ButtonSize.Small,
    trailingButtonType: ButtonType = ButtonType.Primary,
) {
    TnTSelectableLabeledTextField(
        value = value,
        onClickTextField = onClickTextField,
        modifier = modifier,
        title = title,
        showRequiredTitleBadge = showRequiredTitleBadge,
        placeholder = placeholder,
        size = size,
        isWarning = isWarning,
        warningMessage = warningMessage,
        trailing = {
            TnTTextButton(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .heightIn(max = trailingButtonSize.height)
                    .padding(end = 8.dp),
                text = trailingButtonTitle,
                size = trailingButtonSize,
                type = trailingButtonType,
                onClick = onClickTrailingButton,
            )
        },
    )
}

@Composable
private fun DefaultTrailingIcon(
    modifier: Modifier,
) {
    Icon(
        modifier = modifier,
        painter = painterResource(R.drawable.ic_arrow_down),
        contentDescription = null,
        tint = TnTTheme.colors.neutralColors.Neutral400,
    )
}

@Preview(name = "All States Overview", showBackground = true, heightDp = 800)
@Composable
private fun TnTSelectableTextField2AllStatesPreview() {
    TnTTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Text("Empty", style = TnTTheme.typography.body1Bold)
            TnTSelectableTextField(
                value = "",
                onClick = {},
                placeholder = "선택해주세요",
            )

            Text("Selected", style = TnTTheme.typography.body1Bold)
            TnTSelectableTextField(
                value = "2025/01/15",
                onClick = {},
            )

            Text("Labeled - Required", style = TnTTheme.typography.body1Bold)
            TnTSelectableLabeledTextField(
                value = "",
                onClickTextField = {},
                title = "날짜",
                showRequiredTitleBadge = true,
                placeholder = "2025/01/15",
            )

            Text("Labeled - Selected", style = TnTTheme.typography.body1Bold)
            TnTSelectableLabeledTextField(
                value = "09:00",
                onClickTextField = {},
                title = "시간",
                showRequiredTitleBadge = true,
            )

            Text("Labeled - Warning", style = TnTTheme.typography.body1Bold)
            TnTSelectableLabeledTextField(
                value = "",
                onClickTextField = {},
                title = "식사 유형",
                showRequiredTitleBadge = true,
                placeholder = "선택해주세요",
                isWarning = true,
                warningMessage = "식사 유형을 선택해주세요",
            )
        }
    }
}
