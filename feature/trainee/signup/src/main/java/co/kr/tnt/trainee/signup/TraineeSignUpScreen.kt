package co.kr.tnt.trainee.signup

import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.trainee.signup.TraineeSignUpContract.TraineeSignUpEffect
import co.kr.tnt.trainee.signup.TraineeSignUpContract.TraineeSignUpPage
import co.kr.tnt.trainee.signup.TraineeSignUpContract.TraineeSignUpUiEvent
import co.kr.tnt.trainee.signup.TraineeSignUpContract.TraineeSignUpUiState
import java.time.LocalDate

@Composable
internal fun TraineeSignUpRoute(
    authId: String,
    authType: String,
    email: String,
    navigateToPrevious: () -> Unit,
    navigateToConnect: () -> Unit,
    viewModel: TraineeSignUpViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
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
        onSubmitSignUp = { uri ->
            viewModel.setEvent(
                TraineeSignUpUiEvent.RequestSignUp(
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
                TraineeSignUpEffect.NavigateToBack -> navigateToPrevious()
                TraineeSignUpEffect.NavigateToConnect -> navigateToConnect()
                is TraineeSignUpEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@Composable
private fun TraineeSignUpScreen(
    state: TraineeSignUpUiState,
    onProfileImageSelect: (Uri) -> Unit,
    onNameChange: (String) -> Unit,
    onHeightChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
    onCautionChange: (String) -> Unit,
    onBirthdayChange: (LocalDate) -> Unit,
    onPurposeSelected: (String) -> Unit,
    onSubmitSignUp: (Uri?) -> Unit,
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
            state = state,
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
