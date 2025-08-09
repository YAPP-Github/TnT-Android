package co.kr.tnt.trainee.modifymyinfo.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainee.modifymyinfo.TraineeModifyMyInfoRoute

fun NavController.navigateToTraineeModifyMyInfo(
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.TraineeModifyMyInfo,
    builder = navOptions,
)

fun NavGraphBuilder.traineeModifyMyInfo(
    navigateToPrevious: () -> Unit,
) {
    composable<Route.TraineeModifyMyInfo> {
        TraineeModifyMyInfoRoute(
            navigateToPrevious = navigateToPrevious,
        )
    }
}
