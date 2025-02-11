package co.kr.tnt.trainee.mealrecord

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
@Suppress("UnusedParameter")
internal fun TraineeMealRecordRoute(
    navigateToPrevious: () -> Unit,
    viewModel: TraineeMealRecordViewModel = hiltViewModel(),
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
