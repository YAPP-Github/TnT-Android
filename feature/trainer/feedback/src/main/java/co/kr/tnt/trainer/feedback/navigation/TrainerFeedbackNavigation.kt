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
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.TrainerFeedback,
    builder = navOptions,
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
