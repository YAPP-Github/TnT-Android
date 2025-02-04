package co.kr.tnt.trainee.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainee.main.TraineeMainRoute

fun NavController.navigateToTraineeMain(
    clearBackStack: Boolean = false,
    navOptions: NavOptionsBuilder.() -> Unit = { },
) = navigate(
    route = Route.TraineeMain,
    navOptions = navOptions {
        if (clearBackStack) {
            popUpTo(graph.startDestinationId) { inclusive = true }
        }
        navOptions()
    },
)

fun NavGraphBuilder.traineeMainScreen(
    navController: NavHostController,
    navigateToConnect: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToWebView: (url: String) -> Unit,
) {
    composable<Route.TraineeMain> {
        TraineeMainRoute(
            navController = navController,
            navigateToConnect = navigateToConnect,
            navigateToLogin = navigateToLogin,
            navigateToWebView = navigateToWebView,
        )
    }
}
