package co.kr.tnt.trainer.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainer.main.TrainerMainRoute

fun NavController.navigateToTrainerMain(
    clearBackStack: Boolean = false,
    navOptions: NavOptionsBuilder.() -> Unit = { },
) = navigate(
    route = Route.TrainerMain,
    navOptions = navOptions {
        if (clearBackStack) {
            popUpTo(graph.startDestinationId) { inclusive = true }
        }
        navOptions()
    },
)

fun NavGraphBuilder.trainerMainScreen() {
    composable<Route.TrainerMain> {
        TrainerMainRoute()
    }
}
