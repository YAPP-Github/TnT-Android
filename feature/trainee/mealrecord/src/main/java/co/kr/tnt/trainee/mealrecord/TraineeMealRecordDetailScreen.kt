package co.kr.tnt.trainee.mealrecord

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
@Suppress("UnusedParameter")
internal fun TraineeMealRecordDetailRoute(
    navigateToPrevious: () -> Unit,
) {
    TraineeMealRecordDetailScreen()
}

@Composable
private fun TraineeMealRecordDetailScreen() {
    Column {
        Text(
            text = "record detail",
        )
    }
}
