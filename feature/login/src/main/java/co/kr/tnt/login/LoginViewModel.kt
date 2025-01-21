package co.kr.tnt.login

import co.kr.tnt.login.LoginContract.LoginSideEffect
import co.kr.tnt.login.LoginContract.LoginUiEvent
import co.kr.tnt.login.LoginContract.LoginUiState
import co.kr.tnt.login.model.TermState
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor() : BaseViewModel<LoginUiState, LoginUiEvent, LoginSideEffect>(
    LoginUiState(),
) {
    override suspend fun handleEvent(event: LoginUiEvent) {
        when (event) {
            LoginUiEvent.OnClickKakaoLogin -> sendEffect(LoginSideEffect.ShowTermBottomSheet)
            LoginUiEvent.OnCheckAllTermAgree -> checkAllTerms()
            is LoginUiEvent.OnCheckTerm -> checkTerm(event.termState)
            is LoginUiEvent.OnClickTermLink -> TODO()
            LoginUiEvent.OnClickNext -> navigateToNext()
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

    private fun navigateToNext() {
        // TODO 회원가입이 되어있는 유저인 경우 홈, 그렇지 않은 경우 회원가입 화면 이동
        sendEffect(LoginSideEffect.NavigateToSignup)
    }
}
