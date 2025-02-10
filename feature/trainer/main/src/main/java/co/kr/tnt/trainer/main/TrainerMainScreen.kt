package co.kr.tnt.trainer.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
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
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainer.feedback.navigation.trainerFeedbackNavGraph
import co.kr.tnt.trainer.home.navigation.trainerHomeNavGraph
import co.kr.tnt.trainer.members.navigation.trainerMembersNavGraph
import co.kr.tnt.trainer.mypage.navigation.trainerMyPageNavGraph
import co.kr.tnt.trainer.notification.navigation.navigateToTrainerNotification
import co.kr.tnt.trainer.notification.navigation.trainerNotification

@Composable
internal fun TrainerMainRoute(
    navigateToConnect: (trainerId: String, traineeId: String) -> Unit,
    navigateToInvite: (Boolean) -> Unit,
    navigateToLogin: () -> Unit,
    navigateToWebView: (url: String) -> Unit,
    navController: NavHostController = rememberNavController(),
) {
    TrainerMainScreen(
        navController = navController,
        navigateToConnect = navigateToConnect,
        navigateToInvite = navigateToInvite,
        navigateToLogin = navigateToLogin,
        navigateToWebView = navigateToWebView,
    )
}

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Suppress("UnusedParameter")
private fun TrainerMainScreen(
    navController: NavHostController,
    navigateToConnect: (trainerId: String, traineeId: String) -> Unit,
    navigateToInvite: (Boolean) -> Unit,
    navigateToLogin: () -> Unit,
    navigateToWebView: (url: String) -> Unit,
) {
    Scaffold(
        containerColor = TnTTheme.colors.commonColors.Common0,
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
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = Route.TrainerMainTab.Home,
        ) {
            trainerHomeNavGraph(
                navigateToNotification = navController::navigateToTrainerNotification,
            ) {
                trainerNotification(
                    navigateToPrevious = navController::popBackStack,
                    navigateToConnect = navigateToConnect,
                )
            }
            trainerFeedbackNavGraph()
            trainerMembersNavGraph(
                navigateToInvite = navigateToInvite,
            )
            trainerMyPageNavGraph(
                navigateToLogin = navigateToLogin,
                navigateToWebView = navigateToWebView,
            )
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
