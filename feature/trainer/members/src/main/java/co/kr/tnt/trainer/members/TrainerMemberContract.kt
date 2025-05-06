package co.kr.tnt.trainer.members

import co.kr.tnt.domain.model.MemberInfo
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState
import co.kr.tnt.ui.resource.DisplayText

internal class TrainerMemberContract {
    data class TrainerMemberUiState(
        val memberList: List<MemberInfo> = emptyList(),
    ) : UiState

    sealed interface TrainerMemberUiEvent : UiEvent {
        data object OnClickMember : TrainerMemberUiEvent
    }

    sealed interface TrainerMemberSideEffect : UiSideEffect {
        data object NavigateToInvite : TrainerMemberSideEffect
        data class ShowToast(val message: DisplayText) : TrainerMemberSideEffect
    }
}
