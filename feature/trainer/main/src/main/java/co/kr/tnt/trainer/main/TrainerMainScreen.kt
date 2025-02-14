package co.kr.tnt.trainer.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import co.kr.tnt.designsystem.component.bottombar.TnTBottomBar
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.trainer.addptsession.navigation.addPtSession
import co.kr.tnt.trainer.addptsession.navigation.navigateToAddPtSession
import co.kr.tnt.trainer.feedback.navigation.trainerFeedbackNavGraph
import co.kr.tnt.trainer.home.navigation.trainerHomeNavGraph
import co.kr.tnt.trainer.members.navigation.trainerMembersNavGraph
import co.kr.tnt.trainer.mypage.navigation.trainerMyPageNavGraph
import co.kr.tnt.trainer.notification.navigation.navigateToTrainerNotification
import co.kr.tnt.trainer.notification.navigation.trainerNotification
import co.kr.tnt.ui.extensions.safePopBackStack

@Composable
internal fun TrainerMainRoute(
    navigateToConnect: (trainerId: String, traineeId: String) -> Unit,
    navigateToInvite: (Boolean) -> Unit,
    navigateToLogin: () -> Unit,
    navigateToWebView: (url: String) -> Unit,
) {
    val state = rememberTrainerMainState(
        startDestination = TrainerMainTab.HOME.baseRoute,
    )

    TrainerMainScreen(
        state = state,
        navigateToConnect = navigateToConnect,
        navigateToInvite = navigateToInvite,
        navigateToLogin = navigateToLogin,
        navigateToWebView = navigateToWebView,
    )
}

@Composable
private fun TrainerMainScreen(
    state: TrainerMainState,
    navigateToConnect: (trainerId: String, traineeId: String) -> Unit,
    navigateToInvite: (Boolean) -> Unit,
    navigateToLogin: () -> Unit,
    navigateToWebView: (url: String) -> Unit,
) {
    val navController = state.navController

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = state.currentMainTab?.containerColor?.invoke() ?: TnTTheme.colors.commonColors.Common0,
        bottomBar = {
            TnTBottomBar(
                modifier = Modifier.navigationBarsPadding(),
                bottomTabs = state.mainTabs,
                isVisible = state.shouldShowBottomBar,
                currentTab = state.currentMainTab,
                onClickTab = state::navigateMainTab,
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = state.startDestination,
        ) {
            trainerHomeNavGraph(
                padding = innerPadding,
                navigateToNotification = navController::navigateToTrainerNotification,
                navigateToAddPtSession = navController::navigateToAddPtSession,
            ) {
                trainerNotification(
                    navigateToPrevious = navController::safePopBackStack,
                    navigateToConnect = navigateToConnect,
                )
                addPtSession(
                    navigateToPrevious = navController::safePopBackStack,
                )
            }
            trainerFeedbackNavGraph(padding = innerPadding)
            trainerMembersNavGraph(
                padding = innerPadding,
                navigateToInvite = navigateToInvite,
            )
            trainerMyPageNavGraph(
                padding = innerPadding,
                navigateToLogin = navigateToLogin,
                navigateToWebView = navigateToWebView,
            )
        }
    }
}
