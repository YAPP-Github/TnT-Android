package co.kr.tnt.main.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import co.kr.tnt.home.navigation.homeNavGraph
import co.kr.tnt.home.navigation.navigateToHome
import co.kr.tnt.login.navigation.loginScreen
import co.kr.tnt.trainee.connect.navigation.traineeConnectScreen
import co.kr.tnt.trainer.connect.navigation.trainerConnectScreen
import co.kr.tnt.navigation.Route

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
                navigateToHome = {
                    navController.navigateToHome(isTrainer = true) {
                        popUpTo(Route.Login) {
                            inclusive = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                navigateToSignup = { },
            )
            trainerConnectScreen(
                navigateToPrevious = { navController.popBackStack() },
                navigateToHome = {
                    navController.navigateToHome(isTrainer = true, clearBackStack = true)
                },
            )
            traineeConnectScreen(
                navigateToPrevious = { navController.popBackStack() },
                navigateToHome = {
                    navController.navigateToHome(isTrainer = false, clearBackStack = true)
                },
            )
            homeNavGraph {
            }
        }
    }
}
