package co.kr.tnt.trainee.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainee.home.TraineeHomeRoute

fun NavController.navigateToTraineeHome(
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.TraineeHome,
    builder = navOptions,
)

fun NavGraphBuilder.traineeHomeNavGraph(
    padding: PaddingValues,
    navigateToNotification: () -> Unit,
    navigateToMealRecord: () -> Unit,
    navigateToMealDetail: (id: Long) -> Unit,
    homeDestination: NavGraphBuilder.() -> Unit = { },
) {
    navigation<Route.TraineeMainTab.Home>(startDestination = Route.TraineeHome) {
        composable<Route.TraineeHome> {
            TraineeHomeRoute(
                padding = padding,
                navigateToNotification = navigateToNotification,
                navigateToExerciseRecord = { /* TODO */ },
                navigateToMealRecord = navigateToMealRecord,
                navigateToMealDetail = navigateToMealDetail,
            )
        }
        homeDestination()
    }
}
