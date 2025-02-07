package co.kr.tnt.trainer.connect.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainer.connect.TrainerConnectRoute

fun NavController.navigateToTrainerConnect(
    trainerId: String,
    traineeId: String,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.TrainerConnect(trainerId, traineeId),
    builder = navOptions,
)

fun NavGraphBuilder.trainerConnectScreen(
    navigateToPrevious: () -> Unit,
    navigateToHome: (Boolean) -> Unit,
) {
    composable<Route.TrainerConnect> { backstackEntry ->
        backstackEntry.toRoute<Route.TrainerConnect>().apply {
            TrainerConnectRoute(
                trainerId = trainerId,
                traineeId = traineeId,
                navigateToPrevious = navigateToPrevious,
                navigateToHome = { navigateToHome(true) },
            )
        }
    }
}
