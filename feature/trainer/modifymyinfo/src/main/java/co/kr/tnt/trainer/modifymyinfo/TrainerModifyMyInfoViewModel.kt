package co.kr.tnt.trainer.modifymyinfo

import co.kr.tnt.trainer.modifymyinfo.TrainerModifyMyInfoContract.TrainerModifyMyInfoEffect
import co.kr.tnt.trainer.modifymyinfo.TrainerModifyMyInfoContract.TrainerModifyMyInfoUiEvent
import co.kr.tnt.trainer.modifymyinfo.TrainerModifyMyInfoContract.TrainerModifyMyInfoUiState
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
                TrainerModifyMyInfoUiEvent.OnClickBack -> TODO()
                TrainerModifyMyInfoUiEvent.OnClickComplete -> TODO()
                is TrainerModifyMyInfoUiEvent.OnNameChange -> TODO()
                is TrainerModifyMyInfoUiEvent.OnProfileImageSelect -> TODO()
                TrainerModifyMyInfoUiEvent.OnClickProfileEdit -> TODO()
                TrainerModifyMyInfoUiEvent.OnDismissDialog -> TODO()
            }
        }
    }
