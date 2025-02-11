package co.kr.tnt.trainer.addptsession

import co.kr.tnt.trainer.addptsession.AddPtSessionContract.AddPtSessionSideEffect
import co.kr.tnt.trainer.addptsession.AddPtSessionContract.AddPtSessionUiEvent
import co.kr.tnt.trainer.addptsession.AddPtSessionContract.AddPtSessionUiState
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class AddPtSessionViewModel @Inject constructor() :
    BaseViewModel<AddPtSessionUiState, AddPtSessionUiEvent, AddPtSessionSideEffect>(
        AddPtSessionUiState(),
    ) {
        override suspend fun handleEvent(event: AddPtSessionUiEvent) {
            when (event) {
                is AddPtSessionUiEvent.OnChangeMemo -> {}
                AddPtSessionUiEvent.OnClickBack -> {}
                AddPtSessionUiEvent.OnClickComplete -> {}
                AddPtSessionUiEvent.OnClickDate -> {}
                AddPtSessionUiEvent.OnClickEndTime -> {}
                AddPtSessionUiEvent.OnClickMember -> {}
                is AddPtSessionUiEvent.OnClickMinuteChip -> {}
                AddPtSessionUiEvent.OnClickStartTime -> {}
                is AddPtSessionUiEvent.OnSelectDate -> {}
                is AddPtSessionUiEvent.OnSelectEndTime -> {}
                is AddPtSessionUiEvent.OnSelectMember -> {}
                is AddPtSessionUiEvent.OnSelectStartTime -> {}
            }
        }
    }
