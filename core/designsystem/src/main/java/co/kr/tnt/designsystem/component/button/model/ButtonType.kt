package co.kr.tnt.designsystem.component.button.model

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.kr.tnt.designsystem.theme.TnTTheme

enum class ButtonType(
    val stroke: (Boolean) -> Dp,
    val borderColor: @Composable (Boolean) -> Color,
    val colors: @Composable () -> ButtonColors,
) {
    Primary(
        stroke = { _ -> 0.dp },
        borderColor = { _ -> Color.Transparent },
        colors = {
            ButtonDefaults.buttonColors(
                containerColor = TnTTheme.colors.neutralColors.Neutral900,
                contentColor = TnTTheme.colors.neutralColors.Neutral50,
                disabledContainerColor = TnTTheme.colors.neutralColors.Neutral200,
                disabledContentColor = TnTTheme.colors.neutralColors.Neutral50,
            )
        },
    ),
    Gray(
        stroke = { _ -> 0.dp },
        borderColor = { _ -> Color.Transparent },
        colors = {
            ButtonDefaults.buttonColors(
                containerColor = TnTTheme.colors.neutralColors.Neutral100,
                contentColor = TnTTheme.colors.neutralColors.Neutral500,
                disabledContainerColor = TnTTheme.colors.neutralColors.Neutral200,
                disabledContentColor = TnTTheme.colors.neutralColors.Neutral50,
            )
        },
    ),
    GrayOutline(
        stroke = { _ -> 1.dp },
        borderColor = { _ -> TnTTheme.colors.neutralColors.Neutral300 },
        colors = {
            ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = TnTTheme.colors.neutralColors.Neutral500,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = TnTTheme.colors.neutralColors.Neutral300,
            )
        },
    ),
    RedOutline(
        stroke = { enabled -> if (enabled) 1.5.dp else 1.dp },
        borderColor = { enabled ->
            if (enabled) TnTTheme.colors.redColors.Red400 else TnTTheme.colors.neutralColors.Neutral300
        },
        colors = {
            ButtonDefaults.buttonColors(
                containerColor = TnTTheme.colors.redColors.Red50,
                contentColor = TnTTheme.colors.redColors.Red600,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = TnTTheme.colors.neutralColors.Neutral300,
            )
        },
    ),
}
