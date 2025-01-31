package co.kr.tnt.trainee.mypage

import androidx.lifecycle.viewModelScope
import co.kr.tnt.trainee.mypage.TraineeMyPageContract.TraineeMyPageEffect
import co.kr.tnt.trainee.mypage.TraineeMyPageContract.TraineeMyPageUiEvent
import co.kr.tnt.trainee.mypage.TraineeMyPageContract.TraineeMyPageUiState
import co.kr.tnt.trainee.mypage.model.PopupType
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
                TraineeMyPageUiEvent.OnWebViewBackClick -> dismissWebView()
                TraineeMyPageUiEvent.OnOpenSourceClick -> navigateToOpenSource()
                TraineeMyPageUiEvent.OnLogoutClick -> logout()
                TraineeMyPageUiEvent.OnDeleteAccountClick -> deleteAccount()
                TraineeMyPageUiEvent.OnBackClick -> navigateBack()
                TraineeMyPageUiEvent.OnConfirmFirstPopup -> confirmFirstPopup()
                TraineeMyPageUiEvent.OnConfirmSecondPopup -> handleSecondPopupConfirm()
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
                    showFirstPopup = true,
                    popupType = PopupType.DISCONNECT,
                )
            }
        }

        private fun togglePushNotification() {
            updateState { copy(isPushEnabled = !isPushEnabled) }
        }

        private fun openWebView() {
            // TODO Url API 호출?
            val url = "https://www.naver.com"
            updateState { copy(showWebView = !showWebView, url = url) }
        }

        private fun dismissWebView() {
            updateState { copy(showWebView = !showWebView) }
        }

        private fun navigateToOpenSource() {
            // TODO 오픈소스 텍스트 띄우기
        }

        private fun logout() {
            updateState { copy(showFirstPopup = true, popupType = PopupType.LOGOUT) }
        }

        private fun deleteAccount() {
            updateState { copy(showFirstPopup = true, popupType = PopupType.DELETE_ACCOUNT) }
        }

        private fun navigateBack() {
            sendEffect(TraineeMyPageEffect.NavigateToPrevious)
        }

        private fun confirmFirstPopup() {
            updateState { copy(showFirstPopup = false, showSecondPopup = true) }
        }

        private fun dismissPopup() {
            updateState { copy(showFirstPopup = false, showSecondPopup = false) }
        }

        private fun handleSecondPopupConfirm() {
            when (currentState.popupType) {
                PopupType.LOGOUT -> performLogout()
                PopupType.DELETE_ACCOUNT -> performAccountDeletion()
                PopupType.DISCONNECT -> performDisconnect()
            }
        }

        private fun performLogout() {
            viewModelScope.launch {
                // TODO 로그아웃 API 호출
                updateState { copy(showSecondPopup = false) }
                sendEffect(TraineeMyPageEffect.NavigateToLogin)
            }
        }

        private fun performAccountDeletion() {
            viewModelScope.launch {
                // TODO 회원 탈퇴 API 호출
                updateState { copy(showSecondPopup = false) }
                sendEffect(TraineeMyPageEffect.NavigateToLogin)
            }
        }

        private fun performDisconnect() {
            viewModelScope.launch {
                // TODO 연결 해제 API 호출
                updateState { copy(isConnected = false, showSecondPopup = false) }
            }
        }
    }
