package co.kr.tnt.trainer.connect.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import co.kr.tnt.connect.trainer.TrainerConnectRoute
import co.kr.tnt.navigation.Route

fun NavController.navigateToConnect(
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.TrainerConnect,
    builder = navOptions,
)

fun NavGraphBuilder.trainerConnectScreen(
    navigateToPrevious: () -> Unit,
    navigateToHome: (Boolean) -> Unit,
) {
    composable<Route.TrainerConnect> { backstackEntry ->
        backstackEntry.toRoute<Route.TrainerConnect>().apply {
            TrainerConnectRoute(
                navigateToPrevious = navigateToPrevious,
                navigateToHome = { navigateToHome(true) },
            )
        }
    }
}
