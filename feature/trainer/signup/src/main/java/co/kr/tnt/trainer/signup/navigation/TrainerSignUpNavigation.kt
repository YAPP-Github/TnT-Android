package co.kr.tnt.trainer.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainer.signup.TrainerSignUpRoute

fun NavController.navigateToTrainerSignUp(
    authId: String,
    authType: String,
    email: String,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.SignUpBase.TrainerSignUp(
        authId = authId,
        authType = authType,
        email = email,
    ),
    builder = navOptions,
)

fun NavGraphBuilder.trainerSignUpScreen(
    navigateToPrevious: () -> Unit,
    navigateToConnect: () -> Unit,
) {
    composable<Route.SignUpBase.TrainerSignUp> { backstackEntry ->
        backstackEntry.toRoute<Route.SignUpBase.TrainerSignUp>().apply {
            TrainerSignUpRoute(
                authId = authId,
                authType = authType,
                email = email,
                navigateToPrevious = navigateToPrevious,
                navigateToConnect = { navigateToConnect() },
            )
        }
    }
}
