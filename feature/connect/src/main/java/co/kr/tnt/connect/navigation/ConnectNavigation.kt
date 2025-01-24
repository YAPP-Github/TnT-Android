package co.kr.tnt.connect.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import co.kr.tnt.connect.trainee.TraineeConnectRoute
import co.kr.tnt.connect.trainer.TrainerConnectRoute
import co.kr.tnt.navigation.Route

fun NavController.navigateToConnect(
    isTrainer: Boolean,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    if (isTrainer) {
        navigate(Route.TrainerConnect, builder = navOptions)
    } else {
        navigate(Route.TraineeConnect, builder = navOptions)
    },
)

fun NavGraphBuilder.traineeConnectScreen(
    navigateToPrevious: () -> Unit,
    navigateToHome: (Boolean) -> Unit,
) {
    composable<Route.TraineeConnect> { backstackEntry ->
        backstackEntry.toRoute<Route.TraineeConnect>().apply {
            TraineeConnectRoute(
                navigateToPrevious = navigateToPrevious,
                navigateToHome = { navigateToHome(false) },
            )
        }
    }
}

fun NavGraphBuilder.trainerConnectScreen(
    navigateToPrevious: () -> Unit,
    navigateToHome: (Boolean) -> Unit,
) {
    composable<Route.TrainerConnect> { backstackEntry ->
        backstackEntry.toRoute<Route.TrainerConnect>().apply {
            TrainerConnectRoute(
                navigateToPrevious = navigateToPrevious,
                navigateToHome = { navigateToHome(false) },
            )
        }
    }
}
