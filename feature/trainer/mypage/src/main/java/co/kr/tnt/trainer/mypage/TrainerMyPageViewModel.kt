package co.kr.tnt.trainer.mypage

import co.kr.tnt.trainer.mypage.TrainerMyPageContract.TrainerMyPageSideEffect
import co.kr.tnt.trainer.mypage.TrainerMyPageContract.TrainerMyPageUiEvent
import co.kr.tnt.trainer.mypage.TrainerMyPageContract.TrainerMyPageUiState
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class TrainerMyPageViewModel @Inject constructor() :
    BaseViewModel<TrainerMyPageUiState, TrainerMyPageUiEvent, TrainerMyPageSideEffect>(TrainerMyPageUiState()) {
        override suspend fun handleEvent(event: TrainerMyPageUiEvent) {
            when (event) {
                TrainerMyPageUiEvent.OnTogglePushNotification -> TODO()
                TrainerMyPageUiEvent.OnClickPrivacy -> TODO()
                TrainerMyPageUiEvent.OnClickServiceTerm -> TODO()
                TrainerMyPageUiEvent.OnClickOpenSourceLicense -> TODO()
                TrainerMyPageUiEvent.OnClickLogout -> TODO()
                TrainerMyPageUiEvent.OnClickDeleteAccount -> TODO()
                TrainerMyPageUiEvent.OnDismissDialog -> TODO()
                TrainerMyPageUiEvent.OnClickDialogConfirm -> TODO()
            }
        }
    }
