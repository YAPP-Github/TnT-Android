package co.kr.tnt.trainee.signup

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.designsystem.snackbar.LocalSnackbar
import co.kr.tnt.trainee.signup.TraineeSignUpContract.TraineeSignUpEffect
import co.kr.tnt.trainee.signup.TraineeSignUpContract.TraineeSignUpPage
import co.kr.tnt.trainee.signup.TraineeSignUpContract.TraineeSignUpUiEvent
import co.kr.tnt.trainee.signup.TraineeSignUpContract.TraineeSignUpUiState
import java.io.File
import java.time.LocalDate

@Composable
internal fun TraineeSignUpRoute(
    authId: String,
    authType: String,
    email: String,
    messagingToken: String,
    navigateToPrevious: () -> Unit,
    navigateToConnect: () -> Unit,
    viewModel: TraineeSignUpViewModel = hiltViewModel(),
) {
    val snackbar = LocalSnackbar.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TraineeSignUpScreen(
        state = uiState,
        onNameChange = { viewModel.setEvent(TraineeSignUpUiEvent.OnNameChange(it)) },
        onProfileImageSelect = { viewModel.setEvent(TraineeSignUpUiEvent.OnImageChange(it)) },
        onHeightChange = { viewModel.setEvent(TraineeSignUpUiEvent.OnHeightChange(it)) },
        onWeightChange = { viewModel.setEvent(TraineeSignUpUiEvent.OnWeightChange(it)) },
        onBirthdayChange = { viewModel.setEvent(TraineeSignUpUiEvent.OnBirthdayChange(it)) },
        onPurposeSelected = { viewModel.setEvent(TraineeSignUpUiEvent.OnPurposeSelected(it)) },
        onCautionChange = { viewModel.setEvent(TraineeSignUpUiEvent.OnCautionChange(it)) },
        onBackClick = { viewModel.setEvent(TraineeSignUpUiEvent.OnBackClick) },
        onNextClick = { viewModel.setEvent(TraineeSignUpUiEvent.OnNextClick) },
        onSubmitSignUp = { file ->
            viewModel.setEvent(
                TraineeSignUpUiEvent.RequestSignUp(
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
                TraineeSignUpEffect.NavigateToBack -> navigateToPrevious()
                TraineeSignUpEffect.NavigateToConnect -> navigateToConnect()
                is TraineeSignUpEffect.ShowToast -> snackbar.show(effect.message.asString(context))
            }
        }
    }
}

@Composable
private fun TraineeSignUpScreen(
    state: TraineeSignUpUiState,
    onProfileImageSelect: (imageUri: Uri) -> Unit,
    onNameChange: (name: String) -> Unit,
    onHeightChange: (height: String) -> Unit,
    onWeightChange: (weight: String) -> Unit,
    onCautionChange: (caution: String) -> Unit,
    onBirthdayChange: (birthday: LocalDate) -> Unit,
    onPurposeSelected: (purpose: String) -> Unit,
    onSubmitSignUp: (imageFile: File?) -> Unit,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    when (state.page) {
        TraineeSignUpPage.ProfileSetUp -> TraineeProfileSetupPage(
            state = state,
            onProfileImageSelect = onProfileImageSelect,
            onNameChange = onNameChange,
            onBackClick = onBackClick,
            onNextClick = onNextClick,
        )

        TraineeSignUpPage.BasicInfo -> TraineeBasicInfoPage(
            state = state,
            onHeightChange = onHeightChange,
            onWeightChange = onWeightChange,
            onBirthdayChange = onBirthdayChange,
            onBackClick = onBackClick,
            onNextClick = onNextClick,
        )

        TraineeSignUpPage.NoteForTrainer -> TraineeNoteForTrainerPage(
            caution = state.caution,
            onCautionChange = onCautionChange,
            onBackClick = onBackClick,
            onNextClick = onNextClick,
        )

        TraineeSignUpPage.PTPurpose -> TraineePTPurposePage(
            state = state,
            onPurposeSelected = onPurposeSelected,
            onBackClick = onBackClick,
            onNextClick = onNextClick,
        )

        TraineeSignUpPage.SignUpComplete -> TraineeSignUpCompletePage(
            state = state,
            onBackClick = onBackClick,
            onNextClick = onSubmitSignUp,
        )
    }
}
