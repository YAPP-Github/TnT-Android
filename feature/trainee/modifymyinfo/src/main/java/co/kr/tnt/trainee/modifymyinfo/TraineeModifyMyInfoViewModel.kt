package co.kr.tnt.trainee.modifymyinfo

import co.kr.tnt.trainee.modifymyinfo.TraineeModifyMyInfoContract.TraineeModifyMyInfoEffect
import co.kr.tnt.trainee.modifymyinfo.TraineeModifyMyInfoContract.TraineeModifyMyInfoUiEvent
import co.kr.tnt.trainee.modifymyinfo.TraineeModifyMyInfoContract.TraineeModifyMyInfoUiState
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class TraineeModifyMyInfoViewModel @Inject constructor() :
    BaseViewModel<TraineeModifyMyInfoUiState, TraineeModifyMyInfoUiEvent, TraineeModifyMyInfoEffect>(
        TraineeModifyMyInfoUiState,
    ) {
        override suspend fun handleEvent(event: TraineeModifyMyInfoUiEvent) {
            TODO("Not yet implemented")
        }
    }
