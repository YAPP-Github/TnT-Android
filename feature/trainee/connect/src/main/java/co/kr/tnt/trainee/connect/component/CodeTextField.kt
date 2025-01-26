package co.kr.tnt.trainee.connect.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.unit.dp
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.feature.trainee.connect.R
import co.kr.tnt.trainee.connect.component.InputState.FOCUS
import co.kr.tnt.trainee.connect.component.InputState.INVALID
import co.kr.tnt.trainee.connect.component.InputState.UNFOCUSED
import co.kr.tnt.trainee.connect.component.InputState.VALID

enum class InputState {
    UNFOCUSED,
    FOCUS,
    VALID,
    INVALID,
}

@Composable
fun CodeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isCodeValid: InputState?,
    trailingComponent: @Composable BoxScope.() -> Unit = {},
) {
    var codeState by remember { mutableStateOf(UNFOCUSED) }

    val color = when {
        isCodeValid == VALID -> TnTTheme.colors.blueColors.Blue500
        isCodeValid == INVALID -> TnTTheme.colors.redColors.Red500
        codeState == FOCUS -> TnTTheme.colors.neutralColors.Neutral600
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
                onValueChange = { onValueChange(it) },
                singleLine = true,
                cursorBrush = SolidColor(TnTTheme.colors.neutralColors.Neutral900),
                textStyle = TnTTheme.typography.body1Medium.copy(
                    color = TnTTheme.colors.neutralColors.Neutral600,
                ),
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged {
                        if (it.isFocused) {
                            codeState = FOCUS
                        } else if (value.isEmpty()) {
                            codeState = UNFOCUSED
                        }
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
            color = color,
        )
        if (isCodeValid == VALID) {
            Text(
                text = stringResource(R.string.verification_success),
                style = TnTTheme.typography.body2Medium,
                color = color,
                modifier = Modifier.padding(top = 6.dp),
            )
        }
        if (isCodeValid == INVALID) {
            Text(
                text = stringResource(R.string.verification_fail),
                style = TnTTheme.typography.body2Medium,
                color = color,
                modifier = Modifier.padding(top = 6.dp),
            )
        }
    }
}
