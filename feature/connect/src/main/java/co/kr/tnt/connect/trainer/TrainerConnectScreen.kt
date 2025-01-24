package co.kr.tnt.connect.trainer

import TrainerConnectContract.TrainerConnectPage
import TrainerConnectContract.TrainerConnectSideEffect
import TrainerConnectContract.TrainerConnectUiEvent
import TrainerConnectContract.TrainerConnectUiState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
internal fun TrainerConnectRoute(
    navigateToPrevious: () -> Unit,
    navigateToHome: (Boolean) -> Unit,
    viewModel: TrainerConnectViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    TrainerConnectScreen(
        state = state,
        onRegenerateClick = { viewModel.setEvent(TrainerConnectUiEvent.OnRegenerateClick) },
        onBackClick = { viewModel.setEvent(TrainerConnectUiEvent.OnBackClick) },
        onNextClick = { viewModel.setEvent(TrainerConnectUiEvent.OnNextClick) },
        onSkipClick = { viewModel.setEvent(TrainerConnectUiEvent.OnSkipClick) },
    )

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                TrainerConnectSideEffect.NavigateToBack -> navigateToPrevious()
                TrainerConnectSideEffect.NavigateToHome -> navigateToHome(true)
            }
        }
    }
}

@Composable
private fun TrainerConnectScreen(
    state: TrainerConnectUiState,
    onRegenerateClick: () -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    onSkipClick: () -> Unit,
) {
    when (state.page) {
        TrainerConnectPage.CodeGeneration -> CodeGenerationScreen(
            onRegenerateClick = onRegenerateClick,
            onBackClick = onBackClick,
            onSkipClick = onSkipClick,
        )

        TrainerConnectPage.TrainerConnectComplete -> TrainerConnectCompleteScreen(
            state = state,
            onBackClick = onBackClick,
            onNextClick = onNextClick,
        )

        TrainerConnectPage.TraineeProfile -> TraineeProfileScreen(
            state = state,
            onNextClick = onNextClick,
        )
    }
}
