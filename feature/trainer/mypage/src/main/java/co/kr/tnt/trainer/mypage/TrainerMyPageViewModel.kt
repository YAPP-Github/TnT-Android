package co.kr.tnt.trainer.mypage

import androidx.lifecycle.viewModelScope
import co.kr.tnt.domain.repository.LoginRepository
import co.kr.tnt.login.kakao.KakaoLoginSdk
import co.kr.tnt.trainer.mypage.TrainerMyPageContract.TrainerMyPageSideEffect
import co.kr.tnt.trainer.mypage.TrainerMyPageContract.TrainerMyPageUiEvent
import co.kr.tnt.trainer.mypage.TrainerMyPageContract.TrainerMyPageUiState
import co.kr.tnt.trainer.mypage.TrainerMyPageContract.TrainerMyPageUiState.DialogState
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TrainerMyPageViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val kakaoLoginSdk: KakaoLoginSdk,
) : BaseViewModel<TrainerMyPageUiState, TrainerMyPageUiEvent, TrainerMyPageSideEffect>(TrainerMyPageUiState()) {
    override suspend fun handleEvent(event: TrainerMyPageUiEvent) {
        when (event) {
            TrainerMyPageUiEvent.OnTogglePushNotification -> TODO()
            TrainerMyPageUiEvent.OnClickPrivacy -> TODO()
            TrainerMyPageUiEvent.OnClickServiceTerm -> TODO()
            TrainerMyPageUiEvent.OnClickOpenSourceLicense -> TODO()
            TrainerMyPageUiEvent.OnClickLogout -> updateState { copy(dialogState = DialogState.LOGOUT_CONFIRM) }
            TrainerMyPageUiEvent.OnClickDeleteAccount -> updateState {
                copy(dialogState = DialogState.DELETE_ACCOUNT_CONFIRM)
            }
            TrainerMyPageUiEvent.OnDismissDialog -> updateState { copy(dialogState = DialogState.NONE) }
            TrainerMyPageUiEvent.OnClickDialogConfirm -> handleDialogConfirm()
        }
    }

    private fun handleDialogConfirm() {
        when (currentState.dialogState) {
            DialogState.NONE -> Unit
            DialogState.LOGOUT_CONFIRM -> logout()
            DialogState.LOGOUT -> {
                updateState { copy(dialogState = DialogState.NONE) }
                sendEffect(TrainerMyPageSideEffect.NavigateToLogin)
            }
            DialogState.DELETE_ACCOUNT_CONFIRM -> withdraw()
            DialogState.DELETE_ACCOUNT -> {
                updateState { copy(dialogState = DialogState.NONE) }
                sendEffect(TrainerMyPageSideEffect.NavigateToLogin)
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            runCatching {
                loginRepository.logout()
                kakaoLoginSdk.logout()
            }.onSuccess {
                updateState { copy(dialogState = DialogState.LOGOUT) }
            }.onFailure {
                sendEffect(TrainerMyPageSideEffect.ShowToast("로그아웃에 실패하였습니다."))
            }
        }
    }

    private fun withdraw() {
        viewModelScope.launch {
            runCatching {
                loginRepository.withdraw()
                kakaoLoginSdk.unlink()
            }.onSuccess {
                updateState { copy(dialogState = DialogState.DELETE_ACCOUNT) }
            }.onFailure {
                sendEffect(TrainerMyPageSideEffect.ShowToast("탈퇴에 실패하였습니다."))
            }
        }
    }
}
