package co.kr.tnt.trainee.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import co.kr.tnt.navigation.Route
import co.kr.tnt.navigation.model.ScreenMode
import co.kr.tnt.trainee.main.TraineeMainRoute

fun NavController.navigateToTraineeMain(
    clearBackStack: Boolean = false,
    navOptions: NavOptionsBuilder.() -> Unit = { },
) = navigate(
    route = Route.TraineeMain,
    navOptions = navOptions {
        if (clearBackStack) {
            popUpTo(graph.id) { inclusive = true }
        }
        navOptions()
    },
)

fun NavGraphBuilder.traineeMainScreen(
    navigateToConnect: (ScreenMode) -> Unit,
    navigateToLogin: () -> Unit,
    navigateToWebView: (url: String) -> Unit,
    navigateToMealRecord: () -> Unit,
    navigateToMealDetail: (id: Long) -> Unit,
) {
    composable<Route.TraineeMain> {
        TraineeMainRoute(
            navigateToConnect = navigateToConnect,
            navigateToLogin = navigateToLogin,
            navigateToWebView = navigateToWebView,
            navigateToMealRecord = navigateToMealRecord,
            navigateToMealDetail = navigateToMealDetail,
        )
    }
}
