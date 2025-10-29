package co.kr.tnt.trainee.signup

import android.net.Uri
import co.kr.tnt.domain.UserProfilePolicy
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState
import co.kr.tnt.ui.resource.DisplayText
import java.io.File
import java.time.LocalDate

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
        val isNameValid
            get() = name.isBlank() ||
                name.matches(UserProfilePolicy.USER_NAME_REGEX) &&
                name.length <= UserProfilePolicy.USER_NAME_MAX_LENGTH

        val isHeightValid
            get() = height.isNullOrBlank() || (
                height.toIntOrNull() != null &&
                    height.startsWith("0").not() &&
                    height.length <= UserProfilePolicy.USER_HEIGHT_MAX_LENGTH
            )

        val isWeightValid
            get() = weight.isNullOrBlank() || (
                weight.matches(UserProfilePolicy.USER_WEIGHT_REGEX) &&
                    weight.startsWith("0").not() &&
                    weight.length <= UserProfilePolicy.USER_WEIGHT_MAX_LENGTH
            )

        val isBasicInfoValid
            get() = isWeightValid && isHeightValid

        val isCautionNoteValid
            get() = caution.isNullOrBlank() || (
                caution.length < UserProfilePolicy.USER_CAUTION_MAX_LENGTH
            )
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
