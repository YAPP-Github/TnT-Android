package co.kr.tnt.trainee.mypage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.trainee.mypage.TraineeMyPageContract.TraineeMyPageUiState

@Composable
internal fun TraineeMyPageRoute(
    navigateToPrevious: () -> Unit,
    viewModel: TraineeMyPageViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TraineeMyPageScreen(
        state = uiState,
    )
}

@Composable
private fun TraineeMyPageScreen(
    state: TraineeMyPageUiState,
) {
    Scaffold(containerColor = TnTTheme.colors.commonColors.Common0) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(innerPadding),
        ) {
        }
    }
}

@Preview
@Composable
private fun TraineeMyPagePreview() {
    TnTTheme {
        TraineeMyPageScreen(
            state = TraineeMyPageUiState(),
        )
    }
}
