package co.kr.tnt.feature.webview

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import co.kr.tnt.navigation.Route

fun NavController.navigateToWebView(
    url: String,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.WebView(url),
    builder = navOptions,
)

fun NavGraphBuilder.webViewScreen(
    navigateToPrevious: () -> Unit,
) {
    composable<Route.WebView> { backstackEntry ->
        backstackEntry.toRoute<Route.WebView>().apply {
            WebViewScreen(
                url = url,
                onBackClick = navigateToPrevious,
            )
        }
    }
}
