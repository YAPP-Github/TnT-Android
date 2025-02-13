package co.kr.tnt.main.ui

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import co.kr.tnt.designsystem.snackbar.LocalSnackbar
import co.kr.tnt.designsystem.snackbar.TnTSnackbarLayout
import co.kr.tnt.designsystem.snackbar.rememberSnackbarState
import kotlinx.coroutines.flow.collectLatest

@Composable
fun TnTApp(
    appState: TnTAppState,
) {
    val context = LocalContext.current
    val toastState = rememberSnackbarState()

    LaunchedEffect(appState.sessionMonitor.onExpired) {
        appState.sessionMonitor.onExpired.collectLatest {
            // TODO navigate to login dialog
            Toast.makeText(context, "세션이 만료되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    CompositionLocalProvider(LocalSnackbar provides toastState) {
        TnTSnackbarLayout(
            snackbarState = toastState,
            content = {
                TnTNavHost(
                    appState = appState,
                )
            },
        )
    }
}
