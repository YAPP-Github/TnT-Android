package co.kr.tnt.connect.trainer

import TrainerConnectContract.TrainerConnectSideEffect
import TrainerConnectContract.TrainerConnectUiEvent
import TrainerConnectContract.TrainerConnectUiState
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class TrainerConnectViewModel @Inject constructor() :
    BaseViewModel<TrainerConnectUiState, TrainerConnectUiEvent, TrainerConnectSideEffect>(
        TrainerConnectUiState(),
    ) {
        override suspend fun handleEvent(event: TrainerConnectUiEvent) {
            when (event) {
                is TrainerConnectUiEvent.OnRegenerateClick -> regenerateCode()
                TrainerConnectUiEvent.OnSkipClick -> navigateToHome()
            }
        }

        fun generateCode() {
            // TODO 코드 발급
            val code = "12345678"
            updateState { copy(inviteCode = code) }
        }

        private fun regenerateCode() {
            // TODO 코드 재발급
            val newCode = "87654321"
            updateState { copy(inviteCode = newCode) }
        }

        fun navigateToBack() {
            sendEffect(TrainerConnectSideEffect.NavigateToBack)
        }

        private fun navigateToHome() {
            sendEffect(TrainerConnectSideEffect.NavigateToHome)
        }
    }
