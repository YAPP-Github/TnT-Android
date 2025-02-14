package co.kr.tnt.trainer.mypage.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navOptions
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainer.mypage.TrainerMyPageRoute

fun NavController.navigateToTrainerMyPage(
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.TrainerMyPage,
    builder = navOptions,
)

fun NavGraphBuilder.trainerMyPageNavGraph(
    padding: PaddingValues,
    navigateToLogin: () -> Unit,
    navigateToWebView: (String) -> Unit,
    myPageDestination: NavGraphBuilder.() -> Unit = { },
) {
    navigation<Route.TrainerMainTab.MyPage>(startDestination = Route.TrainerMyPage) {
        composable<Route.TrainerMyPage> {
            TrainerMyPageRoute(
                padding = padding,
                navigateToLogin = navigateToLogin,
                navigateToWebView = navigateToWebView,
            )
        }
        myPageDestination()
    }
}
