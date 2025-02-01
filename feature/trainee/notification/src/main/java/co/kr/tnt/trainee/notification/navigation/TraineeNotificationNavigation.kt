package co.kr.tnt.trainee.notification.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainee.notification.TraineeNotificationRoute

fun NavController.navigateToTraineeNotification(
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.TraineeNotification,
    builder = navOptions,
)

fun NavGraphBuilder.traineeNotification(
    navigateToPrevious: () -> Unit,
) {
    composable<Route.TraineeNotification> {
        TraineeNotificationRoute(
            navigateToPrevious = navigateToPrevious,
        )
    }
}
