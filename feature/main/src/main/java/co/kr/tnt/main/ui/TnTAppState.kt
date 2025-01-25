package co.kr.tnt.main.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import co.kr.tnt.navigation.Route

@Composable
fun rememberTnTAppState(
    navController: NavHostController = rememberNavController(),
): TnTAppState {
    return remember { TnTAppState(navController) }
}

@Stable
@Suppress("UnusedPrivateProperty")
class TnTAppState(
    val navController: NavHostController,
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val startDestination = Route.Login
}
