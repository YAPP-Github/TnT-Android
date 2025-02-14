package co.kr.tnt.ui.utils

import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
inline fun throttled(
    throttleTime: Long = 1500L,
    crossinline onClick: () -> Unit,
): () -> Unit {
    var lastTimeClicked by remember { mutableLongStateOf(0L) }
    return {
        val now = SystemClock.uptimeMillis()
        if (now - lastTimeClicked >= throttleTime) {
            lastTimeClicked = now
            onClick()
        }
    }
}
