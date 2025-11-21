package co.kr.tnt.designsystem.component.textfield.model

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class TnTTextFieldSize {
    SMALL,
    LARGE,
    ;

    val minHeight: Dp
        get() = when (this) {
            SMALL -> Dp.Unspecified
            LARGE -> 128.dp
        }
}
