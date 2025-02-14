package co.kr.tnt.trainer.signup

import android.content.Context
import android.net.Uri
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState

private const val MAX_LENGTH = 15

internal class TrainerSignUpContract {
    data class TrainerSignUpUiState(
        val page: TrainerSignUpPage = TrainerSignUpPage.ProfileSetUp,
        val name: String = "",
        val image: Uri? = null,
        val isLoading: Boolean = false,
    ) : UiState {
        /**
         * 입력 값을 검사해 한글/영어/공백만 허용하고 특수문자는 제거
         */
        private val nameRegex = Regex("^[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣 ]+\$")
        val isNameValid get() = name.isBlank() || name.matches(nameRegex) && name.length <= MAX_LENGTH
    }

    sealed interface TrainerSignUpUiEvent : UiEvent {
        data class OnImageChange(val imageUri: Uri) : TrainerSignUpUiEvent
        data class OnNameChange(val name: String) : TrainerSignUpUiEvent
        data object OnNextClick : TrainerSignUpUiEvent
        data object OnBackClick : TrainerSignUpUiEvent
        data class RequestSignUp(
            val context: Context,
            val imageUri: Uri?,
            val id: String,
            val email: String,
            val authType: String,
        ) : TrainerSignUpUiEvent
    }

    sealed interface TrainerSignUpEffect : UiSideEffect {
        data class ShowToast(val message: String) : TrainerSignUpEffect
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
