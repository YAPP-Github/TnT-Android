package co.kr.tnt.trainer.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainer.feedback.navigation.trainerFeedbackNavGraph
import co.kr.tnt.trainer.home.navigation.trainerHomeNavGraph
import co.kr.tnt.trainer.members.navigation.trainerMembersNavGraph
import co.kr.tnt.trainer.mypage.navigation.trainerMyPageNavGraph
import co.kr.tnt.trainer.notification.navigation.navigateToTrainerNotification
import co.kr.tnt.trainer.notification.navigation.trainerNotification

@Composable
internal fun TrainerMainRoute(
    navigateToConnect: (isSkippable: Boolean, isCompleted: Boolean) -> Unit,
    navigateToLogin: () -> Unit,
    navigateToWebView: (url: String) -> Unit,
    navController: NavHostController = rememberNavController(),
) {
    TrainerMainScreen(
        navController = navController,
        navigateToConnect = navigateToConnect,
        navigateToLogin = navigateToLogin,
        navigateToWebView = navigateToWebView,
    )
}

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Suppress("UnusedParameter")
private fun TrainerMainScreen(
    navController: NavHostController,
    navigateToConnect: (isSkippable: Boolean, isCompleted: Boolean) -> Unit,
    navigateToLogin: () -> Unit,
    navigateToWebView: (url: String) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            TrainerMainBottomBar { tab ->
                navController.navigate(
                    route = tab.route,
                    navOptions = navOptions {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    },
                )
            }
        },
    ) { _ ->
        NavHost(
            navController = navController,
            startDestination = Route.TrainerMainTab.Home,
        ) {
            trainerHomeNavGraph(
                navigateToNotification = navController::navigateToTrainerNotification,
            ) {
                trainerNotification(
                    navigateToPrevious = navController::popBackStack,
                    navigateToConnect = { navigateToConnect(false, true) },
                )
            }
            trainerFeedbackNavGraph()
            trainerMembersNavGraph()
            trainerMyPageNavGraph()
        }
    }
}

@Composable
private fun TrainerMainBottomBar(
    onClickTab: (tab: TrainerMainTab) -> Unit,
) {
    Row(
        modifier = Modifier.navigationBarsPadding(),
    ) {
        TrainerMainTab.entries.forEach { tab ->
            Button(
                onClick = { onClickTab(tab) },
            ) {
                Text(tab.contentDescription)
            }
        }
    }
}
