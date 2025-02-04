package co.kr.tnt.trainee.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
@Suppress("UnusedParameter")
internal fun TraineeHomeRoute(
    viewModel: TraineeHomeViewModel = hiltViewModel(),
) {
    TraineeHomeScreen()
}

@Composable
fun TraineeHomeScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Text(
            text = "trainee home",
            modifier = Modifier.padding(innerPadding),
        )
    }
}
