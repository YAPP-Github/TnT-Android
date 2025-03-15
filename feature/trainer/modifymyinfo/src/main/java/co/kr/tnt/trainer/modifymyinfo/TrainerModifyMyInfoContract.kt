package co.kr.tnt.trainer.modifymyinfo

import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState

internal class TrainerModifyMyInfoContract {
    data object TrainerModifyMyInfoUiState : UiState

    data object TrainerModifyMyInfoUiEvent : UiEvent

    sealed interface TrainerModifyMyInfoEffect : UiSideEffect {
        data class ShowToast(val message: String) : TrainerModifyMyInfoEffect
    }
}
