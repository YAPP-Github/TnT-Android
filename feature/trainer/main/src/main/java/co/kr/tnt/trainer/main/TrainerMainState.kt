package co.kr.tnt.trainer.main

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
internal fun rememberTrainerMainState(
    navController: NavHostController = rememberNavController(),
    startDestination: Route,
) = remember {
    TrainerMainState(
        navController = navController,
        startDestination = startDestination,
    )
}

@Stable
internal class TrainerMainState(
    val navController: NavHostController,
    val startDestination: Route,
) {
    private val previousDestination = mutableStateOf<NavDestination?>(null)

    val mainTabs = TrainerMainTab.entries

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

    val currentMainTab: TrainerMainTab?
        @Composable get() = mainTabs.find { tab ->
            currentDestination?.hasRoute(tab.route::class) == true
        }

    val shouldShowBottomBar: Boolean
        @Composable get() = mainTabs.any { tab ->
            currentDestination?.hasRoute(tab.route::class) == true
        }

    fun navigateMainTab(tab: TrainerMainTab) {
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
