package co.kr.tnt.trainer.modifymyinfo.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainer.modifymyinfo.TrainerModifyMyInfoRoute

fun NavController.navigateToTrainerModifyMyInfo(
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.TrainerModifyMyInfo,
    builder = navOptions,
)

fun NavGraphBuilder.trainerModifyMyInfo(
    navigateToPrevious: () -> Unit,
) {
    composable<Route.TrainerModifyMyInfo> {
        TrainerModifyMyInfoRoute(
            navigateToPrevious = navigateToPrevious,
        )
    }
}
