package co.kr.tnt.trainer.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.trainer.signup.TrainerSignUpContract.TrainerSignUpUiEvent
import co.kr.tnt.trainer.signup.TrainerSignUpContract.TrainerSignUpUiState

@Composable
internal fun TrainerSignUpRoute(
    navigateToPrevious: () -> Unit,
    navigateToConnect: () -> Unit,
    viewModel: TrainerSignUpViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TrainerSignUpScreen(
        state = uiState,
        onNextClick = { viewModel.setEvent(TrainerSignUpUiEvent.OnNextClick) },
        onBackClick = { viewModel.setEvent(TrainerSignUpUiEvent.OnBackClick) },
    )

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                TrainerSignUpContract.TrainerSignUpEffect.NavigateToBack -> navigateToPrevious()
                TrainerSignUpContract.TrainerSignUpEffect.NavigateToConnect -> navigateToConnect()
            }
        }
    }
}

@Composable
private fun TrainerSignUpScreen(
    state: TrainerSignUpUiState,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    when (state.page) {
        TrainerSignUpContract.TrainerSignUpPage.ProfileSetUp -> TrainerProfileSetupScreen(
            onNextClick = onNextClick,
            onBackClick = onBackClick,
        )
        TrainerSignUpContract.TrainerSignUpPage.SignUpComplete -> TrainerSignUpCompleteScreen(
            onNextClick = onNextClick,
            onBackClick = onBackClick,
        )
    }
}
