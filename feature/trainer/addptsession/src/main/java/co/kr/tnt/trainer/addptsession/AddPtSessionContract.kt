package co.kr.tnt.trainer.addptsession

import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState

internal class AddPtSessionContract {
    data class AddPtSessionUiState(
        val dummy: String = "",
    ) : UiState

    sealed interface AddPtSessionUiEvent : UiEvent

    sealed interface AddPtSessionSideEffect : UiSideEffect {
        data class ShowToast(val message: String) : AddPtSessionSideEffect
    }
}
