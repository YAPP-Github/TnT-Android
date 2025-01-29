package co.kr.tnt.designsystem.component.chip.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import co.kr.tnt.designsystem.theme.TnTTheme

enum class ChipStyle(
    val backgroundColor: @Composable () -> Color,
    val textColor: @Composable () -> Color,
) {
    BLUE(
        backgroundColor = { TnTTheme.colors.blueColors.Blue100 },
        textColor = { TnTTheme.colors.blueColors.Blue800 },
    ),
    PINK(
        backgroundColor = { TnTTheme.colors.pinkColors.Pink100 },
        textColor = { TnTTheme.colors.pinkColors.Pink800 },
    ),
}
