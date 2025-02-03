package co.kr.tnt.trainer.members.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navOptions
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainer.members.TrainerMembersRoute

fun NavController.navigateToTrainerMembers(
    clearBackStack: Boolean = false,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.TrainerMembers,
    navOptions = navOptions {
        if (clearBackStack) {
            popUpTo(graph.startDestinationId) { inclusive = true }
        }
        navOptions()
    },
)

fun NavGraphBuilder.trainerMembersNavGraph(
    membersDestination: NavGraphBuilder.() -> Unit = { },
) {
    navigation<Route.TrainerMainTab.Members>(startDestination = Route.TrainerMembers) {
        composable<Route.TrainerMembers> {
            TrainerMembersRoute()
        }
        membersDestination()
    }
}
