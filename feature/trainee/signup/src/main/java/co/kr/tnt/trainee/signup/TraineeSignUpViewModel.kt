package co.kr.tnt.trainee.signup

import android.content.Context
import android.net.Uri
import androidx.lifecycle.viewModelScope
import co.kr.tnt.core.ui.R
import co.kr.tnt.domain.repository.SignUpRepository
import co.kr.tnt.trainee.signup.TraineeSignUpContract.TraineeSignUpEffect
import co.kr.tnt.trainee.signup.TraineeSignUpContract.TraineeSignUpPage
import co.kr.tnt.trainee.signup.TraineeSignUpContract.TraineeSignUpUiEvent
import co.kr.tnt.trainee.signup.TraineeSignUpContract.TraineeSignUpUiState
import co.kr.tnt.ui.base.BaseViewModel
import co.kr.tnt.ui.util.toMultiPart
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

private const val MAX_HEIGHT_LENGTH = 3
private const val MAX_WEIGHT_LENGTH = 5

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
            TraineeSignUpUiEvent.OnBasicFormUpdate -> setBasicFormDate()
            is TraineeSignUpUiEvent.OnPurposeSelected -> updateSelectedPurposes(event.purpose)
            is TraineeSignUpUiEvent.OnCautionChange -> updateCaution(event.text)
            TraineeSignUpUiEvent.OnBackClick -> navigateToBack()
            TraineeSignUpUiEvent.OnNextClick -> navigateToNext()
            is TraineeSignUpUiEvent.RequestSignUp -> signUp(
                context = event.context,
                imageUri = event.imageUri,
                id = event.id,
                email = event.email,
                authType = event.authType,
            )
        }
    }

    private fun signUp(
        context: Context,
        imageUri: Uri?,
        id: String,
        email: String,
        authType: String,
    ) {
        viewModelScope.launch {
            val profileImagePart = imageUri?.toMultiPart(context, "profileImage")

            runCatching {
                signUpRepository.signUp(
                    profileImage = profileImagePart,
                    userType = currentState.traineeState,
                    socialId = id,
                    socialType = authType,
                    email = email,
                )
            }.onSuccess {
                sendEffect(TraineeSignUpEffect.NavigateToConnect)
            }.onFailure {
                // TODO 디자인 시스템 Toast 적용
                val message = context.getString(R.string.error_server_request_failed)
                sendEffect(TraineeSignUpEffect.ShowToast(message))
            }
        }
    }

    private fun updateProfileImage(imageUri: Uri) {
        updateState {
            copy(
                traineeState = traineeState.copy(image = imageUri.toString()),
            )
        }
    }

    private fun updateName(name: String) {
        updateState {
            copy(
                traineeState = traineeState.copy(name = name),
            )
        }
    }

    private fun updateHeight(height: String) {
        val isValid = validateHeight(height)
        updateState {
            val isFormValid = isValid && isWeightValid
            copy(
                height = height,
                isHeightValid = isValid,
                isBasicInfoValid = isFormValid,
            )
        }
    }

    private fun updateWeight(weight: String) {
        val isValid = validateWeight(weight)
        updateState {
            val isFormValid = isHeightValid && isValid
            copy(
                weight = weight,
                isWeightValid = isValid,
                isBasicInfoValid = isFormValid,
            )
        }
    }

    private fun updateBirthday(birthday: LocalDate) {
        updateState {
            copy(
                traineeState = traineeState.copy(birthday = birthday),
            )
        }
    }

    private fun setBasicFormDate() {
        updateState {
            copy(
                traineeState = traineeState.copy(
                    weight = weight.toDouble(),
                    height = height.toInt(),
                ),
            )
        }
    }

    /**
     * 키가 유효한 입력값인지 검사
     * 형식: 정수 3자
     */
    private fun validateHeight(input: String): Boolean {
        return input.isNotEmpty() && input.toIntOrNull() != null &&
            !input.startsWith("0") && input.length <= MAX_HEIGHT_LENGTH
    }

    /**
     * 몸무게가 유효한 입력값인지 검사
     * 형식: 5자 이하의 실수 (000, 00, 00.0, 000.0)
     */
    private fun validateWeight(input: String): Boolean {
        val weightRegex = Regex("^(\\d{1,3}(\\.\\d)?)?\$")
        return input.isNotEmpty() && input.matches(weightRegex) &&
            !input.startsWith("0") && input.length <= MAX_WEIGHT_LENGTH
    }

    private fun updateSelectedPurposes(purpose: String) {
        val updatedPurposes = currentState.traineeState.ptPurpose.toMutableList().apply {
            if (contains(purpose)) {
                remove(purpose)
            } else {
                add(purpose)
            }
        }
        updateState {
            copy(
                traineeState = traineeState.copy(ptPurpose = updatedPurposes),
            )
        }
    }

    private fun updateCaution(text: String) {
        updateState {
            copy(
                traineeState = traineeState.copy(caution = text),
            )
        }
    }

    private fun navigateToNext() {
        val nextPage = when (currentState.page) {
            TraineeSignUpPage.BasicInfo -> {
                setBasicFormDate()
                TraineeSignUpPage.getNextPage(currentState.page)
            }

            else -> TraineeSignUpPage.getNextPage(currentState.page)
        }
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
