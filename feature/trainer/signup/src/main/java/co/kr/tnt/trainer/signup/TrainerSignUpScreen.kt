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
        onChangeName = { viewModel.setEvent(TrainerSignUpUiEvent.OnChangeName(it)) },
        onSelectProfileImage = { viewModel.setEvent(TrainerSignUpUiEvent.OnChangeImage(it)) },
        onClickNext = { viewModel.setEvent(TrainerSignUpUiEvent.OnClickNext) },
        onClickBack = { viewModel.setEvent(TrainerSignUpUiEvent.OnClickBack) },
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
                is TrainerSignUpContract.TrainerSignUpEffect.ShowToast -> snackbar.show(effect.message)
            }
        }
    }
}

@Composable
private fun TrainerSignUpScreen(
    state: TrainerSignUpUiState,
    onSelectProfileImage: (Uri) -> Unit,
    onChangeName: (String) -> Unit,
    onSubmitSignUp: (Uri?) -> Unit,
    onClickNext: () -> Unit,
    onClickBack: () -> Unit,
) {
    when (state.page) {
        TrainerSignUpContract.TrainerSignUpPage.ProfileSetUp -> TrainerProfileSetupPage(
            state = state,
            onSelectProfileImage = onSelectProfileImage,
            onChangeName = onChangeName,
            onClickNext = onClickNext,
            onClickBack = onClickBack,
        )

        TrainerSignUpContract.TrainerSignUpPage.SignUpComplete -> TrainerSignUpCompletePage(
            state = state,
            onClickNext = onSubmitSignUp,
            onClickBack = onClickBack,
        )
    }
}
