package co.kr.tnt.roleselect

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import co.kr.tnt.navigation.Route

fun NavController.navigateToRoleSelection(
    authId: String,
    authType: String,
    email: String,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.SignUpBase.RoleSelection(
        authId = authId,
        authType = authType,
        email = email,
    ),
    builder = navOptions,
)

fun NavGraphBuilder.signupNavGraph(
    navigateToTraineeSignUp: (authId: String, authType: String, email: String) -> Unit,
    navigateToTrainerSignUp: (authId: String, authType: String, email: String) -> Unit,
    signUpDestination: NavGraphBuilder.() -> Unit = { },
) {
    navigation<Route.SignUpBaseRoute>(startDestination = Route.SignUpBase.RoleSelection.DEFAULT) {
        composable<Route.SignUpBase.RoleSelection> { navBackstackEntry ->
            navBackstackEntry.toRoute<Route.SignUpBase.RoleSelection>().apply {
                RoleSelectionRoute(
                    navigateToTraineeSignUp = { navigateToTraineeSignUp(authId, authType, email) },
                    navigateToTrainerSignUp = { navigateToTrainerSignUp(authId, authType, email) },
                )
            }
        }
        signUpDestination()
    }
}
