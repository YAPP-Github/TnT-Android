package co.kr.tnt.trainee.connect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectPage
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectSideEffect
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectUiEvent
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectUiState
import co.kr.tnt.trainee.connect.model.PTSessionFormData

@Composable
internal fun TraineeConnectRoute(
    navigateToPrevious: () -> Unit,
    navigateToHome: (Boolean) -> Unit,
    viewModel: TraineeConnectViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    TraineeConnectScreen(
        state = state,
        onFormNextClick = { formData ->
            viewModel.setEvent(TraineeConnectUiEvent.UpdatePTSessionData(formData))
        },
        onBackClick = { viewModel.setEvent(TraineeConnectUiEvent.OnBackClick) },
        onNextClick = { viewModel.setEvent(TraineeConnectUiEvent.OnNextClick) },
        onSkipClick = { viewModel.setEvent(TraineeConnectUiEvent.OnSkipClick) },
        onCodeChanged = { viewModel.setEvent(TraineeConnectUiEvent.OnCodeChanged) },
        onCodeValidationClick = { code ->
            viewModel.setEvent(TraineeConnectUiEvent.OnCodeValidateClick(code))
        },
    )

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                TraineeConnectSideEffect.NavigateToBack -> navigateToPrevious()
                TraineeConnectSideEffect.NavigateToHome -> navigateToHome(false)
                else -> {}
            }
        }
    }
}

@Composable
private fun TraineeConnectScreen(
    state: TraineeConnectUiState,
    onCodeValidationClick: (String) -> Unit,
    onCodeChanged: () -> Unit,
    onFormNextClick: (PTSessionFormData) -> Unit,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    onSkipClick: () -> Unit,
) {
    when (state.page) {
        TraineeConnectPage.CodeEntry -> CodeEntryPage(
            state = state,
            onNextClick = onNextClick,
            onSkipClick = onSkipClick,
            onCodeChanged = onCodeChanged,
            onValidateClick = { code ->
                onCodeValidationClick(code)
            },
        )

        TraineeConnectPage.PTSessionForm -> PTSessionFormPage(
            state = state,
            onNextClick = { formData ->
                onFormNextClick(formData)
            },
            onBackClick = onBackClick,
        )

        TraineeConnectPage.TraineeConnectComplete -> TraineeConnectCompletePage(
            state = state,
            onNextClick = onNextClick,
            onBackClick = onBackClick,
        )
    }
}
