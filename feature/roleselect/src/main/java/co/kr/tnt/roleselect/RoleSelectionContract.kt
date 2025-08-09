package co.kr.tnt.roleselect

import co.kr.tnt.roleselect.model.RoleState
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState

internal class RoleSelectionContract {
    data class RoleSelectionUiState(
        val userType: RoleState = RoleState.Trainer,
    ) : UiState

    sealed interface RoleSelectionUiEvent : UiEvent {
        data class OnClickNext(val role: RoleState) : RoleSelectionUiEvent
    }

    sealed interface RoleSelectionEffect : UiSideEffect {
        data object NavigateToTrainerSignUp : RoleSelectionEffect
        data object NavigateToTraineeSignUp : RoleSelectionEffect
    }
}
