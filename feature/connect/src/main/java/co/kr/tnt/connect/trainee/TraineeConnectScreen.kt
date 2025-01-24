package co.kr.tnt.connect.trainee

import TraineeConnectContract.TraineeConnectPage
import TraineeConnectContract.TraineeConnectSideEffect
import TraineeConnectContract.TraineeConnectUiEvent
import TraineeConnectContract.TraineeConnectUiState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.connect.ConnectCompleteScreen
import co.kr.tnt.domain.model.UserType

@Composable
internal fun TraineeConnectRoute(
    navigateToPrevious: () -> Unit,
    navigateToHome: (Boolean) -> Unit,
    viewModel: TraineeConnectViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    TraineeConnectScreen(
        state = state,
        onBackClick = { viewModel.setEvent(TraineeConnectUiEvent.OnBackClick) },
        onNextClick = { viewModel.setEvent(TraineeConnectUiEvent.OnNextClick) },
        onSkipClick = { viewModel.setEvent(TraineeConnectUiEvent.OnSkipClick) },
    )

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                TraineeConnectSideEffect.NavigateToBack -> navigateToPrevious()
                TraineeConnectSideEffect.NavigateToHome -> navigateToHome(false)
            }
        }
    }
}

@Composable
private fun TraineeConnectScreen(
    state: TraineeConnectUiState,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    onSkipClick: () -> Unit,
) {
    when (state.page) {
        TraineeConnectPage.CodeEntry -> CodeEntryScreen(
            onNextClick = onNextClick,
            onSkipClick = onSkipClick,
        )

        TraineeConnectPage.PTSessionForm -> PTSessionFormScreen(
            onNextClick = onNextClick,
            onBackClick = onBackClick,
        )

        TraineeConnectPage.TraineeConnectComplete -> ConnectCompleteScreen(
            userType = UserType.Trainee,
            onNextClick = onNextClick,
            onBackClick = onBackClick,
        )
    }
}
