package co.kr.tnt.trainer.signup

import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TrainerSignUpScreen(
        state = uiState,
        onNameChange = { viewModel.setEvent(TrainerSignUpUiEvent.OnNameChange(it)) },
        onProfileImageSelect = { viewModel.setEvent(TrainerSignUpUiEvent.OnImageChange(it)) },
        onNextClick = { viewModel.setEvent(TrainerSignUpUiEvent.OnNextClick) },
        onBackClick = { viewModel.setEvent(TrainerSignUpUiEvent.OnBackClick) },
        onSubmitSignUp = { uri ->
            viewModel.setEvent(
                TrainerSignUpUiEvent.RequestSignUp(
                    context = context,
                    imageUri = uri,
                    id = authId,
                    email = email,
                    authType = authType,
                ),
            )
        },
    )

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                TrainerSignUpContract.TrainerSignUpEffect.NavigateToBack -> navigateToPrevious()
                TrainerSignUpContract.TrainerSignUpEffect.NavigateToConnect -> navigateToConnect()
                is TrainerSignUpContract.TrainerSignUpEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@Composable
private fun TrainerSignUpScreen(
    state: TrainerSignUpUiState,
    onProfileImageSelect: (Uri) -> Unit,
    onNameChange: (String) -> Unit,
    onSubmitSignUp: (Uri?) -> Unit,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    when (state.page) {
        TrainerSignUpContract.TrainerSignUpPage.ProfileSetUp -> TrainerProfileSetupPage(
            state = state,
            onProfileImageSelect = onProfileImageSelect,
            onNameChange = onNameChange,
            onNextClick = onNextClick,
            onBackClick = onBackClick,
        )

        TrainerSignUpContract.TrainerSignUpPage.SignUpComplete -> TrainerSignUpCompletePage(
            state = state,
            onNextClick = onSubmitSignUp,
            onBackClick = onBackClick,
        )
    }
}
