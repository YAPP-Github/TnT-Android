package co.kr.tnt.roleselect

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import co.kr.tnt.navigation.Route

fun NavController.navigateToRoleSelection(
    authId: String,
    authType: String,
    email: String,
    messagingToken: String,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.RoleSelection(
        authId = authId,
        authType = authType,
        email = email,
        messagingToken = messagingToken,
    ),
    builder = navOptions,
)

fun NavGraphBuilder.roleSelectionScreen(
    navigateToTraineeSignUp: (authId: String, authType: String, email: String, messagingToken: String) -> Unit,
    navigateToTrainerSignUp: (authId: String, authType: String, email: String, messagingToken: String) -> Unit,
) {
    composable<Route.RoleSelection> { navBackstackEntry ->
        navBackstackEntry.toRoute<Route.RoleSelection>().apply {
            RoleSelectionRoute(
                navigateToTraineeSignUp = { navigateToTraineeSignUp(authId, authType, email, messagingToken) },
                navigateToTrainerSignUp = { navigateToTrainerSignUp(authId, authType, email, messagingToken) },
            )
        }
    }
}
