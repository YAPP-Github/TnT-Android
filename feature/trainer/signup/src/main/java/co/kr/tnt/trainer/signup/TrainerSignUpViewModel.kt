package co.kr.tnt.trainer.signup

import android.content.Context
import android.net.Uri
import androidx.lifecycle.viewModelScope
import co.kr.tnt.domain.model.UserType
import co.kr.tnt.domain.repository.SignUpRepository
import co.kr.tnt.trainer.signup.TrainerSignUpContract.TrainerSignUpEffect
import co.kr.tnt.trainer.signup.TrainerSignUpContract.TrainerSignUpPage
import co.kr.tnt.trainer.signup.TrainerSignUpContract.TrainerSignUpUiEvent
import co.kr.tnt.trainer.signup.TrainerSignUpContract.TrainerSignUpUiState
import co.kr.tnt.ui.base.BaseViewModel
import co.kr.tnt.ui.util.toFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import co.kr.tnt.core.ui.R as uiResource

@HiltViewModel
internal class TrainerSignUpViewModel @Inject constructor(
    private val signUpRepository: SignUpRepository,
) : BaseViewModel<TrainerSignUpUiState, TrainerSignUpUiEvent, TrainerSignUpEffect>(
        TrainerSignUpUiState(),
    ) {
    override suspend fun handleEvent(event: TrainerSignUpUiEvent) {
        when (event) {
            is TrainerSignUpUiEvent.OnImageChange -> setProfileImage(event.imageUri)
            is TrainerSignUpUiEvent.OnNameChange -> setName(event.name)
            TrainerSignUpUiEvent.OnNextClick -> navigateToNext()
            TrainerSignUpUiEvent.OnBackClick -> navigateToBack()
            is TrainerSignUpUiEvent.RequestSignUp -> signUp(
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
            val profileImagePart = imageUri?.toFile(context)

            runCatching {
                signUpRepository.signUp(
                    profileImage = profileImagePart,
                    userType = UserType.Trainer(
                        id = id,
                        name = currentState.name,
                        image = currentState.image.toString(),
                    ),
                    socialId = id,
                    socialType = authType,
                    email = email,
                )
            }.onSuccess {
                sendEffect(TrainerSignUpEffect.NavigateToConnect)
            }.onFailure {
                // TODO 디자인 시스템 Toast 적용
                val message = context.getString(uiResource.string.error_server_request_failed)
                sendEffect(TrainerSignUpEffect.ShowToast(message))
            }
        }
    }

    private fun setProfileImage(imageUri: Uri) {
        updateState { copy(image = imageUri) }
    }

    private fun setName(name: String) {
        updateState { copy(name = name) }
    }

    private fun navigateToNext() {
        val nextPage = TrainerSignUpPage.getNextPage(currentState.page)
        updateState { copy(page = nextPage) }
    }

    private fun navigateToBack() {
        val previousPage = when (currentState.page) {
            TrainerSignUpPage.ProfileSetUp -> {
                sendEffect(TrainerSignUpEffect.NavigateToBack)
                return
            }

            else -> TrainerSignUpPage.getPreviousPage(currentState.page)
        }
        updateState { copy(page = previousPage) }
    }
}
