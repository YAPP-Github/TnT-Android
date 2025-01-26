package co.kr.tnt.main.ui

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.collectLatest

@Composable
fun TnTApp(
    appState: TnTAppState,
) {
    val context = LocalContext.current

    LaunchedEffect(appState.sessionMonitor.onExpired) {
        appState.sessionMonitor.onExpired.collectLatest {
            // TODO navigate to login dialog
            Toast.makeText(context, "세션이 만료되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    TnTNavHost(
        appState = appState,
    )
}
