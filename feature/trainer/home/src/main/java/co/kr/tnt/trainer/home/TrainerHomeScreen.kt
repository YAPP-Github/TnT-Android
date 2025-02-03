package co.kr.tnt.trainer.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
@Suppress("UnusedParameter")
internal fun TrainerHomeRoute(
    viewModel: TrainerHomeViewModel = hiltViewModel(),
) {
    TrainerHomeScreen()
}

@Composable
private fun TrainerHomeScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Text(
            text = "trainer home",
            modifier = Modifier.padding(innerPadding),
        )
    }
}
