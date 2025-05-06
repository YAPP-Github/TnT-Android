package co.kr.tnt.trainee.signup

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.designsystem.snackbar.LocalSnackbar
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
    messagingToken: String,
    navigateToPrevious: () -> Unit,
    navigateToConnect: () -> Unit,
    viewModel: TraineeSignUpViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val snackbar = LocalSnackbar.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TraineeSignUpScreen(
        state = uiState,
        onChangeName = { viewModel.setEvent(TraineeSignUpUiEvent.OnChangeName(it)) },
        onProfileImageSelect = { viewModel.setEvent(TraineeSignUpUiEvent.OnChangeImage(it)) },
        onChangeHeight = { viewModel.setEvent(TraineeSignUpUiEvent.OnChangeHeight(it)) },
        onChangeWeight = { viewModel.setEvent(TraineeSignUpUiEvent.OnChangeWeight(it)) },
        onChangeBirthday = { viewModel.setEvent(TraineeSignUpUiEvent.OnChangeBirthday(it)) },
        onSelectPurpose = { viewModel.setEvent(TraineeSignUpUiEvent.OnSelectPurpose(it)) },
        onChangeCaution = { viewModel.setEvent(TraineeSignUpUiEvent.OnChangeCaution(it)) },
        onClickBack = { viewModel.setEvent(TraineeSignUpUiEvent.OnClickBack) },
        onClickNext = { viewModel.setEvent(TraineeSignUpUiEvent.OnClickNext) },
        onSubmitSignUp = { uri ->
            viewModel.setEvent(
                TraineeSignUpUiEvent.RequestSignUp(
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
                TraineeSignUpEffect.NavigateToBack -> navigateToPrevious()
                TraineeSignUpEffect.NavigateToConnect -> navigateToConnect()
                is TraineeSignUpEffect.ShowToast -> snackbar.show(effect.message)
            }
        }
    }
}

@Composable
private fun TraineeSignUpScreen(
    state: TraineeSignUpUiState,
    onProfileImageSelect: (Uri) -> Unit,
    onChangeName: (String) -> Unit,
    onChangeHeight: (String) -> Unit,
    onChangeWeight: (String) -> Unit,
    onChangeCaution: (String) -> Unit,
    onChangeBirthday: (LocalDate) -> Unit,
    onSelectPurpose: (String) -> Unit,
    onSubmitSignUp: (Uri?) -> Unit,
    onClickNext: () -> Unit,
    onClickBack: () -> Unit,
) {
    when (state.page) {
        TraineeSignUpPage.ProfileSetUp -> TraineeProfileSetupPage(
            state = state,
            onProfileImageSelect = onProfileImageSelect,
            onChangeName = onChangeName,
            onClickBack = onClickBack,
            onClickNext = onClickNext,
        )

        TraineeSignUpPage.BasicInfo -> TraineeBasicInfoPage(
            state = state,
            onChangeHeight = onChangeHeight,
            onChangeWeight = onChangeWeight,
            onChangeBirthday = onChangeBirthday,
            onClickBack = onClickBack,
            onClickNext = onClickNext,
        )

        TraineeSignUpPage.NoteForTrainer -> TraineeNoteForTrainerPage(
            caution = state.caution,
            onChangeCaution = onChangeCaution,
            onClickBack = onClickBack,
            onClickNext = onClickNext,
        )

        TraineeSignUpPage.PTPurpose -> TraineePTPurposePage(
            state = state,
            onSelectPurpose = onSelectPurpose,
            onClickBack = onClickBack,
            onClickNext = onClickNext,
        )

        TraineeSignUpPage.SignUpComplete -> TraineeSignUpCompletePage(
            state = state,
            onClickBack = onClickBack,
            onClickNext = onSubmitSignUp,
        )
    }
}
