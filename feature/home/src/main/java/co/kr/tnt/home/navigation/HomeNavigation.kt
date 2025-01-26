package co.kr.tnt.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navOptions
import androidx.navigation.toRoute
import co.kr.tnt.home.HomeRoute
import co.kr.tnt.navigation.Route

fun NavController.navigateToHome(
    isTrainer: Boolean,
    clearBackStack: Boolean = false,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.Home(isTrainer),
    navOptions = navOptions {
        if (clearBackStack) {
            popUpTo(graph.startDestinationId) { inclusive = true }
        }
        navOptions()
    },
)

fun NavGraphBuilder.homeNavGraph(
    homeDestination: NavGraphBuilder.() -> Unit = { },
) {
    navigation<Route.HomeBase>(startDestination = Route.Home(false)) {
        composable<Route.Home> { backstackEntry ->
            backstackEntry.toRoute<Route.Home>().apply {
                HomeRoute(
                    isTrainer = isTrainer,
                )
            }
            homeDestination()
        }
    }
}
