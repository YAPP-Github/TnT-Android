package co.kr.tnt.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import co.kr.tnt.domain.model.LoginResult
import co.kr.tnt.domain.model.UserType
import co.kr.tnt.login.LoginRoute
import co.kr.tnt.navigation.Route

fun NavController.navigateToLogin(
    clearBackStack: Boolean = false,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.Login,
    navOptions = navOptions {
        if (clearBackStack) {
            popUpTo(graph.id) { inclusive = true }
        }
        navOptions()
    },
)

fun NavGraphBuilder.loginScreen(
    navigateToWebView: (url: String) -> Unit,
    navigateToHome: (UserType) -> Unit,
    navigateToSignup: (loginResult: LoginResult, messagingToken: String) -> Unit,
) {
    composable<Route.Login> {
        LoginRoute(
            navigateToWebView = navigateToWebView,
            navigateToHome = navigateToHome,
            navigateToSignup = navigateToSignup,
        )
    }
}
