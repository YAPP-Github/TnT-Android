package co.kr.tnt.trainer.modifymyinfo

import co.kr.tnt.trainer.modifymyinfo.TrainerModifyMyInfoContract.TrainerModifyMyInfoEffect
import co.kr.tnt.trainer.modifymyinfo.TrainerModifyMyInfoContract.TrainerModifyMyInfoUiEvent
import co.kr.tnt.trainer.modifymyinfo.TrainerModifyMyInfoContract.TrainerModifyMyInfoUiState
import co.kr.tnt.trainer.modifymyinfo.TrainerModifyMyInfoContract.TrainerModifyMyInfoUiState.DialogState
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class TrainerModifyMyInfoViewModel @Inject constructor() :
    BaseViewModel<TrainerModifyMyInfoUiState, TrainerModifyMyInfoUiEvent, TrainerModifyMyInfoEffect>(
        TrainerModifyMyInfoUiState(),
    ) {
        override suspend fun handleEvent(event: TrainerModifyMyInfoUiEvent) {
            when (event) {
                TrainerModifyMyInfoUiEvent.OnClickBack -> sendEffect(TrainerModifyMyInfoEffect.NavigateToPrevious)
                TrainerModifyMyInfoUiEvent.OnClickComplete -> TODO()
                is TrainerModifyMyInfoUiEvent.OnNameChange -> updateState { copy(name = event.name) }
                is TrainerModifyMyInfoUiEvent.OnProfileImageSelect -> updateState {
                    copy(profileImage = event.image.path)
                }
                TrainerModifyMyInfoUiEvent.OnClickProfileEdit -> TODO()
                TrainerModifyMyInfoUiEvent.OnDismissDialog -> updateState { copy(dialogState = DialogState.NONE) }
            }
        }
    }
