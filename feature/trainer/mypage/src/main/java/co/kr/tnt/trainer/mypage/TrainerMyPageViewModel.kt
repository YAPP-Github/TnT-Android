package co.kr.tnt.trainer.mypage

import androidx.lifecycle.viewModelScope
import co.kr.tnt.core.ui.R.string.core_failed_to_server_request
import co.kr.tnt.domain.repository.LoginRepository
import co.kr.tnt.domain.repository.SettingRepository
import co.kr.tnt.domain.repository.TrainerRepository
import co.kr.tnt.domain.utils.AppUrls
import co.kr.tnt.feature.trainer.mypage.R
import co.kr.tnt.login.kakao.KakaoLoginSdk
import co.kr.tnt.trainer.mypage.TrainerMyPageContract.TrainerMyPageSideEffect
import co.kr.tnt.trainer.mypage.TrainerMyPageContract.TrainerMyPageUiEvent
import co.kr.tnt.trainer.mypage.TrainerMyPageContract.TrainerMyPageUiState
import co.kr.tnt.trainer.mypage.TrainerMyPageContract.TrainerMyPageUiState.DialogState
import co.kr.tnt.ui.base.BaseViewModel
import co.kr.tnt.ui.resource.DisplayText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TrainerMyPageViewModel @Inject constructor(
    private val trainerRepository: TrainerRepository,
    private val loginRepository: LoginRepository,
    private val settingRepository: SettingRepository,
    private val kakaoLoginSdk: KakaoLoginSdk,
) : BaseViewModel<TrainerMyPageUiState, TrainerMyPageUiEvent, TrainerMyPageSideEffect>(TrainerMyPageUiState()) {
    init {
        viewModelScope.launch {
            runCatching {
                trainerRepository.getMyInfo()
            }.onSuccess { user ->
                updateState { copy(user = user) }
            }.onFailure {
                sendEffect(TrainerMyPageSideEffect.ShowToast(DisplayText.Resource(core_failed_to_server_request)))
            }

            settingRepository.isEnablePushNotification()
                .onEach { isEnablePushNotification ->
                    updateState { copy(isEnablePushNotification = isEnablePushNotification) }
                }
                .launchIn(viewModelScope)
        }
    }

    override suspend fun handleEvent(event: TrainerMyPageUiEvent) {
        when (event) {
            is TrainerMyPageUiEvent.OnTogglePushNotification -> handleToggleNotification(
                isGrantedPermission = event.isGrantedPermission,
                shouldShowRationale = event.shouldShowRationale,
            )

            TrainerMyPageUiEvent.OnClickPrivacy -> sendEffect(
                TrainerMyPageSideEffect.NavigateToWebView(AppUrls.PRIVACY_POLICY_URL),
            )

            TrainerMyPageUiEvent.OnClickTermsOfService -> sendEffect(
                TrainerMyPageSideEffect.NavigateToWebView(AppUrls.TERMS_OF_SERVICE_URL),
            )

            TrainerMyPageUiEvent.OnClickOpenSourceLicense -> sendEffect(
                TrainerMyPageSideEffect.NavigateToOpenSourceLicense,
            )

            TrainerMyPageUiEvent.OnClickLogout -> updateState { copy(dialogState = DialogState.LOGOUT_CONFIRM) }
            TrainerMyPageUiEvent.OnClickDeleteAccount -> updateState {
                copy(dialogState = DialogState.DELETE_ACCOUNT_CONFIRM)
            }

            TrainerMyPageUiEvent.OnDismissDialog -> updateState { copy(dialogState = DialogState.NONE) }
            TrainerMyPageUiEvent.OnClickDialogConfirm -> handleDialogConfirm()
        }
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
                sendEffect(TrainerMyPageSideEffect.RequestPermission(isExplicitlyDenied = false))
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
                sendEffect(TrainerMyPageSideEffect.NavigateToLogin)
            }

            DialogState.DELETE_ACCOUNT_CONFIRM -> withdraw()
            DialogState.DELETE_ACCOUNT -> {
                updateState { copy(dialogState = DialogState.NONE) }
                sendEffect(TrainerMyPageSideEffect.NavigateToLogin)
            }

            DialogState.SHOULD_ALLOW_PERMISSION -> {
                updateState { copy(dialogState = DialogState.NONE) }
                sendEffect(TrainerMyPageSideEffect.RequestPermission(isExplicitlyDenied = true))
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            runCatching {
                loginRepository.logout()
                kakaoLoginSdk.logout()
            }.onSuccess {
                updateState { copy(dialogState = DialogState.LOGOUT) }
            }.onFailure {
                sendEffect(
                    TrainerMyPageSideEffect.ShowToast(
                        DisplayText.Resource(R.string.failed_logout),
                    ),
                )
            }.also {
                updateState { copy(isLoading = false) }
            }
        }
    }

    private fun withdraw() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            runCatching {
                loginRepository.withdraw()
                kakaoLoginSdk.unlink()
            }.onSuccess {
                updateState { copy(dialogState = DialogState.DELETE_ACCOUNT) }
            }.onFailure {
                sendEffect(TrainerMyPageSideEffect.ShowToast(DisplayText.Resource(R.string.failed_delete_account)))
            }.also {
                updateState { copy(isLoading = false) }
            }
        }
    }
}
