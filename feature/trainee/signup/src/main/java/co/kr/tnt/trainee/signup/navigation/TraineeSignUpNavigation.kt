package co.kr.tnt.trainee.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainee.signup.TraineeSignUpRoute

fun NavController.navigateToTraineeSignUp(
    authId: String,
    authType: String,
    email: String,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.TraineeSignUp(
        authId = authId,
        authType = authType,
        email = email,
    ),
    builder = navOptions,
)

fun NavGraphBuilder.traineeSignUpScreen(
    navigateToPrevious: () -> Unit,
    navigateToConnect: () -> Unit,
) {
    composable<Route.TraineeSignUp> { backstackEntry ->
        backstackEntry.toRoute<Route.TraineeSignUp>().apply {
            TraineeSignUpRoute(
                authId = authId,
                authType = authType,
                email = email,
                navigateToPrevious = navigateToPrevious,
                navigateToConnect = navigateToConnect,
            )
        }
    }
}
