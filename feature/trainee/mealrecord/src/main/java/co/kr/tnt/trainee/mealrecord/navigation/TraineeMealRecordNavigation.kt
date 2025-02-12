package co.kr.tnt.trainee.mealrecord.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainee.mealrecord.TraineeMealRecordDetailRoute
import co.kr.tnt.trainee.mealrecord.TraineeMealRecordRoute

fun NavController.navigateToTraineeMealRecord(
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.TraineeMealRecord,
    builder = navOptions,
)

fun NavController.navigateToTraineeMealRecordDetail(
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.TraineeMealRecordDetail,
    builder = navOptions,
)

fun NavGraphBuilder.traineeMealRecordScreen(
    navigateToPrevious: () -> Unit,
) {
    composable<Route.TraineeMealRecord> { backstackEntry ->
        backstackEntry.toRoute<Route.TraineeMealRecord>().apply {
            TraineeMealRecordRoute(
                navigateToPrevious = navigateToPrevious,
            )
        }
    }

    composable<Route.TraineeMealRecordDetail> { backstackEntry ->
        backstackEntry.toRoute<Route.TraineeMealRecordDetail>().apply {
            TraineeMealRecordDetailRoute(
                navigateToPrevious = navigateToPrevious,
            )
        }
    }
}
