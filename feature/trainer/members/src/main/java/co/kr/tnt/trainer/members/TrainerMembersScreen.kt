package co.kr.tnt.trainer.members

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
@Suppress("UnusedParameter")
internal fun TrainerMembersRoute(
    viewModel: TrainerMembersViewModel = hiltViewModel(),
) {
    TrainerMembersScreen()
}

@Composable
private fun TrainerMembersScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Text(
            text = "trainer members",
            modifier = Modifier.padding(innerPadding),
        )
    }
}
