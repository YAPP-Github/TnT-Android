package co.kr.tnt.trainee.signup

import co.kr.tnt.trainee.signup.TraineeSignUpContract.TraineeSignUpEffect
import co.kr.tnt.trainee.signup.TraineeSignUpContract.TraineeSignUpPage
import co.kr.tnt.trainee.signup.TraineeSignUpContract.TraineeSignUpUiEvent
import co.kr.tnt.trainee.signup.TraineeSignUpContract.TraineeSignUpUiState
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class TraineeSignUpViewModel @Inject constructor() :
    BaseViewModel<TraineeSignUpUiState, TraineeSignUpUiEvent, TraineeSignUpEffect>(
        TraineeSignUpUiState(),
    ) {
        override suspend fun handleEvent(event: TraineeSignUpUiEvent) {
            when (event) {
                TraineeSignUpUiEvent.OnBackClick -> navigateToBack()
                TraineeSignUpUiEvent.OnNextClick -> navigateToNext()
            }
        }

        private fun navigateToNext() {
            val nextPage = when (currentState.page) {
                TraineeSignUpPage.SignUpComplete -> {
                    sendEffect(TraineeSignUpEffect.NavigateToConnect)
                    return
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
