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
    route = Route.TrainerSignUp(
        authId = authId,
        authType = authType,
        email = email,
    ),
    builder = navOptions,
)

fun NavGraphBuilder.trainerSignUpScreen(
    navigateToPrevious: () -> Unit,
    navigateToInvite: (Boolean) -> Unit,
) {
    composable<Route.TrainerSignUp> { backstackEntry ->
        backstackEntry.toRoute<Route.TrainerSignUp>().apply {
            TrainerSignUpRoute(
                authId = authId,
                authType = authType,
                email = email,
                navigateToPrevious = navigateToPrevious,
                navigateToInvite = navigateToInvite,
            )
        }
    }
}
