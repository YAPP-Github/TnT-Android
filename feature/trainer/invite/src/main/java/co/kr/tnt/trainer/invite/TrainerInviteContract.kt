package co.kr.tnt.trainer.invite

import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState
import co.kr.tnt.ui.model.SnackbarType
import co.kr.tnt.ui.resource.DisplayText

internal class TrainerInviteContract {
    data class TrainerInviteUiState(
        val inviteCode: String = "",
    ) : UiState

    sealed interface TrainerInviteUiEvent : UiEvent {
        data object OnClickRegenerate : TrainerInviteUiEvent
        data class OnClickCode(val code: String) : TrainerInviteUiEvent
        data object OnClickBack : TrainerInviteUiEvent
        data object OnClickSkip : TrainerInviteUiEvent
    }

    sealed interface TrainerInviteSideEffect : UiSideEffect {
        data object NavigateToBack : TrainerInviteSideEffect
        data object NavigateToHome : TrainerInviteSideEffect
        data class ShowToast(
            val message: DisplayText,
            val type: SnackbarType = SnackbarType.WARNING,
        ) : TrainerInviteSideEffect

        data class CopyToClipBoard(val value: String) : TrainerInviteSideEffect
    }
}
