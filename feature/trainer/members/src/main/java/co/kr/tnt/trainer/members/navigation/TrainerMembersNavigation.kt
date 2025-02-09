package co.kr.tnt.trainer.members.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainer.members.TrainerMembersRoute

fun NavController.navigateToTrainerMembers(
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.TrainerMembers,
    builder = navOptions,
)

fun NavGraphBuilder.trainerMembersNavGraph(
    navigateToInvite: (Boolean) -> Unit,
    membersDestination: NavGraphBuilder.() -> Unit = { },
) {
    navigation<Route.TrainerMainTab.Members>(startDestination = Route.TrainerMembers) {
        composable<Route.TrainerMembers> {
            TrainerMembersRoute(
                navigateToInvite = navigateToInvite,
            )
        }
        membersDestination()
    }
}
