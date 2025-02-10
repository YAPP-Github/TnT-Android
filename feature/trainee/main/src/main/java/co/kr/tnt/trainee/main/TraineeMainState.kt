package co.kr.tnt.trainee.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import co.kr.tnt.navigation.Route

@Composable
internal fun rememberTraineeMainState(
    navController: NavHostController = rememberNavController(),
    startDestination: Route,
) = remember {
    TraineeMainState(
        navController = navController,
        startDestination = startDestination,
    )
}

@Stable
internal class TraineeMainState(
    val navController: NavHostController,
    val startDestination: Route,
) {
    private val previousDestination = mutableStateOf<NavDestination?>(null)

    val mainTabs = TraineeMainTab.entries

    private val currentDestination: NavDestination?
        @Composable get() {
            val currentEntry = navController.currentBackStackEntryFlow
                .collectAsState(initial = null)

            return currentEntry.value?.destination.also { destination ->
                if (destination != null) {
                    previousDestination.value = destination
                }
            } ?: previousDestination.value
        }

    val currentMainTab: TraineeMainTab?
        @Composable get() = mainTabs.find { tab ->
            currentDestination?.hasRoute(tab.route::class) == true
        }

    val shouldShowBottomBar: Boolean
        @Composable get() = mainTabs.any { tab ->
            currentDestination?.hasRoute(tab.route::class) == true
        }

    fun navigateMainTab(tab: TraineeMainTab) {
        navController.navigate(
            route = tab.route,
            navOptions = navOptions {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            },
        )
    }
}
