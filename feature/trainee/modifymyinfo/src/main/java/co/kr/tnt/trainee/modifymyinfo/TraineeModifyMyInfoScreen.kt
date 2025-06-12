package co.kr.tnt.trainee.modifymyinfo

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.trainee.modifymyinfo.TraineeModifyMyInfoContract.TraineeModifyMyInfoUiState

@Composable
internal fun TraineeModifyMyInfoRoute(
    viewModel: TraineeModifyMyInfoViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    TraineeModifyMyInfoScreen(
        state = state,
    )
}

@Composable
private fun TraineeModifyMyInfoScreen(
    state: TraineeModifyMyInfoUiState,
) {
    Scaffold { padding ->
        Text(
            "Trainee modify my info",
            modifier = Modifier.padding(padding),
        )
    }
}
