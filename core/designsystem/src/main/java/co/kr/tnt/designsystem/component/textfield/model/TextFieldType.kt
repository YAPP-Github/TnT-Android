package co.kr.tnt.designsystem.component.textfield.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.kr.tnt.designsystem.theme.TnTTheme

internal enum class TnTTextFieldType {
    DEF,
    FOCUS,
    ERROR,
    ;

    val borderColor: Color
        @Composable
        get() = when (this) {
            DEF -> TnTTheme.colors.neutralColors.Neutral300
            FOCUS -> TnTTheme.colors.neutralColors.Neutral900
            ERROR -> TnTTheme.colors.redColors.Red500
        }

    val borderWidth: Dp
        get() = when (this) {
            DEF -> 1.dp
            FOCUS -> 2.dp
            ERROR -> 2.dp
        }

    internal companion object {
        fun from(
            isWarning: Boolean,
            hasFocus: Boolean,
        ): TnTTextFieldType = when {
            isWarning -> ERROR
            hasFocus -> FOCUS
            else -> DEF
        }
    }
}
