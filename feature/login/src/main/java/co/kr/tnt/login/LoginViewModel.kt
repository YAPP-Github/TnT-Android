package co.kr.tnt.login

import androidx.lifecycle.viewModelScope
import co.kr.tnt.core.ui.R.string.core_failed_to_server_request
import co.kr.tnt.domain.model.AuthType
import co.kr.tnt.domain.model.LoginResult
import co.kr.tnt.domain.repository.LoginRepository
import co.kr.tnt.feature.login.R
import co.kr.tnt.login.LoginContract.LoginSideEffect
import co.kr.tnt.login.LoginContract.LoginUiEvent
import co.kr.tnt.login.LoginContract.LoginUiState
import co.kr.tnt.login.model.TermState
import co.kr.tnt.ui.base.BaseViewModel
import co.kr.tnt.ui.resource.DisplayText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
) : BaseViewModel<LoginUiState, LoginUiEvent, LoginSideEffect>(
        LoginUiState(),
    ) {
    private var loginResult: LoginResult? = null
    private var messagingToken: String = ""

    override suspend fun handleEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.OnAuthSuccess -> login(event.authType, event.accessToken)

            is LoginUiEvent.OnAuthFail -> {
                if (event.throwable !is LoginException.CancelException) {
                    sendEffect(
                        LoginSideEffect.ShowToast(
                            DisplayText.Resource(R.string.failed_login_try_again),
                        ),
                    )
                }
            }

            LoginUiEvent.OnCheckAllTermAgree -> checkAllTerms()
            is LoginUiEvent.OnCheckTerm -> checkTerm(event.termState)

            is LoginUiEvent.OnClickTermLink -> {
                sendEffect(LoginSideEffect.NavigateToWebView(event.link))
            }

            LoginUiEvent.OnClickNext -> navigateToSignup()
            is LoginUiEvent.OnGetMessagingTokenSucceed -> messagingToken = event.token
        }
    }

    private fun login(
        authType: AuthType,
        accessToken: String,
    ) {
        viewModelScope.launch {
            runCatching {
                loginRepository.login(
                    authType = authType,
                    accessToken = accessToken,
                    messagingToken = messagingToken,
                )
            }.onSuccess { loginResult ->
                loginResult.userType?.let { userType ->
                    if (loginResult.isSignUp) {
                        sendEffect(LoginSideEffect.NavigateToHome(userType))
                        return@onSuccess
                    }
                }

                this@LoginViewModel.loginResult = loginResult
                clearAllChecks()
                sendEffect(LoginSideEffect.ShowTermBottomSheet)
            }.onFailure {
                sendEffect(
                    LoginSideEffect.ShowToast(
                        DisplayText.Resource(core_failed_to_server_request),
                    ),
                )
            }
        }
    }

    private fun checkAllTerms() {
        updateState {
            copy(
                terms = terms.keys.associateWith {
                    !isAllTermChecked()
                },
            )
        }
    }

    private fun checkTerm(termState: TermState) {
        updateState {
            copy(
                terms = terms.toMutableMap()
                    .also { terms -> terms[termState] = !(terms[termState] ?: false) }
                    .toMap(),
            )
        }
    }

    private fun clearAllChecks() {
        updateState {
            copy(
                terms = terms.keys.associateWith { false },
            )
        }
    }

    private fun navigateToSignup() {
        loginResult?.let { loginResult ->
            sendEffect(LoginSideEffect.NavigateToSignup(loginResult, messagingToken))
            this@LoginViewModel.loginResult = null
        } ?: run {
            sendEffect(
                LoginSideEffect.ShowToast(
                    DisplayText.Resource(R.string.failed_login_try_again),
                ),
            )
        }
    }
}
