package co.kr.tnt.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import co.kr.tnt.login.LoginRoute
import co.kr.tnt.navigation.Route

fun NavController.navigateToLogin(
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.Login,
    builder = navOptions,
)

fun NavGraphBuilder.loginScreen(
    navigateToHome: () -> Unit,
    navigateToSignup: () -> Unit,
) {
    composable<Route.Login> {
        LoginRoute(
            navigateToHome = navigateToHome,
            navigateToSignup = navigateToSignup,
        )
    }
}
