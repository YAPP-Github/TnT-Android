package co.kr.tnt.trainer.mypage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navOptions
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainer.mypage.TrainerMyPageRoute

fun NavController.navigateToTrainerMyPage(
    clearBackStack: Boolean = false,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.TrainerMyPage,
    navOptions = navOptions {
        if (clearBackStack) {
            popUpTo(graph.startDestinationId) { inclusive = true }
        }
        navOptions()
    },
)

fun NavGraphBuilder.trainerMyPageNavGraph(
    myPageDestination: NavGraphBuilder.() -> Unit = { },
) {
    navigation<Route.TrainerMainTab.MyPage>(startDestination = Route.TrainerMyPage) {
        composable<Route.TrainerMyPage> {
            TrainerMyPageRoute()
        }
        myPageDestination()
    }
}
