package co.kr.tnt.trainee.connect.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import co.kr.tnt.navigation.Route
import co.kr.tnt.navigation.model.ScreenMode
import co.kr.tnt.trainee.connect.TraineeConnectRoute

fun NavController.navigateToTraineeConnect(
    screenMode: ScreenMode,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.TraineeConnect(screenMode),
    builder = navOptions,
)

fun NavGraphBuilder.traineeConnectScreen(
    navigateToPrevious: () -> Unit,
    navigateToHome: (Boolean) -> Unit,
) {
    composable<Route.TraineeConnect> { backstackEntry ->
        backstackEntry.toRoute<Route.TraineeConnect>().apply {
            TraineeConnectRoute(
                screenMode = screenMode,
                navigateToPrevious = navigateToPrevious,
                navigateToHome = { navigateToHome(false) },
            )
        }
    }
}
