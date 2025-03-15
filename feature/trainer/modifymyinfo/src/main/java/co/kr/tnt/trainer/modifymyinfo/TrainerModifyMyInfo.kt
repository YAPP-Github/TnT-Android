package co.kr.tnt.trainer.modifymyinfo

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.trainer.modifymyinfo.TrainerModifyMyInfoContract.TrainerModifyMyInfoUiState

@Composable
internal fun TrainerModifyMyInfoRoute(
    viewModel: TrainerModifyMyInfoViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    TrainerModifyMyInfoScreen(
        state = state,
    )
}

@Composable
private fun TrainerModifyMyInfoScreen(
    state: TrainerModifyMyInfoUiState,
) {
    Scaffold { padding ->
        Text(
            "Trainer modify my info",
            modifier = Modifier.padding(padding),
        )
    }
}
