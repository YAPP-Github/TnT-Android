package co.kr.tnt.trainer.invite

import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState

internal class TrainerInviteContract {
    data class TrainerInviteUiState(
        val inviteCode: String = "",
    ) : UiState

    sealed interface TrainerInviteUiEvent : UiEvent {
        data object OnRegenerateClick : TrainerInviteUiEvent
        data object OnBackClick : TrainerInviteUiEvent
        data object OnSkipClick : TrainerInviteUiEvent
    }

    sealed interface TrainerInviteSideEffect : UiSideEffect {
        data object NavigateToBack : TrainerInviteSideEffect
        data object NavigateToHome : TrainerInviteSideEffect
        data class ShowToast(val message: String) : TrainerInviteSideEffect
    }
}
