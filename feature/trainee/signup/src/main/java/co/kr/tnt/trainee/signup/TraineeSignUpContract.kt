package co.kr.tnt.trainee.signup

import android.net.Uri
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState
import co.kr.tnt.ui.resource.DisplayText
import java.io.File
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
        val ptPurpose: List<String>? = emptyList(),
        val caution: String? = "",
        val isLoading: Boolean = false,
    ) : UiState {
        /**
         * 입력 값을 검사해 한글/영어/공백만 허용하고 특수문자는 제거
         */
        private val nameRegex = Regex("^[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣 ]+\$")
        val isNameValid get() = name.isBlank() || name.matches(nameRegex) && name.length <= MAX_NAME_LENGTH

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
        data class OnChangeImage(val imageUri: Uri) : TraineeSignUpUiEvent
        data class OnChangeName(val name: String) : TraineeSignUpUiEvent
        data class OnChangeHeight(val height: String) : TraineeSignUpUiEvent
        data class OnChangeWeight(val weight: String) : TraineeSignUpUiEvent
        data class OnChangeBirthday(val birthday: LocalDate) : TraineeSignUpUiEvent
        data class OnSelectPurpose(val purpose: String) : TraineeSignUpUiEvent
        data class OnChangeCaution(val text: String) : TraineeSignUpUiEvent
        data object OnClickNext : TraineeSignUpUiEvent
        data object OnClickBack : TraineeSignUpUiEvent
        data class RequestSignUp(
            val imageFile: File?,
            val id: String,
            val email: String,
            val authType: String,
            val messagingToken: String,
        ) : TraineeSignUpUiEvent
    }

    sealed interface TraineeSignUpEffect : UiSideEffect {
        data class ShowToast(val message: DisplayText) : TraineeSignUpEffect
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
