package co.kr.tnt.trainer.invite.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import co.kr.tnt.navigation.Route
import co.kr.tnt.navigation.model.ScreenMode
import co.kr.tnt.trainer.invite.TrainerInviteRoute

fun NavController.navigateToTrainerInvite(
    screenMode: ScreenMode,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.TrainerInvite(screenMode),
    builder = navOptions,
)

fun NavGraphBuilder.trainerInviteScreen(
    navigateToPrevious: () -> Unit,
    navigateToHome: (Boolean) -> Unit,
) {
    composable<Route.TrainerInvite> { backstackEntry ->
        backstackEntry.toRoute<Route.TrainerInvite>().apply {
            TrainerInviteRoute(
                screenMode = screenMode,
                navigateToPrevious = navigateToPrevious,
                navigateToHome = { navigateToHome(true) },
            )
        }
    }
}
