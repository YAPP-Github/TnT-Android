package co.kr.tnt.trainee.mealrecord.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import co.kr.tnt.navigation.Route
import co.kr.tnt.trainee.mealrecord.record.TraineeMealRecordRoute

fun NavController.navigateToTraineeMealRecord(
    selectedDate: String,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) = navigate(
    route = Route.TraineeMealRecord(selectedDate),
    builder = navOptions,
)

fun NavGraphBuilder.traineeMealRecord(
    navigateToPrevious: () -> Unit,
) {
    composable<Route.TraineeMealRecord> { backstackEntry ->
        backstackEntry.toRoute<Route.TraineeMealRecord>().apply {
            TraineeMealRecordRoute(
                selectedDate = selectedDate,
                navigateToPrevious = navigateToPrevious,
            )
        }
    }
}
