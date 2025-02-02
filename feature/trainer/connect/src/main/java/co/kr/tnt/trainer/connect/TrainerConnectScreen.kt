package co.kr.tnt.trainer.connect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.trainer.connect.TrainerConnectContract.TrainerConnectPage
import co.kr.tnt.trainer.connect.TrainerConnectContract.TrainerConnectSideEffect
import co.kr.tnt.trainer.connect.TrainerConnectContract.TrainerConnectUiEvent
import co.kr.tnt.trainer.connect.TrainerConnectContract.TrainerConnectUiState

@Composable
internal fun TrainerConnectRoute(
    isSkippable: Boolean,
    isCompleted: Boolean,
    navigateToPrevious: () -> Unit,
    navigateToHome: (Boolean) -> Unit,
    viewModel: TrainerConnectViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        val startPage = if (isCompleted) {
            TrainerConnectContract.TrainerConnectPage.TrainerConnectComplete
        } else {
            TrainerConnectContract.TrainerConnectPage.CodeGeneration
        }
        viewModel.setStartPage(startPage)
    }

    TrainerConnectScreen(
        state = state,
        isSkippable = isSkippable,
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
    isSkippable: Boolean,
    onRegenerateClick: () -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    onSkipClick: () -> Unit,
) {
    when (state.page) {
        TrainerConnectPage.CodeGeneration -> CodeGenerationPage(
            state = state,
            isSkippable = isSkippable,
            onRegenerateClick = onRegenerateClick,
            onBackClick = onBackClick,
            onSkipClick = onSkipClick,
        )

        TrainerConnectPage.TrainerConnectComplete -> TrainerConnectCompletePage(
            state = state,
            onBackClick = onBackClick,
            onNextClick = onNextClick,
        )

        TrainerConnectPage.TraineeProfile -> TraineeProfilePage(
            state = state,
            onNextClick = onNextClick,
            onBackClick = onBackClick,
        )
    }
}
