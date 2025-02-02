package co.kr.tnt.login

import androidx.lifecycle.viewModelScope
import co.kr.tnt.domain.model.AuthType
import co.kr.tnt.domain.model.LoginResult
import co.kr.tnt.domain.repository.LoginRepository
import co.kr.tnt.login.LoginContract.LoginSideEffect
import co.kr.tnt.login.LoginContract.LoginUiEvent
import co.kr.tnt.login.LoginContract.LoginUiState
import co.kr.tnt.login.model.TermState
import co.kr.tnt.ui.base.BaseViewModel
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

    override suspend fun handleEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.OnAuthSuccess -> login(event.authType, event.accessToken)

            is LoginUiEvent.OnAuthFail -> {
                if (event.throwable !is LoginException.CancelException) {
                    sendEffect(LoginSideEffect.ShowToast("로그인에 실패하였습니다. 다시 시도해주세요."))
                }
            }

            LoginUiEvent.OnCheckAllTermAgree -> checkAllTerms()
            is LoginUiEvent.OnCheckTerm -> checkTerm(event.termState)
            is LoginUiEvent.OnClickTermLink -> TODO()
            LoginUiEvent.OnClickNext -> navigateToSignup()
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
                )
            }.onSuccess { loginResult ->
                if (loginResult.isSignUp) {
                    sendEffect(LoginSideEffect.NavigateToHome)
                    return@onSuccess
                }

                this@LoginViewModel.loginResult = loginResult
                sendEffect(LoginSideEffect.ShowTermBottomSheet)
            }.onFailure {
                // TODO resource
                sendEffect(LoginSideEffect.ShowToast("알 수 없는 오류가 발생했습니다. 다시 시도해주세요."))
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

    private fun navigateToSignup() {
        loginResult?.let { loginResult ->
            sendEffect(LoginSideEffect.NavigateToSignup(loginResult))
            this@LoginViewModel.loginResult = null
        } ?: run {
            sendEffect(LoginSideEffect.ShowToast("로그인에 실패하였습니다. 다시 시도해주세요."))
        }
    }
}
