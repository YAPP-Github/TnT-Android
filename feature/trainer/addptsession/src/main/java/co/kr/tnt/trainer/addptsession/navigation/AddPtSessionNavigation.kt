package co.kr.tnt.trainer.addptsession.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainer.addptsession.AddPtSessionRoute

fun NavController.navigateToAddPtSession(
    selectedDate: String,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.AddPtSession(selectedDate),
    builder = navOptions,
)

fun NavGraphBuilder.addPtSession(
    navigateToPrevious: () -> Unit,
) {
    composable<Route.AddPtSession> { backstackEntry ->
        backstackEntry.toRoute<Route.AddPtSession>().apply {
            AddPtSessionRoute(
                selectedDate = selectedDate,
                navigateToPrevious = navigateToPrevious,
            )
        }
    }
}
