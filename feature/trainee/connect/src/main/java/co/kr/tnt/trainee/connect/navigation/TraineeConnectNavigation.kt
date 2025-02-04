package co.kr.tnt.trainee.connect.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainee.connect.TraineeConnectRoute

fun NavController.navigateToTraineeConnect(
    isSkippable: Boolean,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.TraineeConnect(isSkippable),
    builder = navOptions,
)

fun NavGraphBuilder.traineeConnectScreen(
    navigateToPrevious: () -> Unit,
    navigateToHome: (Boolean) -> Unit,
) {
    composable<Route.TraineeConnect> { backstackEntry ->
        backstackEntry.toRoute<Route.TraineeConnect>().apply {
            TraineeConnectRoute(
                isSkippable = isSkippable,
                navigateToPrevious = navigateToPrevious,
                navigateToHome = { navigateToHome(false) },
            )
        }
    }
}
