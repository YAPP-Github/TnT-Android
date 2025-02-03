package co.kr.tnt.trainer.main

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
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainer.feedback.navigation.trainerFeedbackNavGraph
import co.kr.tnt.trainer.home.navigation.trainerHomeNavGraph
import co.kr.tnt.trainer.members.navigation.trainerMembersNavGraph

@Composable
@Suppress("UnusedParameter")
internal fun TrainerMainRoute(
    navController: NavHostController,
    navigateToConnect: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToWebView: (url: String) -> Unit,
) {
    TrainerMainScreen(
        navController = navController,
        navigateToConnect = navigateToConnect,
        navigateToLogin = navigateToLogin,
        navigateToWebView = navigateToWebView,
    )
}

@Composable
fun TrainerMainScreen(
    navController: NavHostController,
    navigateToConnect: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToWebView: (url: String) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Row(
                modifier = Modifier.navigationBarsPadding(),
            ) {
                Button(
                    onClick = {
                        navController.navigate(
                            route = Route.TrainerMainTab.Home,
                            navOptions = getTopLevelNavOptions(navController),
                        )
                    },
                ) {
                    Text("home")
                }
                Button(
                    onClick = {
                        navController.navigate(
                            route = Route.TrainerMainTab.Feedback,
                            navOptions = getTopLevelNavOptions(navController),
                        )
                    },
                ) {
                    Text("feedBack")
                }
                Button(
                    onClick = {
                        navController.navigate(
                            route = Route.TrainerMainTab.Members,
                            navOptions = getTopLevelNavOptions(navController),
                        )
                    },
                ) {
                    Text("members")
                }
                Button(
                    onClick = {
                        navController.navigate(
                            route = Route.TrainerMainTab.MyPage,
                            navOptions = getTopLevelNavOptions(navController),
                        )
                    },
                ) {
                    Text("my page")
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = Route.TrainerMainTab.Home,
        ) {
            trainerHomeNavGraph()
            trainerFeedbackNavGraph()
            trainerMembersNavGraph()
            composable<Route.TrainerMainTab.MyPage> {
                Text(text = "trainer my page")
            }
        }
    }
}

private fun getTopLevelNavOptions(navController: NavHostController) = navOptions {
    popUpTo(navController.graph.findStartDestination().id) {
        saveState = true
    }
    launchSingleTop = true
    restoreState = true
}
