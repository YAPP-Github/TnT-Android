package co.kr.tnt.main.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import co.kr.tnt.home.navigation.homeNavGraph
import co.kr.tnt.login.navigation.loginScreen

@Composable
fun TnTNavHost(
    appState: TnTAppState,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        NavHost(
            navController = navController,
            startDestination = appState.startDestination,
        ) {
            loginScreen(
                navigateToHome = { },
                navigateToSignup = { },
            )
            homeNavGraph {
            }
        }
    }
}
