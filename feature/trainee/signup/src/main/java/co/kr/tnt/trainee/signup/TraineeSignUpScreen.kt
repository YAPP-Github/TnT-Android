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
        onChangeName = { viewModel.setEvent(TraineeSignUpUiEvent.OnChangeName(it)) },
        onSelectProfileImage = { viewModel.setEvent(TraineeSignUpUiEvent.OnChangeImage(it)) },
        onChangeHeight = { viewModel.setEvent(TraineeSignUpUiEvent.OnChangeHeight(it)) },
        onChangeWeight = { viewModel.setEvent(TraineeSignUpUiEvent.OnChangeWeight(it)) },
        onChangeBirthday = { viewModel.setEvent(TraineeSignUpUiEvent.OnChangeBirthday(it)) },
        onSelectPurpose = { viewModel.setEvent(TraineeSignUpUiEvent.OnSelectPurpose(it)) },
        onChangeCaution = { viewModel.setEvent(TraineeSignUpUiEvent.OnChangeCaution(it)) },
        onClickBack = { viewModel.setEvent(TraineeSignUpUiEvent.OnClickBack) },
        onClickNext = { viewModel.setEvent(TraineeSignUpUiEvent.OnClickNext) },
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
                is TraineeSignUpEffect.ShowToast -> snackbar.show(effect.message)
            }
        }
    }
}

@Composable
private fun TraineeSignUpScreen(
    state: TraineeSignUpUiState,
    onSelectProfileImage: (imageUri: Uri) -> Unit,
    onChangeName: (name: String) -> Unit,
    onChangeHeight: (height: String) -> Unit,
    onChangeWeight: (weight: String) -> Unit,
    onChangeCaution: (caution: String) -> Unit,
    onChangeBirthday: (birthday: LocalDate) -> Unit,
    onSelectPurpose: (purpose: String) -> Unit,
    onSubmitSignUp: (imageFile: File?) -> Unit,
    onClickNext: () -> Unit,
    onClickBack: () -> Unit,
) {
    when (state.page) {
        TraineeSignUpPage.ProfileSetUp -> TraineeProfileSetupPage(
            state = state,
            onProfileImageSelect = onSelectProfileImage,
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
