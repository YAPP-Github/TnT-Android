package co.kr.tnt.trainee.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import co.kr.tnt.designsystem.component.bottombar.TnTBottomBar
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.trainee.home.navigation.traineeHomeNavGraph
import co.kr.tnt.trainee.mypage.navigation.traineeMyPageNavGraph
import co.kr.tnt.trainee.notification.navigation.navigateToTraineeNotification
import co.kr.tnt.trainee.notification.navigation.traineeNotification

@Composable
internal fun TraineeMainRoute(
    navigateToConnect: (Boolean) -> Unit,
    navigateToLogin: () -> Unit,
    navigateToWebView: (url: String) -> Unit,
    navigateToMealRecord: () -> Unit,
    navigateToMealDetail: (id: Long) -> Unit,
) {
    val state = rememberTraineeMainState(
        startDestination = TraineeMainTab.HOME.baseRoute,
    )

    TraineeMainScreen(
        state = state,
        navigateToConnect = navigateToConnect,
        navigateToLogin = navigateToLogin,
        navigateToWebView = navigateToWebView,
        navigateToMealRecord = navigateToMealRecord,
        navigateToMealDetail = navigateToMealDetail,
    )
}

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
private fun TraineeMainScreen(
    state: TraineeMainState,
    navigateToConnect: (Boolean) -> Unit,
    navigateToLogin: () -> Unit,
    navigateToWebView: (url: String) -> Unit,
    navigateToMealRecord: () -> Unit,
    navigateToMealDetail: (id: Long) -> Unit,
) {
    val navController = state.navController

    Scaffold(
        containerColor = TnTTheme.colors.commonColors.Common0,
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (state.shouldShowBottomBar) {
                TnTBottomBar(
                    modifier = Modifier.navigationBarsPadding(),
                    bottomTabs = state.mainTabs,
                    currentTab = state.currentMainTab,
                    onClickTab = state::navigateMainTab,
                )
            }
        },
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = state.startDestination,
        ) {
            traineeHomeNavGraph(
                navigateToNotification = navController::navigateToTraineeNotification,
                navigateToMealRecord = navigateToMealRecord,
                navigateToMealDetail = navigateToMealDetail,
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
