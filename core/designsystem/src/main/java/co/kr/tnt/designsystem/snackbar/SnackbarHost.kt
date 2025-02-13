package co.kr.tnt.designsystem.snackbar

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.RecomposeScope
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.AccessibilityManager
import androidx.compose.ui.platform.LocalAccessibilityManager
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.dismiss
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.util.fastForEach
import co.kr.tnt.core.designsystem.R
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.resume

val LocalSnackbar = staticCompositionLocalOf<SnackbarState> {
    error("LocalSnackbar not Implemented")
}

@Composable
fun TnTSnackbarLayout(
    snackbarState: SnackbarState,
    modifier: Modifier = Modifier,
    snackbarHost: @Composable (SnackbarState) -> Unit = { TnTSnackbarHost(it) },
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            content()
        }
        snackbarHost(snackbarState)
    }
}

@Composable
fun TnTSnackbarHost(
    snackbarState: SnackbarState,
) {
    val currentSnackbarData = snackbarState.currentSnackbarData ?: return
    val accessibilityManager = LocalAccessibilityManager.current
    LaunchedEffect(currentSnackbarData) {
        val duration = currentSnackbarData.duration.toMillis(
            currentSnackbarData.icon != null,
            accessibilityManager,
        )

        delay(duration)
        currentSnackbarData.dismiss()
    }
    FadeInFadeOutWithScale(
        current = currentSnackbarData,
        content = { TnTSnackbar(snackbarData = currentSnackbarData) },
    )
}

@Composable
private fun FadeInFadeOutWithScale(
    current: SnackbarData?,
    modifier: Modifier = Modifier,
    content: @Composable (SnackbarData) -> Unit,
) {
    val state = remember { FadeInFadeOutState<SnackbarData?>() }
    if (current != state.current) {
        state.current = current
        val keys = state.items.map { it.key }.toMutableList()
        if (!keys.contains(current)) {
            keys.add(current)
        }
        state.items.clear()
        keys.filterNotNull().mapTo(state.items) { key ->
            FadeInFadeOutAnimationItem(key) { children ->
                val isVisible = key == current
                val duration = if (isVisible) SNACKBAR_FADE_IN_MILLIS else SNACKBAR_FADE_OUT_MILLIS
                val delay = SNACKBAR_FADE_OUT_MILLIS + SNACKBAR_IN_BETWEEN_DELAY_MILLIS
                val animationDelay = if (isVisible && keys.filterNotNull().size != 1) delay else 0
                val opacity = animatedOpacity(
                    animation = tween(
                        easing = LinearEasing,
                        delayMillis = animationDelay,
                        durationMillis = duration,
                    ),
                    visible = isVisible,
                    onAnimationFinish = {
                        if (key != state.current) {
                            // leave only the current in the list
                            state.items.removeAll { it.key == key }
                            state.scope?.invalidate()
                        }
                    },
                )
                val scale = animatedScale(
                    animation = tween(
                        easing = FastOutSlowInEasing,
                        delayMillis = animationDelay,
                        durationMillis = duration,
                    ),
                    visible = isVisible,
                )
                Box(
                    Modifier
                        .graphicsLayer(
                            scaleX = scale.value,
                            scaleY = scale.value,
                            alpha = opacity.value,
                        )
                        .semantics {
                            liveRegion = LiveRegionMode.Polite
                            dismiss {
                                key.dismiss()
                                true
                            }
                        },
                ) {
                    children()
                }
            }
        }
    }
    Box(modifier) {
        state.scope = currentRecomposeScope
        state.items.fastForEach { (item, opacity) ->
            key(item) {
                opacity {
                    item?.let {
                        content(it)
                    }
                }
            }
        }
    }
}

interface SnackbarData {
    val message: String

    @get:DrawableRes
    val icon: Int?
    val duration: Long

    fun dismiss()
}

@Stable
class SnackbarState {
    private val mutex = Mutex()

    var currentSnackbarData by mutableStateOf<SnackbarData?>(null)
        private set

    suspend fun show(
        message: String,
        @DrawableRes icon: Int? = R.drawable.ic_warning,
        duration: Long = 2000L,
    ): SnackbarResult = mutex.withLock {
        try {
            return suspendCancellableCoroutine { continuation ->
                currentSnackbarData = SnackbarDataImpl(
                    message = message,
                    icon = icon,
                    duration = duration,
                    continuation = continuation,
                )
            }
        } finally {
            currentSnackbarData = null
        }
    }

    @Stable
    private class SnackbarDataImpl(
        override val message: String,
        override val icon: Int?,
        override val duration: Long,
        private val continuation: CancellableContinuation<SnackbarResult>,
    ) : SnackbarData {
        override fun dismiss() {
            if (continuation.isActive) continuation.resume(SnackbarResult.DISMISSED)
        }
    }
}

enum class SnackbarResult {
    DISMISSED,
}

@Composable
fun rememberSnackbarState(): SnackbarState {
    val state = SnackbarState()
    return remember(state.currentSnackbarData) {
        SnackbarState()
    }
}

private class FadeInFadeOutState<T> {
    // we use Any here as something which will not be equals to the real initial value
    var current: Any? = Any()
    var items = mutableListOf<FadeInFadeOutAnimationItem<T>>()
    var scope: RecomposeScope? = null
}

private data class FadeInFadeOutAnimationItem<T>(
    val key: T,
    val transition: FadeInFadeOutTransition,
)

private typealias FadeInFadeOutTransition = @Composable (content: @Composable () -> Unit) -> Unit

@Composable
private fun animatedOpacity(
    animation: AnimationSpec<Float>,
    visible: Boolean,
    onAnimationFinish: () -> Unit = {},
): State<Float> {
    val alpha = remember { Animatable(if (!visible) 1f else 0f) }
    LaunchedEffect(visible) {
        alpha.animateTo(
            if (visible) 1f else 0f,
            animationSpec = animation,
        )
        onAnimationFinish()
    }
    return alpha.asState()
}

@Composable
private fun animatedScale(animation: AnimationSpec<Float>, visible: Boolean): State<Float> {
    val scale = remember { Animatable(if (!visible) 1f else 0.8f) }
    LaunchedEffect(visible) {
        scale.animateTo(
            if (visible) 1f else 0.8f,
            animationSpec = animation,
        )
    }
    return scale.asState()
}

private fun Long.toMillis(
    hasAction: Boolean,
    accessibilityManager: AccessibilityManager?,
): Long {
    if (accessibilityManager == null) {
        return this
    }
    return accessibilityManager.calculateRecommendedTimeoutMillis(
        this,
        containsIcons = true,
        containsText = true,
        containsControls = hasAction,
    )
}

private const val SNACKBAR_FADE_IN_MILLIS = 150
private const val SNACKBAR_FADE_OUT_MILLIS = 150
private const val SNACKBAR_IN_BETWEEN_DELAY_MILLIS = 0
