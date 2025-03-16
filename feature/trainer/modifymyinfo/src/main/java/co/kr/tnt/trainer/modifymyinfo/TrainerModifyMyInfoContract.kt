package co.kr.tnt.trainer.modifymyinfo

import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState
import java.io.File

internal class TrainerModifyMyInfoContract {
    data class TrainerModifyMyInfoUiState(
        val profileImage: String = "",
        val name: String = "",
        val dialogState: DialogState = DialogState.NONE,
    ) : UiState {
        enum class DialogState {
            NONE,
            CONFIRM_EXIT,
        }
    }

    sealed interface TrainerModifyMyInfoUiEvent : UiEvent {
        data object OnClickBack : TrainerModifyMyInfoUiEvent
        data object OnClickComplete : TrainerModifyMyInfoUiEvent
        data class OnNameChange(val name: String) : TrainerModifyMyInfoUiEvent
        data class OnProfileImageSelect(val image: File) : TrainerModifyMyInfoUiEvent
        data object OnClickProfileEdit : TrainerModifyMyInfoUiEvent
        data object OnDismissDialog : TrainerModifyMyInfoUiEvent
    }

    sealed interface TrainerModifyMyInfoEffect : UiSideEffect {
        data class ShowToast(val message: String) : TrainerModifyMyInfoEffect
        data object NavigateToPrevious : TrainerModifyMyInfoEffect
    }
}
