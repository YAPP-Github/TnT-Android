package co.kr.tnt.trainee.signup

import android.content.Context
import android.net.Uri
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState
import java.time.LocalDate

private const val MAX_NAME_LENGTH = 15
private const val MAX_HEIGHT_LENGTH = 3
private const val MAX_WEIGHT_LENGTH = 5

internal class TraineeSignUpContract {
    data class TraineeSignUpUiState(
        val page: TraineeSignUpPage = TraineeSignUpPage.ProfileSetUp,
        val name: String = "",
        val image: Uri? = null,
        val birthday: LocalDate? = null,
        val height: String? = null,
        val weight: String? = null,
        val ptPurpose: List<String> = emptyList(),
        val caution: String? = "",
    ) : UiState {
        val isNameValid get() = name.length <= MAX_NAME_LENGTH

        /**
         * 키가 유효한 입력값인지 검사
         * 형식: 정수 3자
         */
        val isHeightValid
            get() = height.isNullOrBlank() || (
                height.toIntOrNull() != null &&
                    !height.startsWith("0") &&
                    height.length <= MAX_HEIGHT_LENGTH
            )

        /**
         * 몸무게가 유효한 입력값인지 검사
         * 형식: 5자 이하의 실수 (000, 00, 00.0, 000.0)
         */
        private val weightRegex = Regex("^(\\d{1,3}(\\.\\d)?)?\$")
        val isWeightValid
            get() = weight.isNullOrBlank() || (
                weight.matches(weightRegex) &&
                    !weight.startsWith("0") &&
                    weight.length <= MAX_WEIGHT_LENGTH
            )

        val isBasicInfoValid
            get() = isWeightValid && isHeightValid
    }

    sealed interface TraineeSignUpUiEvent : UiEvent {
        data class OnImageChange(val imageUri: Uri) : TraineeSignUpUiEvent
        data class OnNameChange(val name: String) : TraineeSignUpUiEvent
        data class OnHeightChange(val height: String) : TraineeSignUpUiEvent
        data class OnWeightChange(val weight: String) : TraineeSignUpUiEvent
        data class OnBirthdayChange(val birthday: LocalDate) : TraineeSignUpUiEvent
        data class OnPurposeSelected(val purpose: String) : TraineeSignUpUiEvent
        data class OnCautionChange(val text: String) : TraineeSignUpUiEvent
        data object OnNextClick : TraineeSignUpUiEvent
        data object OnBackClick : TraineeSignUpUiEvent
        data class RequestSignUp(
            val context: Context,
            val imageUri: Uri?,
            val id: String,
            val email: String,
            val authType: String,
        ) : TraineeSignUpUiEvent
    }

    sealed interface TraineeSignUpEffect : UiSideEffect {
        data class ShowToast(val message: String) : TraineeSignUpEffect
        data object NavigateToBack : TraineeSignUpEffect
        data object NavigateToConnect : TraineeSignUpEffect
    }

    enum class TraineeSignUpPage {
        ProfileSetUp,
        BasicInfo,
        PTPurpose,
        NoteForTrainer,
        SignUpComplete,
        ;

        companion object {
            fun getPreviousPage(currentPage: TraineeSignUpPage): TraineeSignUpPage {
                return when (currentPage) {
                    BasicInfo -> ProfileSetUp
                    PTPurpose -> BasicInfo
                    NoteForTrainer -> PTPurpose
                    SignUpComplete -> NoteForTrainer
                    else -> error("No previous page defined for $currentPage")
                }
            }

            fun getNextPage(currentPage: TraineeSignUpPage): TraineeSignUpPage {
                return when (currentPage) {
                    ProfileSetUp -> BasicInfo
                    BasicInfo -> PTPurpose
                    PTPurpose -> NoteForTrainer
                    NoteForTrainer -> SignUpComplete
                    else -> error("No next page defined for $currentPage")
                }
            }
        }
    }
}
