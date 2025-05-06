package co.kr.tnt.trainer.connect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.designsystem.snackbar.LocalSnackbar
import co.kr.tnt.trainer.connect.TrainerConnectContract.TrainerConnectPage
import co.kr.tnt.trainer.connect.TrainerConnectContract.TrainerConnectSideEffect
import co.kr.tnt.trainer.connect.TrainerConnectContract.TrainerConnectUiEvent
import co.kr.tnt.trainer.connect.TrainerConnectContract.TrainerConnectUiState

@Composable
internal fun TrainerConnectRoute(
    trainerId: String,
    traineeId: String,
    navigateToPrevious: () -> Unit,
    navigateToHome: (Boolean) -> Unit,
    viewModel: TrainerConnectViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val snackbar = LocalSnackbar.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(trainerId) {
        viewModel.setEvent(TrainerConnectUiEvent.OnFetchInitialData(trainerId, traineeId))
    }

    TrainerConnectScreen(
        state = state,
        onBackClick = { viewModel.setEvent(TrainerConnectUiEvent.OnBackClick) },
        onNextClick = { viewModel.setEvent(TrainerConnectUiEvent.OnNextClick) },
    )

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                TrainerConnectSideEffect.NavigateToBack -> navigateToPrevious()
                TrainerConnectSideEffect.NavigateToHome -> navigateToHome(true)
                is TrainerConnectSideEffect.ShowToast -> snackbar.show(effect.message.asString(context))
            }
        }
    }
}

@Composable
private fun TrainerConnectScreen(
    state: TrainerConnectUiState,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    when (state.page) {
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
