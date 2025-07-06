package co.kr.tnt.trainee.signup

import android.net.Uri
import androidx.lifecycle.viewModelScope
import co.kr.tnt.core.ui.R.string.core_failed_to_server_request
import co.kr.tnt.domain.model.User
import co.kr.tnt.domain.repository.SignUpRepository
import co.kr.tnt.trainee.signup.TraineeSignUpContract.TraineeSignUpEffect
import co.kr.tnt.trainee.signup.TraineeSignUpContract.TraineeSignUpPage
import co.kr.tnt.trainee.signup.TraineeSignUpContract.TraineeSignUpUiEvent
import co.kr.tnt.trainee.signup.TraineeSignUpContract.TraineeSignUpUiState
import co.kr.tnt.ui.base.BaseViewModel
import co.kr.tnt.ui.resource.DisplayText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class TraineeSignUpViewModel @Inject constructor(
    private val signUpRepository: SignUpRepository,
) : BaseViewModel<TraineeSignUpUiState, TraineeSignUpUiEvent, TraineeSignUpEffect>(
        TraineeSignUpUiState(),
    ) {
    override suspend fun handleEvent(event: TraineeSignUpUiEvent) {
        when (event) {
            is TraineeSignUpUiEvent.OnImageChange -> updateProfileImage(event.imageUri)
            is TraineeSignUpUiEvent.OnNameChange -> updateName(event.name)
            is TraineeSignUpUiEvent.OnHeightChange -> updateHeight(event.height)
            is TraineeSignUpUiEvent.OnWeightChange -> updateWeight(event.weight)
            is TraineeSignUpUiEvent.OnBirthdayChange -> updateBirthday(event.birthday)
            is TraineeSignUpUiEvent.OnPurposeSelected -> updateSelectedPurposes(event.purpose)
            is TraineeSignUpUiEvent.OnCautionChange -> updateCaution(event.text)
            TraineeSignUpUiEvent.OnBackClick -> navigateToBack()
            TraineeSignUpUiEvent.OnNextClick -> navigateToNext()
            is TraineeSignUpUiEvent.RequestSignUp -> signUp(
                imageFile = event.imageFile,
                id = event.id,
                email = event.email,
                authType = event.authType,
                messagingToken = event.messagingToken,
            )
        }
    }

    private fun signUp(
        imageFile: File?,
        id: String,
        email: String,
        authType: String,
        messagingToken: String,
    ) {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            val state = currentState
            runCatching {
                signUpRepository.signUp(
                    profileImage = imageFile,
                    user = User.Trainee(
                        id = id,
                        name = state.name,
                        image = state.image.toString(),
                        birthday = state.birthday,
                        weight = state.weight?.toDoubleOrNull(),
                        height = state.height?.toIntOrNull(),
                        ptPurpose = state.ptPurpose,
                        caution = state.caution,
                        isConnected = false,
                    ),
                    socialId = id,
                    socialType = authType,
                    email = email,
                    messagingToken = messagingToken,
                )
            }.onSuccess {
                sendEffect(TraineeSignUpEffect.NavigateToConnect)
            }.onFailure {
                sendEffect(
                    TraineeSignUpEffect.ShowToast(
                        DisplayText.Resource(core_failed_to_server_request),
                    ),
                )
            }.also {
                updateState { copy(isLoading = false) }
            }
        }
    }

    private fun updateProfileImage(imageUri: Uri) {
        updateState { copy(image = imageUri) }
    }

    private fun updateName(name: String) {
        updateState { copy(name = name) }
    }

    private fun updateHeight(height: String) {
        updateState { copy(height = height.takeIf { it.isNotEmpty() }) }
    }

    private fun updateWeight(weight: String) {
        updateState { copy(weight = weight.takeIf { it.isNotEmpty() }) }
    }

    private fun updateBirthday(birthday: LocalDate) {
        updateState { copy(birthday = birthday) }
    }

    private fun updateSelectedPurposes(purpose: String) {
        val updatedPurposes = currentState.ptPurpose.orEmpty().toMutableList().apply {
            if (contains(purpose)) {
                remove(purpose)
            } else {
                add(purpose)
            }
        }
        updateState { copy(ptPurpose = updatedPurposes) }
    }

    private fun updateCaution(text: String) {
        updateState { copy(caution = text) }
    }

    private fun navigateToNext() {
        val nextPage = TraineeSignUpPage.getNextPage(currentState.page)
        updateState { copy(page = nextPage) }
    }

    private fun navigateToBack() {
        val previousPage = when (currentState.page) {
            TraineeSignUpPage.ProfileSetUp -> {
                sendEffect(TraineeSignUpEffect.NavigateToBack)
                return
            }

            else -> TraineeSignUpPage.getPreviousPage(currentState.page)
        }
        updateState { copy(page = previousPage) }
    }
}
