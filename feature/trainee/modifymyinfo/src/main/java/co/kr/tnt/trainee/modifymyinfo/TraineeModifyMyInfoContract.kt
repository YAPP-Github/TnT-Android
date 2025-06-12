package co.kr.tnt.trainee.modifymyinfo

import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState

internal class TraineeModifyMyInfoContract {
    data object TraineeModifyMyInfoUiState : UiState

    data object TraineeModifyMyInfoUiEvent : UiEvent

    sealed interface TraineeModifyMyInfoEffect : UiSideEffect {
        data class ShowToast(val message: String) : TraineeModifyMyInfoEffect
    }
}
