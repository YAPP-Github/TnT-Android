package co.kr.tnt.roleselect

import co.kr.tnt.roleselect.RoleSelectionContract.RoleSelectionEffect
import co.kr.tnt.roleselect.RoleSelectionContract.RoleSelectionUiEvent
import co.kr.tnt.roleselect.RoleSelectionContract.RoleSelectionUiState
import co.kr.tnt.roleselect.model.RoleState
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class RoleSelectionViewModel @Inject constructor() :
    BaseViewModel<RoleSelectionUiState, RoleSelectionUiEvent, RoleSelectionEffect>(
        RoleSelectionUiState(),
    ) {
        override suspend fun handleEvent(event: RoleSelectionUiEvent) {
            when (event) {
                is RoleSelectionUiEvent.OnNextClick -> navigateToSignUp(event.role)
            }
        }

        private fun navigateToSignUp(role: RoleState) {
            when (role) {
                is RoleState.Trainer -> {
                    sendEffect(RoleSelectionEffect.NavigateToTrainerSignUp)
                }

                is RoleState.Trainee -> {
                    sendEffect(RoleSelectionEffect.NavigateToTraineeSignUp)
                }
            }
        }
    }
