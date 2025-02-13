package co.kr.tnt.trainee.mypage

import android.util.Log
import androidx.lifecycle.viewModelScope
import co.kr.tnt.domain.repository.LoginRepository
import co.kr.tnt.domain.repository.SettingRepository
import co.kr.tnt.domain.repository.TraineeRepository
import co.kr.tnt.domain.utils.AppUrls
import co.kr.tnt.login.kakao.KakaoLoginSdk
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
    private val loginRepository: LoginRepository,
    private val kakaoLoginSdk: KakaoLoginSdk,
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

                is TraineeMyPageUiEvent.OnToggleNotification -> handleToggleNotification(
                    isGrantedPermission = event.isGrantedPermission,
                    shouldShowRationale = event.shouldShowRationale,
                )
                TraineeMyPageUiEvent.OnClickTermsOfService -> sendEffect(
                    TraineeMyPageEffect.NavigateToWebView(AppUrls.TERMS_OF_SERVICE_URL),
                )

                TraineeMyPageUiEvent.OnClickPrivacy -> sendEffect(
                    TraineeMyPageEffect.NavigateToWebView(AppUrls.PRIVACY_POLICY_URL),
                )

                TraineeMyPageUiEvent.OnClickOpenSource -> sendEffect(
                    TraineeMyPageEffect.NavigateToOpenSourceLicense,
                )

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
                    Log.d("test", it.toString())
                    sendEffect(TraineeMyPageEffect.ShowToast("서버 요청에 실패했어요"))
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

        private fun handleToggleNotification(
            isGrantedPermission: Boolean,
            shouldShowRationale: Boolean,
        ) {
            viewModelScope.launch {
                if (currentState.isEnablePushNotification) {
                    settingRepository.setEnablePushNotification(isEnable = false)
                    return@launch
                }

                if (isGrantedPermission) {
                    settingRepository.setEnablePushNotification(isEnable = currentState.isEnablePushNotification.not())
                    return@launch
                }

                if (shouldShowRationale.not()) {
                    sendEffect(TraineeMyPageEffect.RequestPermission(isExplicitlyDenied = false))
                    return@launch
                }

                if (shouldShowRationale) {
                    updateState { copy(dialogState = DialogState.SHOULD_ALLOW_PERMISSION) }
                }
            }
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

                DialogState.SHOULD_ALLOW_PERMISSION -> {
                    updateState { copy(dialogState = DialogState.NONE) }
                    sendEffect(TraineeMyPageEffect.RequestPermission(isExplicitlyDenied = true))
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
                    sendEffect(TraineeMyPageEffect.ShowToast("로그아웃에 실패하였습니다."))
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
                    sendEffect(TraineeMyPageEffect.ShowToast("탈퇴에 실패하였습니다."))
                }
            }
        }
    }
