package co.kr.tnt.trainer.notification.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainer.notification.TrainerNotificationRoute

fun NavController.navigateToTrainerNotification(
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.TrainerNotification,
    builder = navOptions,
)

fun NavGraphBuilder.trainerNotification(
    navigateToPrevious: () -> Unit,
    navigateToConnect: (trainerId: String, traineeId: String) -> Unit,
) {
    composable<Route.TrainerNotification> {
        TrainerNotificationRoute(
            navigateToPrevious = navigateToPrevious,
            navigateToConnect = navigateToConnect,
        )
    }
}
