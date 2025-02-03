package co.kr.tnt.trainer.feedback

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
@Suppress("UnusedParameter")
internal fun TrainerFeedbackRoute(
    viewModel: TrainerFeedbackViewModel = hiltViewModel(),
) {
    TrainerFeedbackScreen()
}

@Composable
private fun TrainerFeedbackScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Text(
            text = "trainer feedback",
            modifier = Modifier.padding(innerPadding),
        )
    }
}
