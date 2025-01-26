package co.kr.tnt.trainer.signup

import co.kr.tnt.trainer.signup.TrainerSignUpContract.TrainerSignUpEffect
import co.kr.tnt.trainer.signup.TrainerSignUpContract.TrainerSignUpPage
import co.kr.tnt.trainer.signup.TrainerSignUpContract.TrainerSignUpUiEvent
import co.kr.tnt.trainer.signup.TrainerSignUpContract.TrainerSignUpUiState
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class TrainerSignUpViewModel @Inject constructor() :
    BaseViewModel<TrainerSignUpUiState, TrainerSignUpUiEvent, TrainerSignUpEffect>(
        TrainerSignUpUiState(),
    ) {
        override suspend fun handleEvent(event: TrainerSignUpUiEvent) {
            when (event) {
                TrainerSignUpUiEvent.OnNextClick -> navigateToNext()
                TrainerSignUpUiEvent.OnBackClick -> navigateToBack()
            }
        }

        private fun navigateToNext() {
            val nextPage = when (currentState.page) {
                TrainerSignUpPage.SignUpComplete -> {
                    sendEffect(TrainerSignUpEffect.NavigateToConnect)
                    return
                }

                else -> TrainerSignUpPage.getNextPage(currentState.page)
            }
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
