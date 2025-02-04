package co.kr.tnt.trainee.main

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
import androidx.navigation.navOptions
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainee.home.navigation.traineeHomeNavGraph
import co.kr.tnt.trainee.mypage.navigation.traineeMyPageNavGraph

@Composable
internal fun TraineeMainRoute(
    navController: NavHostController,
    navigateToConnect: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToWebView: (url: String) -> Unit,
) {
    TraineeMainScreen(
        navController = navController,
        navigateToConnect = navigateToConnect,
        navigateToLogin = navigateToLogin,
        navigateToWebView = navigateToWebView,
    )
}

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
private fun TraineeMainScreen(
    navController: NavHostController,
    navigateToConnect: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToWebView: (url: String) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            TraineeMainBottomBar { tab ->
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
            startDestination = Route.TraineeMainTab.Home,
        ) {
            traineeHomeNavGraph()
            traineeMyPageNavGraph(
                navigateToPrevious = navController::popBackStack,
                navigateToLogin = navigateToLogin,
                navigateToWebView = navigateToWebView,
                navigateToTraineeConnect = navigateToConnect,
            )
        }
    }
}

@Composable
private fun TraineeMainBottomBar(
    onClickTab: (tab: TraineeMainTab) -> Unit,
) {
    Row(
        modifier = Modifier.navigationBarsPadding(),
    ) {
        TraineeMainTab.entries.forEach { tab ->
            Button(
                onClick = { onClickTab(tab) },
            ) {
                Text(tab.contentDescription)
            }
        }
    }
}
