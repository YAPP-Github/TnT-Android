package co.kr.tnt.trainer.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navOptions
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainer.home.TrainerHomeRoute

fun NavController.navigateToTrainerHome(
    clearBackStack: Boolean = false,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.TrainerHome,
    navOptions = navOptions {
        if (clearBackStack) {
            popUpTo(graph.startDestinationId) { inclusive = true }
        }
        navOptions()
    },
)

fun NavGraphBuilder.trainerHomeNavGraph(
    homeDestination: NavGraphBuilder.() -> Unit = { },
) {
    navigation<Route.TrainerMainTab.Home>(startDestination = Route.TrainerHome) {
        composable<Route.TrainerHome> {
            TrainerHomeRoute()
        }
        homeDestination()
    }
}
