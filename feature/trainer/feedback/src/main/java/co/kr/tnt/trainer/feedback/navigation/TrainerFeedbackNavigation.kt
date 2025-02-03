package co.kr.tnt.trainer.feedback.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navOptions
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainer.feedback.TrainerFeedbackRoute

fun NavController.navigateToTrainerFeedback(
    clearBackStack: Boolean = false,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.TrainerHome,
    navOptions = navOptions {
        if (clearBackStack) {
            popUpTo(graph.startDestinationId) { inclusive = true }
        }
        navOptions()
    },
)

fun NavGraphBuilder.trainerFeedbackNavGraph(
    feedbackDestination: NavGraphBuilder.() -> Unit = { },
) {
    navigation<Route.TrainerMainTab.Feedback>(startDestination = Route.TrainerFeedback) {
        composable<Route.TrainerFeedback> {
            TrainerFeedbackRoute()
        }
        feedbackDestination()
    }
}
