package co.kr.tnt.trainer.signup

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.designsystem.snackbar.LocalSnackbar
import co.kr.tnt.navigation.model.ScreenMode
import co.kr.tnt.trainer.signup.TrainerSignUpContract.TrainerSignUpUiEvent
import co.kr.tnt.trainer.signup.TrainerSignUpContract.TrainerSignUpUiState

@Composable
internal fun TrainerSignUpRoute(
    authId: String,
    authType: String,
    email: String,
    messagingToken: String,
    navigateToPrevious: () -> Unit,
    navigateToInvite: (ScreenMode) -> Unit,
    viewModel: TrainerSignUpViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val snackbar = LocalSnackbar.current
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
                    messagingToken = messagingToken,
                ),
            )
        },
    )

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                TrainerSignUpContract.TrainerSignUpEffect.NavigateToBack -> navigateToPrevious()
                TrainerSignUpContract.TrainerSignUpEffect.NavigateToConnect -> navigateToInvite(ScreenMode.SKIP)
                is TrainerSignUpContract.TrainerSignUpEffect.ShowToast -> snackbar.show(
                    effect.message.asString(context),
                )
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
