package co.kr.tnt.main.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import co.kr.tnt.feature.webview.navigateToWebView
import co.kr.tnt.feature.webview.webViewScreen
import co.kr.tnt.login.navigation.loginScreen
import co.kr.tnt.login.navigation.navigateToLogin
import co.kr.tnt.roleselect.navigateToRoleSelection
import co.kr.tnt.roleselect.roleSelectionScreen
import co.kr.tnt.trainee.connect.navigation.navigateToTraineeConnect
import co.kr.tnt.trainee.connect.navigation.traineeConnectScreen
import co.kr.tnt.trainee.main.navigation.navigateToTraineeMain
import co.kr.tnt.trainee.main.navigation.traineeMainScreen
import co.kr.tnt.trainee.signup.navigation.navigateToTraineeSignUp
import co.kr.tnt.trainee.signup.navigation.traineeSignUpScreen
import co.kr.tnt.trainer.connect.navigation.navigateToTrainerConnect
import co.kr.tnt.trainer.connect.navigation.trainerConnectScreen
import co.kr.tnt.trainer.main.navigation.navigateToTrainerMain
import co.kr.tnt.trainer.main.navigation.trainerMainScreen
import co.kr.tnt.trainer.signup.navigation.navigateToTrainerSignUp
import co.kr.tnt.trainer.signup.navigation.trainerSignUpScreen

@Composable
fun TnTNavHost(
    appState: TnTAppState,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController
    val mainNavController = appState.mainNavController

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        NavHost(
            navController = navController,
            startDestination = appState.startDestination,
        ) {
            loginScreen(
                navigateToHome = {
                    navController.navigateToTrainerMain(clearBackStack = true)
                },
                navigateToSignup = { loginResult ->
                    navController.navigateToRoleSelection(
                        authId = loginResult.authId,
                        authType = loginResult.authType.name,
                        email = loginResult.email,
                    )
                },
            )
            roleSelectionScreen(
                navigateToTraineeSignUp = navController::navigateToTraineeSignUp,
                navigateToTrainerSignUp = navController::navigateToTrainerSignUp,
            )
            trainerSignUpScreen(
                navigateToPrevious = navController::popBackStack,
                navigateToConnect = { navController.navigateToTrainerConnect(isFromMyPage = false) },
            )
            traineeSignUpScreen(
                navigateToPrevious = navController::popBackStack,
                navigateToConnect = { navController.navigateToTraineeConnect(isFromMyPage = false) },
            )
            trainerConnectScreen(
                navigateToPrevious = navController::popBackStack,
                navigateToHome = { navController.navigateToTrainerMain(clearBackStack = true) },
            )
            traineeConnectScreen(
                navigateToPrevious = navController::popBackStack,
                navigateToHome = { navController.navigateToTraineeMain(clearBackStack = true) },
            )
            trainerMainScreen(
                navController = mainNavController,
                navigateToConnect = { navController.navigateToTraineeConnect(true) },
                navigateToWebView = navController::navigateToWebView,
                navigateToLogin = { navController.navigateToLogin(clearBackStack = true) },
            )
            traineeMainScreen(
                navController = mainNavController,
                navigateToConnect = { navController.navigateToTraineeConnect(true) },
                navigateToWebView = navController::navigateToWebView,
                navigateToLogin = { navController.navigateToLogin(clearBackStack = true) },
            )
            webViewScreen(
                navigateToPrevious = navController::popBackStack,
            )
        }
    }
}
