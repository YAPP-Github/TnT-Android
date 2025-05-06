package co.kr.tnt.login

import co.kr.tnt.domain.model.AuthType
import co.kr.tnt.domain.model.LoginResult
import co.kr.tnt.domain.model.Term
import co.kr.tnt.domain.model.UserType
import co.kr.tnt.login.model.TermState
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState
import co.kr.tnt.ui.resource.DisplayText

internal class LoginContract {
    data class LoginUiState(
        val terms: Map<TermState, Boolean> = Term.entries.associate { term ->
            TermState.fromDomain(term) to false
        },
    ) : UiState {
        fun isAllTermChecked() = terms.all { it.value }
    }

    sealed interface LoginUiEvent : UiEvent {
        data class OnAuthSuccess(
            val authType: AuthType,
            val accessToken: String,
        ) : LoginUiEvent

        data class OnAuthFail(val throwable: Throwable) : LoginUiEvent
        data object OnCheckAllTermAgree : LoginUiEvent
        data class OnCheckTerm(val termState: TermState) : LoginUiEvent
        data class OnClickTermLink(val link: String) : LoginUiEvent
        data object OnClickNext : LoginUiEvent
        data class OnGetMessagingTokenSucceed(val token: String) : LoginUiEvent
    }

    sealed interface LoginSideEffect : UiSideEffect {
        data object ShowTermBottomSheet : LoginSideEffect
        data class ShowToast(val message: DisplayText) : LoginSideEffect
        data class NavigateToWebView(val url: String) : LoginSideEffect
        data class NavigateToHome(val userType: UserType) : LoginSideEffect
        data class NavigateToSignup(
            val loginResult: LoginResult,
            val messagingToken: String,
        ) : LoginSideEffect
    }
}
