package co.kr.tnt.trainee.mealdetail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainee.mealdetail.TraineeMealDetailRoute

fun NavController.navigateToTraineeMealDetail(
    id: Long,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.TraineeMealDetail(id),
    builder = navOptions,
)

fun NavGraphBuilder.traineeMealDetail(
    navigateToPrevious: () -> Unit,
) {
    composable<Route.TraineeMealDetail> { backstackEntry ->
        backstackEntry.toRoute<Route.TraineeMealDetail>().apply {
            TraineeMealDetailRoute(
                mealId = id,
                navigateToPrevious = navigateToPrevious,
            )
        }
    }
}
