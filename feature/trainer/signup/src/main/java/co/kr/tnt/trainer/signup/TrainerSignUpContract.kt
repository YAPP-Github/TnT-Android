package co.kr.tnt.trainer.signup

import android.net.Uri
import co.kr.tnt.domain.UserProfilePolicy
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState
import co.kr.tnt.ui.resource.DisplayText
import java.io.File

internal class TrainerSignUpContract {
    data class TrainerSignUpUiState(
        val page: TrainerSignUpPage = TrainerSignUpPage.ProfileSetUp,
        val name: String = "",
        val image: Uri? = null,
        val isLoading: Boolean = false,
    ) : UiState {
        val isNameValid
            get() = name.isBlank() ||
                name.matches(UserProfilePolicy.USER_NAME_REGEX) &&
                name.length <= UserProfilePolicy.USER_NAME_MAX_LENGTH
    }

    sealed interface TrainerSignUpUiEvent : UiEvent {
        data class OnChangeImage(val imageUri: Uri) : TrainerSignUpUiEvent
        data class OnChangeName(val name: String) : TrainerSignUpUiEvent
        data object OnClickNext : TrainerSignUpUiEvent
        data object OnClickBack : TrainerSignUpUiEvent
        data class RequestSignUp(
            val imageFile: File?,
            val id: String,
            val email: String,
            val authType: String,
            val messagingToken: String,
        ) : TrainerSignUpUiEvent
    }

    sealed interface TrainerSignUpEffect : UiSideEffect {
        data class ShowToast(val message: DisplayText) : TrainerSignUpEffect
        data object NavigateToBack : TrainerSignUpEffect
        data object NavigateToConnect : TrainerSignUpEffect
    }

    enum class TrainerSignUpPage {
        ProfileSetUp,
        SignUpComplete,
        ;

        companion object {
            fun getPreviousPage(currentPage: TrainerSignUpPage): TrainerSignUpPage {
                return when (currentPage) {
                    SignUpComplete -> ProfileSetUp
                    else -> error("No previous page defined for $currentPage")
                }
            }

            fun getNextPage(currentPage: TrainerSignUpPage): TrainerSignUpPage {
                return when (currentPage) {
                    ProfileSetUp -> SignUpComplete
                    else -> error("No next page defined for $currentPage")
                }
            }
        }
    }
}
