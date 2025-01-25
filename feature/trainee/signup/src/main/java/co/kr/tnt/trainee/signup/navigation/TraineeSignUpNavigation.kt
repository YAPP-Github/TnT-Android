package co.kr.tnt.trainee.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainee.signup.TraineeSignUpRoute

fun NavController.navigateToTraineeSignUp(
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.TraineeSignUp,
    builder = navOptions,
)

fun NavGraphBuilder.traineeSignUpScreen(
    navigateToPrevious: () -> Unit,
    navigateToHome: () -> Unit,
) {
    composable<Route.TraineeSignUp> { backstackEntry ->
        backstackEntry.toRoute<Route.TraineeSignUp>().apply {
            // TODO 115 머지되면 connect로 이동
            TraineeSignUpRoute(
                navigateToPrevious = navigateToPrevious,
                navigateToConnect = { navigateToHome() },
            )
        }
    }
}
