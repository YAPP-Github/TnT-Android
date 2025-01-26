package co.kr.tnt.trainer.connect.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainer.connect.TrainerConnectRoute

fun NavController.navigateToTrainerConnect(
    isFromMyPage: Boolean,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.TrainerConnect(isFromMyPage),
    builder = navOptions,
)

fun NavGraphBuilder.trainerConnectScreen(
    navigateToPrevious: () -> Unit,
    navigateToHome: (Boolean) -> Unit,
) {
    composable<Route.TrainerConnect> { backstackEntry ->
        backstackEntry.toRoute<Route.TrainerConnect>().apply {
            TrainerConnectRoute(
                isFromMyPage = isFromMyPage,
                navigateToPrevious = navigateToPrevious,
                navigateToHome = { navigateToHome(true) },
            )
        }
    }
}
