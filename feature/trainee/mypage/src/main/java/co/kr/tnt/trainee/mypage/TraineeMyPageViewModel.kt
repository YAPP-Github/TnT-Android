package co.kr.tnt.trainee.mypage

import co.kr.tnt.trainee.mypage.TraineeMyPageContract.TraineeMyPageEffect
import co.kr.tnt.trainee.mypage.TraineeMyPageContract.TraineeMyPageUiEvent
import co.kr.tnt.trainee.mypage.TraineeMyPageContract.TraineeMyPageUiState
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class TraineeMyPageViewModel @Inject constructor() :
    BaseViewModel<TraineeMyPageUiState, TraineeMyPageUiEvent, TraineeMyPageEffect>(
        TraineeMyPageUiState(),
    ) {
        override suspend fun handleEvent(event: TraineeMyPageUiEvent) {
            when (event) {
                TraineeMyPageUiEvent.OnConnectClick -> TODO()
                TraineeMyPageUiEvent.ToggleNotification -> TODO()
            }
        }
    }
