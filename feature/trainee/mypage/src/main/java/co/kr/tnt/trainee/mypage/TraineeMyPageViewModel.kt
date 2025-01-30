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
                TraineeMyPageUiEvent.OnEditButtonClick -> navigateToPersonalInfo()
                TraineeMyPageUiEvent.OnConnectButtonClick -> navigateToConnect()
                TraineeMyPageUiEvent.OnDisconnectButtonClick -> showDisconnectPopup()
                TraineeMyPageUiEvent.ToggleNotification -> togglePushNotification()
                TraineeMyPageUiEvent.OnServiceTermClick -> openWebView(url = "https://www.naver.com")
                TraineeMyPageUiEvent.OnPrivacyClick -> openWebView(url = "https://www.naver.com")
                TraineeMyPageUiEvent.OnOpenSourceClick -> navigateToOpenSource()
                TraineeMyPageUiEvent.OnLogoutClick -> logout()
                TraineeMyPageUiEvent.OnDeleteAccountClick -> deleteAccount()
                TraineeMyPageUiEvent.OnBackClick -> navigateBack()
            }
        }

        private fun navigateToPersonalInfo() {
            // TODO 개인 정보 수정 화면
        }

        private fun navigateToConnect() {
            sendEffect(TraineeMyPageEffect.NavigateToConnect)
        }

        private fun showDisconnectPopup() {
            // TODO 연결 끊기 팝업 노출
        }

        private fun togglePushNotification() {
            updateState { copy(isPushEnabled = !isPushEnabled) }
        }

        private fun openWebView(url: String) {
            // TODO 웹뷰 띄우기
        }

        private fun navigateToOpenSource() {
            // TODO 오픈소스 텍스트 띄우기
        }

        private fun logout() {
            // TODO 로그아웃 팝업 노출
        }

        private fun deleteAccount() {
            // TODO 계정 삭제 팝업 노출
        }

        private fun navigateBack() {
            sendEffect(TraineeMyPageEffect.NavigateToPrevious)
        }
    }
