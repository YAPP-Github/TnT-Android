package co.kr.tnt.trainee.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.trainee.signup.TraineeSignUpContract.TraineeSignUpEffect
import co.kr.tnt.trainee.signup.TraineeSignUpContract.TraineeSignUpPage
import co.kr.tnt.trainee.signup.TraineeSignUpContract.TraineeSignUpUiEvent
import co.kr.tnt.trainee.signup.TraineeSignUpContract.TraineeSignUpUiState

@Composable
internal fun TraineeSignUpRoute(
    navigateToPrevious: () -> Unit,
    navigateToConnect: () -> Unit,
    viewModel: TraineeSignUpViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TraineeSignUpScreen(
        state = uiState,
        onBackClick = { viewModel.setEvent(TraineeSignUpUiEvent.OnBackClick) },
        onNextClick = { viewModel.setEvent(TraineeSignUpUiEvent.OnNextClick) },
    )

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                TraineeSignUpEffect.NavigateToBack -> navigateToPrevious()
                TraineeSignUpEffect.NavigateToConnect -> navigateToConnect()
            }
        }
    }
}

@Composable
private fun TraineeSignUpScreen(
    state: TraineeSignUpUiState,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    when (state.page) {
        TraineeSignUpPage.ProfileSetUp -> TraineeProfileSetupPage(
            onBackClick = onBackClick,
            onNextClick = onNextClick,
        )
        TraineeSignUpPage.BasicInfo -> TraineeBasicInfoPage(
            onBackClick = onBackClick,
            onNextClick = onNextClick,
        )
        TraineeSignUpPage.NoteForTrainer -> TraineeNoteForTrainerPage(
            onBackClick = onBackClick,
            onNextClick = onNextClick,
        )
        TraineeSignUpPage.PTPurpose -> TraineePTPurposePage(
            onBackClick = onBackClick,
            onNextClick = onNextClick,
        )
        TraineeSignUpPage.SignUpComplete -> TraineeSignUpCompletePage(
            onBackClick = onBackClick,
            onNextClick = onNextClick,
        )
    }
}
