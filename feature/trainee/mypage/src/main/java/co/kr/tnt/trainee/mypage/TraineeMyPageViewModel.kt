package co.kr.tnt.trainee.mypage

import android.util.Log
import androidx.lifecycle.viewModelScope
import co.kr.tnt.domain.repository.SettingRepository
import co.kr.tnt.domain.repository.TraineeRepository
import co.kr.tnt.domain.utils.AppUrls
import co.kr.tnt.trainee.mypage.TraineeMyPageContract.TraineeMyPageEffect
import co.kr.tnt.trainee.mypage.TraineeMyPageContract.TraineeMyPageUiEvent
import co.kr.tnt.trainee.mypage.TraineeMyPageContract.TraineeMyPageUiState
import co.kr.tnt.trainee.mypage.TraineeMyPageContract.TraineeMyPageUiState.DialogState
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TraineeMyPageViewModel @Inject constructor(
    private val traineeRepository: TraineeRepository,
    private val settingRepository: SettingRepository,
) :
    BaseViewModel<TraineeMyPageUiState, TraineeMyPageUiEvent, TraineeMyPageEffect>(
            TraineeMyPageUiState(),
        ) {
        init {
            loadUserData()
        }

        override suspend fun handleEvent(event: TraineeMyPageUiEvent) {
            when (event) {
                TraineeMyPageUiEvent.OnClickConnect -> navigateToConnect()
                TraineeMyPageUiEvent.OnClickDisconnect -> updateState {
                    copy(dialogState = DialogState.DISCONNECT_CONFIRM)
                }

                TraineeMyPageUiEvent.ToggleNotification -> togglePushNotification()
                TraineeMyPageUiEvent.OnClickTermsOfService -> sendEffect(
                    TraineeMyPageEffect.NavigateToWebView(AppUrls.TERMS_OF_SERVICE_URL),
                )

                TraineeMyPageUiEvent.OnClickPrivacy -> sendEffect(
                    TraineeMyPageEffect.NavigateToWebView(AppUrls.PRIVACY_POLICY_URL),
                )

                TraineeMyPageUiEvent.OnClickOpenSource -> navigateToOpenSource()
                TraineeMyPageUiEvent.OnClickLogout -> updateState { copy(dialogState = DialogState.LOGOUT_CONFIRM) }
                TraineeMyPageUiEvent.OnClickDeleteAccount -> updateState {
                    copy(
                        dialogState = DialogState.DELETE_ACCOUNT_CONFIRM,
                    )
                }

                TraineeMyPageUiEvent.OnClickDialogConfirm -> handleDialogConfirm()
                TraineeMyPageUiEvent.OnDismissDialog -> updateState { copy(dialogState = DialogState.NONE) }
            }
        }

        private fun loadUserData() {
            viewModelScope.launch {
                runCatching {
                    traineeRepository.getMyInfo()
                }.onSuccess { user ->
                    updateState { copy(user = user) }
                }.onFailure {
                    sendEffect(TraineeMyPageEffect.ShowToast("서버 요청에 실패했어요"))
                    Log.d("debugginh", it.toString())
                }

                settingRepository.isEnablePushNotification()
                    .onEach { isEnablePushNotification ->
                        updateState { copy(isEnablePushNotification = isEnablePushNotification) }
                    }
                    .launchIn(viewModelScope)
            }
        }

        private fun navigateToConnect() {
            sendEffect(TraineeMyPageEffect.NavigateToConnect)
        }

        private fun togglePushNotification() {
            updateState { copy(isEnablePushNotification = !isEnablePushNotification) }
        }

        private fun navigateToOpenSource() {
            // TODO 오픈소스 텍스트 띄우기
        }

        private fun handleDialogConfirm() {
            when (currentState.dialogState) {
                DialogState.NONE -> Unit
                DialogState.LOGOUT_CONFIRM -> logout()
                DialogState.LOGOUT -> {
                    updateState { copy(dialogState = DialogState.NONE) }
                    sendEffect(TraineeMyPageEffect.NavigateToLogin)
                }

                DialogState.DELETE_ACCOUNT_CONFIRM -> withdraw()
                DialogState.DELETE_ACCOUNT -> {
                    updateState { copy(dialogState = DialogState.NONE) }
                    sendEffect(TraineeMyPageEffect.NavigateToLogin)
                }

                DialogState.DISCONNECT_CONFIRM -> disconnect()
                DialogState.DISCONNECT -> updateState { copy(dialogState = DialogState.NONE) }
                DialogState.SHOULD_ALLOW_PERMISSION -> {
                    updateState { copy(dialogState = DialogState.NONE) }
                    // TODO 알림 권한 설정
                }
            }
        }

        private fun logout() {
            viewModelScope.launch {
                // TODO 로그아웃 API 호출
                updateState { copy(dialogState = DialogState.LOGOUT) }
            }
        }

        private fun withdraw() {
            viewModelScope.launch {
                // TODO 회원 탈퇴 API 호출
                updateState { copy(dialogState = DialogState.DELETE_ACCOUNT) }
            }
        }

        private fun disconnect() {
            viewModelScope.launch {
                // TODO 연결 해제 API 호출
                updateState { copy(isConnected = false, dialogState = DialogState.DISCONNECT) }
            }
        }
    }
