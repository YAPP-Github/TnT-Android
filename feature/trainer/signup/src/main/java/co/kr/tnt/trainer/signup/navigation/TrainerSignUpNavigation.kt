package co.kr.tnt.trainer.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import co.kr.tnt.navigation.Route
import co.kr.tnt.navigation.model.ScreenMode
import co.kr.tnt.trainer.signup.TrainerSignUpRoute

fun NavController.navigateToTrainerSignUp(
    authId: String,
    authType: String,
    email: String,
    messagingToken: String,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.TrainerSignUp(
        authId = authId,
        authType = authType,
        email = email,
        messagingToken = messagingToken,
    ),
    builder = navOptions,
)

fun NavGraphBuilder.trainerSignUpScreen(
    navigateToPrevious: () -> Unit,
    navigateToInvite: (ScreenMode) -> Unit,
) {
    composable<Route.TrainerSignUp> { backstackEntry ->
        backstackEntry.toRoute<Route.TrainerSignUp>().apply {
            TrainerSignUpRoute(
                authId = authId,
                authType = authType,
                email = email,
                messagingToken = messagingToken,
                navigateToPrevious = navigateToPrevious,
                navigateToInvite = navigateToInvite,
            )
        }
    }
}
