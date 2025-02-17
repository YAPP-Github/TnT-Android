package co.kr.tnt.designsystem.utils

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.addFocusCleaner(
    focusManager: FocusManager,
    onFocusCleared: () -> Unit = {},
): Modifier {
    return this.pointerInput(Unit) {
        detectTapGestures(
            onTap = {
                onFocusCleared()
                focusManager.clearFocus()
            },
        )
    }
}
