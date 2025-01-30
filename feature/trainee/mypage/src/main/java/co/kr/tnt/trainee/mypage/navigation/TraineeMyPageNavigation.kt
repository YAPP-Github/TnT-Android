package co.kr.tnt.trainee.mypage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainee.mypage.TraineeMyPageRoute

fun NavController.navigateToTraineeMyPage(
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.TraineeMyPage,
    builder = navOptions,
)

fun NavGraphBuilder.traineeMyPageScreen(
    navigateToPrevious: () -> Unit,
    navigateToTraineeConnect: () -> Unit,
    navigateToLogin: () -> Unit,
) {
    composable<Route.TraineeMyPage> {
        TraineeMyPageRoute(
            navigateToPrevious = navigateToPrevious,
            navigateToConnect = navigateToTraineeConnect,
            navigateToLogin = navigateToLogin,
        )
    }
}
