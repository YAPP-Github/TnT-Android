package co.kr.tnt.trainee.mealrecord

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
@Suppress("UnusedParameter")
internal fun TraineeMealRecordRoute(
    viewModel: TraineeMealRecordViewModel,
) {
    TraineeMealRecordScreen()
}

@Composable
private fun TraineeMealRecordScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Text("Trainee Record")
    }
}
