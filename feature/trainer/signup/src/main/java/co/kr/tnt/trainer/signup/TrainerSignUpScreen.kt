package co.kr.tnt.trainer.signup

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.trainer.signup.TrainerSignUpContract.TrainerSignUpUiEvent
import co.kr.tnt.trainer.signup.TrainerSignUpContract.TrainerSignUpUiState

@Composable
internal fun TrainerSignUpRoute(
    authId: String,
    authType: String,
    email: String,
    navigateToPrevious: () -> Unit,
    navigateToConnect: () -> Unit,
    viewModel: TrainerSignUpViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TrainerSignUpScreen(
        state = uiState,
        onNameChanged = { viewModel.setEvent(TrainerSignUpUiEvent.OnNameChanged(it)) },
        onProfileImageSelected = { viewModel.setEvent(TrainerSignUpUiEvent.OnImagePicked(it)) },
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
    onProfileImageSelected: (Uri) -> Unit,
    onNameChanged: (String) -> Unit,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    when (state.page) {
        TrainerSignUpContract.TrainerSignUpPage.ProfileSetUp -> TrainerProfileSetupPage(
            state = state,
            onProfileImageSelected = onProfileImageSelected,
            onNameChanged = onNameChanged,
            onNextClick = onNextClick,
            onBackClick = onBackClick,
        )
        TrainerSignUpContract.TrainerSignUpPage.SignUpComplete -> TrainerSignUpCompletePage(
            state = state,
            onNextClick = onNextClick,
            onBackClick = onBackClick,
        )
    }
}
