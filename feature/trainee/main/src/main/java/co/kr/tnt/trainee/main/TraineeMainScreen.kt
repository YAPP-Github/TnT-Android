package co.kr.tnt.trainee.main

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainee.home.navigation.traineeHomeNavGraph
import co.kr.tnt.trainee.mypage.navigation.traineeMyPageNavGraph
import co.kr.tnt.trainee.notification.navigation.navigateToTraineeNotification
import co.kr.tnt.trainee.notification.navigation.traineeNotification

@Composable
internal fun TraineeMainRoute(
    navigateToConnect: (Boolean) -> Unit,
    navigateToLogin: () -> Unit,
    navigateToWebView: (url: String) -> Unit,
    navController: NavHostController = rememberNavController(),
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
    navigateToConnect: (Boolean) -> Unit,
    navigateToLogin: () -> Unit,
    navigateToWebView: (url: String) -> Unit,
) {
    Scaffold(
        containerColor = TnTTheme.colors.commonColors.Common0,
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            TraineeMainBottomBar(
                navController = navController,
                onClickTab = { tab ->
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
                },
            )
        },
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = Route.TraineeMainTab.Home,
        ) {
            traineeHomeNavGraph(
                navigateToNotification = navController::navigateToTraineeNotification,
            ) {
                traineeNotification(
                    navigateToPrevious = navController::popBackStack,
                )
            }
            traineeMyPageNavGraph(
                navigateToLogin = navigateToLogin,
                navigateToWebView = navigateToWebView,
                navigateToTraineeConnect = navigateToConnect,
            )
        }
    }
}

@Composable
private fun TraineeMainBottomBar(
    navController: NavHostController,
    onClickTab: (tab: TraineeMainTab) -> Unit,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .background(TnTTheme.colors.commonColors.Common0)
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .navigationBarsPadding(),
    ) {
        TraineeMainTab.entries.forEach { tab ->
            val selected = currentRoute?.contains(tab.route.toString()) == true
            BottomAppBarItem(
                tab = tab,
                selected = selected,
                onClickTab = onClickTab,
            )
        }
    }
}

@Composable
private fun RowScope.BottomAppBarItem(
    tab: TraineeMainTab,
    selected: Boolean,
    onClickTab: (tab: TraineeMainTab) -> Unit,
) {
    Button(
        onClick = { onClickTab(tab) },
        modifier = Modifier.weight(1f),
        colors = ButtonColors(
            containerColor = TnTTheme.colors.commonColors.Common0,
            contentColor = TnTTheme.colors.commonColors.Common0,
            disabledContainerColor = TnTTheme.colors.commonColors.Common0,
            disabledContentColor = TnTTheme.colors.commonColors.Common0,
        ),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                painter = painterResource(tab.icon),
                contentDescription = null,
                tint = if (selected) {
                    TnTTheme.colors.neutralColors.Neutral800
                } else {
                    TnTTheme.colors.neutralColors.Neutral300
                },
            )
            Text(
                text = tab.contentDescription,
                style = TnTTheme.typography.label2Medium,
                fontSize = 12.sp,
                color = if (selected) {
                    TnTTheme.colors.neutralColors.Neutral800
                } else {
                    TnTTheme.colors.neutralColors.Neutral400
                },
                modifier = Modifier.padding(top = 6.dp),
            )
        }
    }
}

@Preview
@Composable
private fun TraineeMainBottomPreview() {
    TnTTheme {
        TraineeMainBottomBar(
            onClickTab = {},
            navController = rememberNavController(),
        )
    }
}
