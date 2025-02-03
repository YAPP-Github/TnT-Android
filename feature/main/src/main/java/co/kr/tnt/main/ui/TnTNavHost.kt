package co.kr.tnt.main.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import co.kr.tnt.feature.webview.navigateToWebView
import co.kr.tnt.feature.webview.webViewScreen
import co.kr.tnt.home.navigation.navigateToHome
import co.kr.tnt.login.navigation.loginScreen
import co.kr.tnt.login.navigation.navigateToLogin
import co.kr.tnt.roleselect.navigateToRoleSelection
import co.kr.tnt.roleselect.signUpNavGraph
import co.kr.tnt.trainee.connect.navigation.navigateToTraineeConnect
import co.kr.tnt.trainee.connect.navigation.traineeConnectScreen
import co.kr.tnt.trainee.mypage.navigation.traineeMyPageScreen
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
            signUpNavGraph(
                navigateToTraineeSignUp = navController::navigateToTraineeSignUp,
                navigateToTrainerSignUp = navController::navigateToTrainerSignUp,
            ) {
                trainerSignUpScreen(
                    navigateToPrevious = navController::popBackStack,
                    navigateToConnect = { navController.navigateToTrainerConnect(isFromMyPage = false) },
                )
                traineeSignUpScreen(
                    navigateToPrevious = navController::popBackStack,
                    navigateToConnect = { navController.navigateToTraineeConnect(isFromMyPage = false) },
                )
            }
            trainerConnectScreen(
                navigateToPrevious = navController::popBackStack,
                navigateToHome = { navController.navigateToTrainerMain(clearBackStack = true) },
            )
            traineeConnectScreen(
                navigateToPrevious = navController::popBackStack,
                navigateToHome = { navController.navigateToHome(clearBackStack = true) },
            )
            traineeMyPageScreen(
                navigateToPrevious = navController::popBackStack,
                navigateToTraineeConnect = { navController.navigateToTraineeConnect(isFromMyPage = true) },
                navigateToLogin = { navController.navigateToLogin(clearBackStack = true) },
                navigateToWebView = navController::navigateToWebView,
            )
            trainerMainScreen(
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
