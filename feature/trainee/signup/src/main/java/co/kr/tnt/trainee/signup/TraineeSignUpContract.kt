package co.kr.tnt.trainee.signup

import android.content.Context
import android.net.Uri
import co.kr.tnt.domain.model.UserType
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState
import java.time.LocalDate

internal class TraineeSignUpContract {
    data class TraineeSignUpUiState(
        val page: TraineeSignUpPage = TraineeSignUpPage.ProfileSetUp,
        val height: String = "",
        val weight: String = "",
        val isHeightValid: Boolean = false,
        val isWeightValid: Boolean = false,
        val isBasicInfoValid: Boolean = false,
        val traineeState: UserType.Trainee = UserType.Trainee(),
    ) : UiState

    sealed interface TraineeSignUpUiEvent : UiEvent {
        data class OnImageChange(val imageUri: Uri) : TraineeSignUpUiEvent
        data class OnNameChange(val name: String) : TraineeSignUpUiEvent
        data class OnHeightChange(val height: String) : TraineeSignUpUiEvent
        data class OnWeightChange(val weight: String) : TraineeSignUpUiEvent
        data class OnBirthdayChange(val birthday: LocalDate) : TraineeSignUpUiEvent
        data object OnBasicFormUpdate : TraineeSignUpUiEvent
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
