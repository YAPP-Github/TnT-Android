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
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.RoleSelection(
        authId = authId,
        authType = authType,
        email = email,
    ),
    builder = navOptions,
)

fun NavGraphBuilder.roleSelectionScreen() {
    composable<Route.RoleSelection> { navBackstackEntry ->
        navBackstackEntry.toRoute<Route.RoleSelection>().apply {
            RoleSelectionScreen(
                authId = authId,
                authType = authType,
                email = email,
            )
        }
    }
}
