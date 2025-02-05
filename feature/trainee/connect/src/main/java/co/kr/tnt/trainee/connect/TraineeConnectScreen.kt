package co.kr.tnt.trainee.connect

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectPage
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectSideEffect
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectUiEvent
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectUiState
import co.kr.tnt.trainee.connect.model.FormData

@Composable
internal fun TraineeConnectRoute(
    isSkippable: Boolean,
    navigateToPrevious: () -> Unit,
    navigateToHome: (Boolean) -> Unit,
    viewModel: TraineeConnectViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    TraineeConnectScreen(
        state = state,
        isSkippable = isSkippable,
        onBackClick = { viewModel.setEvent(TraineeConnectUiEvent.OnBackClick) },
        onNextClick = { data ->
            viewModel.setEvent(TraineeConnectUiEvent.OnNextClick(data))
        },
        onSkipClick = { viewModel.setEvent(TraineeConnectUiEvent.OnSkipClick) },
        onCodeChanged = { code ->
            viewModel.setEvent(TraineeConnectUiEvent.OnCodeChanged(code))
        },
        onCodeValidationClick = { code ->
            viewModel.setEvent(TraineeConnectUiEvent.OnCodeValidateClick(code))
        },
    )

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                TraineeConnectSideEffect.NavigateToBack -> navigateToPrevious()
                TraineeConnectSideEffect.NavigateToHome -> navigateToHome(false)
                is TraineeConnectSideEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@Composable
private fun TraineeConnectScreen(
    state: TraineeConnectUiState,
    isSkippable: Boolean,
    onCodeValidationClick: (String) -> Unit,
    onCodeChanged: (String) -> Unit,
    onNextClick: (FormData?) -> Unit,
    onBackClick: () -> Unit,
    onSkipClick: () -> Unit,
) {
    when (state.page) {
        TraineeConnectPage.CodeEntry -> CodeEntryPage(
            state = state,
            isSkippable = isSkippable,
            onNextClick = onNextClick,
            onBackClick = onBackClick,
            onSkipClick = onSkipClick,
            onCodeChanged = { code ->
                onCodeChanged(code)
            },
            onValidateClick = { code ->
                onCodeValidationClick(code)
            },
        )

        TraineeConnectPage.PTSessionForm -> PTSessionFormPage(
            state = state,
            onNextClick = onNextClick,
            onBackClick = onBackClick,
        )

        TraineeConnectPage.TraineeConnectComplete -> TraineeConnectCompletePage(
            state = state,
            onNextClick = onNextClick,
            onBackClick = onBackClick,
        )
    }
}
