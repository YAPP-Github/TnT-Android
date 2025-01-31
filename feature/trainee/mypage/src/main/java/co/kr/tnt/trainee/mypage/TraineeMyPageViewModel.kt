package co.kr.tnt.trainee.mypage

import androidx.lifecycle.viewModelScope
import co.kr.tnt.trainee.mypage.TraineeMyPageContract.TraineeMyPageEffect
import co.kr.tnt.trainee.mypage.TraineeMyPageContract.TraineeMyPageUiEvent
import co.kr.tnt.trainee.mypage.TraineeMyPageContract.TraineeMyPageUiState
import co.kr.tnt.trainee.mypage.model.DialogState
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TraineeMyPageViewModel @Inject constructor() :
    BaseViewModel<TraineeMyPageUiState, TraineeMyPageUiEvent, TraineeMyPageEffect>(
        TraineeMyPageUiState(),
    ) {
        override suspend fun handleEvent(event: TraineeMyPageUiEvent) {
            when (event) {
                TraineeMyPageUiEvent.OnEditButtonClick -> navigateToPersonalInfo()
                TraineeMyPageUiEvent.OnConnectButtonClick -> navigateToConnect()
                TraineeMyPageUiEvent.OnDisconnectButtonClick -> showDisconnectPopup()
                TraineeMyPageUiEvent.ToggleNotification -> togglePushNotification()
                TraineeMyPageUiEvent.OnServiceTermClick -> openWebView()
                TraineeMyPageUiEvent.OnPrivacyClick -> openWebView()
                TraineeMyPageUiEvent.OnOpenSourceClick -> navigateToOpenSource()
                TraineeMyPageUiEvent.OnLogoutClick -> logout()
                TraineeMyPageUiEvent.OnDeleteAccountClick -> deleteAccount()
                TraineeMyPageUiEvent.OnConfirmWarningDialog -> confirmWarningDialog()
                TraineeMyPageUiEvent.OnConfirmCompleteDialog -> handleCompleteDialogConfirm()
                TraineeMyPageUiEvent.OnDismissPopup -> dismissPopup()
            }
        }

        init {
            loadUserData()
        }

        private fun loadUserData() {
            viewModelScope.launch {
                // TODO 유저 정보 API 호출
                updateState {
                    copy(
                        image = null,
                        name = "김회원",
                        trainerName = "",
                        isConnected = true,
                        isPushEnabled = true,
                        appVersion = "0.0.0",
                    )
                }
            }
        }

        private fun navigateToPersonalInfo() {
            // TODO 개인 정보 수정 화면 연결
        }

        private fun navigateToConnect() {
            sendEffect(TraineeMyPageEffect.NavigateToConnect)
        }

        private fun showDisconnectPopup() {
            // TODO 트레이너 이름 불러오기? -> API 나오면 수정
            updateState {
                copy(
                    trainerName = "김피티",
                    showWarningDialog = true,
                    dialogState = DialogState.DISCONNECT,
                )
            }
        }

        private fun togglePushNotification() {
            updateState { copy(isPushEnabled = !isPushEnabled) }
        }

        private fun openWebView() {
            // TODO Url API 호출?
            val url = "https://www.naver.com"
            updateState {
                copy(
                    showWebView = !showWebView,
                    url = url,
                )
            }
            sendEffect(TraineeMyPageEffect.NavigateToWebView(url))
        }

        private fun navigateToOpenSource() {
            // TODO 오픈소스 텍스트 띄우기
        }

        private fun logout() {
            updateState {
                copy(
                    showWarningDialog = true,
                    dialogState = DialogState.LOGOUT,
                )
            }
        }

        private fun deleteAccount() {
            updateState {
                copy(
                    showWarningDialog = true,
                    dialogState = DialogState.DELETE_ACCOUNT,
                )
            }
        }

        private fun confirmWarningDialog() {
            updateState {
                copy(
                    showWarningDialog = false,
                    showCompleteDialog = true,
                )
            }
        }

        private fun handleCompleteDialogConfirm() {
            when (currentState.dialogState) {
                DialogState.LOGOUT -> performLogout()
                DialogState.DELETE_ACCOUNT -> performAccountDeletion()
                DialogState.DISCONNECT -> performDisconnect()
            }
        }

        private fun dismissPopup() {
            updateState {
                copy(
                    showWarningDialog = false,
                    showCompleteDialog = false,
                )
            }
        }

        private fun performLogout() {
            viewModelScope.launch {
                // TODO 로그아웃 API 호출
                updateState { copy(showCompleteDialog = false) }
                sendEffect(TraineeMyPageEffect.NavigateToLogin)
            }
        }

        private fun performAccountDeletion() {
            viewModelScope.launch {
                // TODO 회원 탈퇴 API 호출
                updateState { copy(showCompleteDialog = false) }
                sendEffect(TraineeMyPageEffect.NavigateToLogin)
            }
        }

        private fun performDisconnect() {
            viewModelScope.launch {
                // TODO 연결 해제 API 호출
                updateState {
                    copy(
                        isConnected = false,
                        showCompleteDialog = false,
                    )
                }
            }
        }
    }
