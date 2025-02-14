package co.kr.tnt.trainer.feedback.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainer.feedback.TrainerFeedbackRoute

fun NavController.navigateToTrainerFeedback(
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.TrainerFeedback,
    builder = navOptions,
)

fun NavGraphBuilder.trainerFeedbackNavGraph(
    padding: PaddingValues,
    feedbackDestination: NavGraphBuilder.() -> Unit = { },
) {
    navigation<Route.TrainerMainTab.Feedback>(startDestination = Route.TrainerFeedback) {
        composable<Route.TrainerFeedback> {
            TrainerFeedbackRoute(padding)
        }
        feedbackDestination()
    }
}
