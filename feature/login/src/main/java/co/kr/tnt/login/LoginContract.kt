package co.kr.tnt.login

import co.kr.tnt.domain.model.Term
import co.kr.tnt.login.model.TermState
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState

internal class LoginContract {
    data class LoginUiState(
        val terms: Map<TermState, Boolean> = Term.entries.associate { term ->
            TermState.fromDomain(term) to false
        },
    ) : UiState {
        fun isAllTermChecked() = terms.all { it.value }
    }

    sealed interface LoginUiEvent : UiEvent {
        data object OnClickKakaoLogin : LoginUiEvent
        data object OnCheckAllTermAgree : LoginUiEvent
        data class OnCheckTerm(val termState: TermState) : LoginUiEvent
        data class OnClickTermLink(val link: String) : LoginUiEvent
        data object OnClickNext : LoginUiEvent
    }

    sealed interface LoginSideEffect : UiSideEffect {
        data object ShowTermBottomSheet : LoginSideEffect
        data object NavigateToHome : LoginSideEffect
        data object NavigateToSignup : LoginSideEffect
    }
}
