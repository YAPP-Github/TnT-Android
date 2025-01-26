package co.kr.tnt.trainer.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainer.signup.TrainerSignUpRoute

fun NavController.navigateToTrainerSignUp(
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.TrainerSignUp,
    builder = navOptions,
)

fun NavGraphBuilder.trainerSignUpScreen(
    navigateToPrevious: () -> Unit,
    navigateToConnect: () -> Unit,
) {
    composable<Route.TrainerSignUp> { backstackEntry ->
        backstackEntry.toRoute<Route.TrainerSignUp>().apply {
            // TODO 115 머지되면 connect로 이동
            TrainerSignUpRoute(
                navigateToPrevious = navigateToPrevious,
                navigateToConnect = { navigateToConnect() },
            )
        }
    }
}
