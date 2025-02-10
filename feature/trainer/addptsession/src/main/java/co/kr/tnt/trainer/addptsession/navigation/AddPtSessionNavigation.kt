package co.kr.tnt.trainer.addptsession.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainer.addptsession.AddPtSessionRoute

fun NavController.navigateToAddPtSession(
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.AddPtSession,
    builder = navOptions,
)

fun NavGraphBuilder.addPtSession() {
    composable<Route.AddPtSession> {
        AddPtSessionRoute()
    }
}
