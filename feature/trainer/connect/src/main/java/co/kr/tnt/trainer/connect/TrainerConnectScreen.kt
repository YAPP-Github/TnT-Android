package co.kr.tnt.trainer.connect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
    val snackbar = LocalSnackbar.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(trainerId) {
        viewModel.setEvent(TrainerConnectUiEvent.OnFetchInitialData(trainerId, traineeId))
    }

    TrainerConnectScreen(
        state = state,
        onClickBack = { viewModel.setEvent(TrainerConnectUiEvent.OnClickBack) },
        onClickNext = { viewModel.setEvent(TrainerConnectUiEvent.OnClickNext) },
    )

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                TrainerConnectSideEffect.NavigateToBack -> navigateToPrevious()
                TrainerConnectSideEffect.NavigateToHome -> navigateToHome(true)
                is TrainerConnectSideEffect.ShowToast -> snackbar.show(effect.message)
            }
        }
    }
}

@Composable
private fun TrainerConnectScreen(
    state: TrainerConnectUiState,
    onClickBack: () -> Unit,
    onClickNext: () -> Unit,
) {
    when (state.page) {
        TrainerConnectPage.TrainerConnectComplete -> TrainerConnectCompletePage(
            state = state,
            onClickBack = onClickBack,
            onClickNext = onClickNext,
        )

        TrainerConnectPage.TraineeProfile -> TraineeProfilePage(
            state = state,
            onClickBack = onClickBack,
            onClickNext = onClickNext,
        )
    }
}
