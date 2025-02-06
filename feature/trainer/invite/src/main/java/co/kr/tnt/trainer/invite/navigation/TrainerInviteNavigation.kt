package co.kr.tnt.trainer.invite.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainer.invite.TrainerInviteRoute

fun NavController.navigateToTrainerInvite(
    isSkippable: Boolean,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.TrainerInvite(isSkippable),
    builder = navOptions,
)

fun NavGraphBuilder.trainerInviteScreen(
    navigateToPrevious: () -> Unit,
    navigateToHome: (Boolean) -> Unit,
) {
    composable<Route.TrainerInvite> { backstackEntry ->
        backstackEntry.toRoute<Route.TrainerInvite>().apply {
            TrainerInviteRoute(
                isSkippable = isSkippable,
                navigateToPrevious = navigateToPrevious,
                navigateToHome = { navigateToHome(true) },
            )
        }
    }
}
