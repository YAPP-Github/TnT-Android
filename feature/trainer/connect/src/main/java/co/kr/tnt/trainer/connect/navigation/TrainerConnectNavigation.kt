package co.kr.tnt.trainer.connect.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainer.connect.TrainerConnectRoute

fun NavController.navigateToTrainerConnect(
    isSkippable: Boolean,
    isCompleted: Boolean,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.TrainerConnect(isSkippable, isCompleted),
    builder = navOptions,
)

fun NavGraphBuilder.trainerConnectScreen(
    navigateToPrevious: () -> Unit,
    navigateToHome: (Boolean) -> Unit,
) {
    composable<Route.TrainerConnect> { backstackEntry ->
        backstackEntry.toRoute<Route.TrainerConnect>().apply {
            TrainerConnectRoute(
                isSkippable = isSkippable,
                isCompleted = isCompleted,
                navigateToPrevious = navigateToPrevious,
                navigateToHome = { navigateToHome(true) },
            )
        }
    }
}
