package co.kr.tnt.trainee.mypage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainee.mypage.TraineeMyPageRoute

fun NavController.navigateToTraineeMyPage(
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.TraineeMyPage,
    builder = navOptions,
)

fun NavGraphBuilder.traineeMyPageNavGraph(
    navigateToPrevious: () -> Unit,
    navigateToTraineeConnect: (Boolean) -> Unit,
    navigateToLogin: () -> Unit,
    navigateToWebView: (String) -> Unit,
    myPageDestination: NavGraphBuilder.() -> Unit = { },
) {
    navigation<Route.TraineeMainTab.MyPage>(startDestination = Route.TraineeMyPage) {
        composable<Route.TraineeMyPage> {
            TraineeMyPageRoute(
                navigateToPrevious = navigateToPrevious,
                navigateToConnect = navigateToTraineeConnect,
                navigateToLogin = navigateToLogin,
                navigateToWebView = navigateToWebView,
            )
        }
        myPageDestination()
    }
}
