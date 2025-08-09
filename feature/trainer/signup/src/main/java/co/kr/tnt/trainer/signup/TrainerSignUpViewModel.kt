package co.kr.tnt.trainer.signup

import android.net.Uri
import androidx.lifecycle.viewModelScope
import co.kr.tnt.core.ui.R.string.core_failed_to_server_request
import co.kr.tnt.domain.model.User
import co.kr.tnt.domain.model.trainer.TrainerManagementMemberCount
import co.kr.tnt.domain.repository.SignUpRepository
import co.kr.tnt.trainer.signup.TrainerSignUpContract.TrainerSignUpEffect
import co.kr.tnt.trainer.signup.TrainerSignUpContract.TrainerSignUpPage
import co.kr.tnt.trainer.signup.TrainerSignUpContract.TrainerSignUpUiEvent
import co.kr.tnt.trainer.signup.TrainerSignUpContract.TrainerSignUpUiState
import co.kr.tnt.ui.base.BaseViewModel
import co.kr.tnt.ui.resource.DisplayText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
internal class TrainerSignUpViewModel @Inject constructor(
    private val signUpRepository: SignUpRepository,
) : BaseViewModel<TrainerSignUpUiState, TrainerSignUpUiEvent, TrainerSignUpEffect>(
        TrainerSignUpUiState(),
    ) {
    override suspend fun handleEvent(event: TrainerSignUpUiEvent) {
        when (event) {
            is TrainerSignUpUiEvent.OnChangeImage -> setProfileImage(event.imageUri)
            is TrainerSignUpUiEvent.OnChangeName -> setName(event.name)
            TrainerSignUpUiEvent.OnClickNext -> navigateToNext()
            TrainerSignUpUiEvent.OnClickBack -> navigateToBack()
            is TrainerSignUpUiEvent.RequestSignUp -> signUp(
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
        viewModelScope.launch(Dispatchers.IO) {
            updateState { copy(isLoading = true) }

            runCatching {
                signUpRepository.signUp(
                    profileImage = imageFile,
                    user = User.Trainer(
                        id = id,
                        name = currentState.name,
                        image = currentState.image.toString(),
                        memberCounts = TrainerManagementMemberCount.ZERO,
                    ),
                    socialId = id,
                    socialType = authType,
                    email = email,
                    messagingToken = messagingToken,
                )
            }.onSuccess {
                sendEffect(TrainerSignUpEffect.NavigateToConnect)
            }.onFailure {
                sendEffect(TrainerSignUpEffect.ShowToast(DisplayText.Resource(core_failed_to_server_request)))
            }.also {
                updateState { copy(isLoading = false) }
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
