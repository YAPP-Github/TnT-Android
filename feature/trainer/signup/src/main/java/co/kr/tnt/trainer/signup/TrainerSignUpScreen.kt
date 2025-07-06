package co.kr.tnt.trainer.signup

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.designsystem.snackbar.LocalSnackbar
import co.kr.tnt.navigation.model.ScreenMode
import co.kr.tnt.trainer.signup.TrainerSignUpContract.TrainerSignUpUiEvent
import co.kr.tnt.trainer.signup.TrainerSignUpContract.TrainerSignUpUiState
import java.io.File

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
    val snackbar = LocalSnackbar.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TrainerSignUpScreen(
        state = uiState,
        onNameChange = { viewModel.setEvent(TrainerSignUpUiEvent.OnNameChange(it)) },
        onProfileImageSelect = { viewModel.setEvent(TrainerSignUpUiEvent.OnImageChange(it)) },
        onNextClick = { viewModel.setEvent(TrainerSignUpUiEvent.OnNextClick) },
        onBackClick = { viewModel.setEvent(TrainerSignUpUiEvent.OnBackClick) },
        onSubmitSignUp = { file ->
            viewModel.setEvent(
                TrainerSignUpUiEvent.RequestSignUp(
                    imageFile = file,
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
    onProfileImageSelect: (imageUri: Uri) -> Unit,
    onNameChange: (name: String) -> Unit,
    onSubmitSignUp: (imageFile: File?) -> Unit,
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
