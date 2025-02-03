package co.kr.tnt.trainer.mypage

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
@Suppress("UnusedParameter")
internal fun TrainerMyPageRoute(
    viewModel: TrainerMyPageViewModel = hiltViewModel(),
) {
    TrainerMyPageScreen()
}

@Composable
private fun TrainerMyPageScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Text(
            text = "trainer my page",
            modifier = Modifier.padding(innerPadding),
        )
    }
}
