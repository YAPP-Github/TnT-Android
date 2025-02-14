package co.kr.tnt.main.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import co.kr.tnt.domain.monitor.SessionMonitor
import co.kr.tnt.navigation.Route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun rememberTnTAppState(
    sessionMonitor: SessionMonitor,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    startDestination: Route,
): TnTAppState {
    return remember {
        TnTAppState(
            sessionMonitor,
            navController,
            startDestination,
            coroutineScope,
        )
    }
}

@Stable
@Suppress("UnusedPrivateProperty")
class TnTAppState(
    val sessionMonitor: SessionMonitor,
    val navController: NavHostController,
    val startDestination: Route,
    coroutineScope: CoroutineScope,
) {
    init {
        coroutineScope.launch {
            sessionMonitor.onExpired.collectLatest {
                showSessionExpiredDialog = true
            }
        }
    }

    var showSessionExpiredDialog by mutableStateOf(false)
        private set

    fun dismissSessionDialog() {
        showSessionExpiredDialog = false
    }

    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination
}
