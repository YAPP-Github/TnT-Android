package co.kr.tnt.trainer.invite

import androidx.lifecycle.viewModelScope
import co.kr.tnt.domain.repository.ConnectRepository
import co.kr.tnt.trainer.invite.TrainerInviteContract.TrainerInviteSideEffect
import co.kr.tnt.trainer.invite.TrainerInviteContract.TrainerInviteUiEvent
import co.kr.tnt.trainer.invite.TrainerInviteContract.TrainerInviteUiState
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TrainerInviteViewModel @Inject constructor(
    private val connectRepository: ConnectRepository,
) : BaseViewModel<TrainerInviteUiState, TrainerInviteUiEvent, TrainerInviteSideEffect>(
        TrainerInviteUiState(),
    ) {
    override suspend fun handleEvent(event: TrainerInviteUiEvent) {
        when (event) {
            TrainerInviteUiEvent.OnRegenerateClick -> regenerateCode()
            TrainerInviteUiEvent.OnRegenerateClick -> generateCode()
            TrainerInviteUiEvent.OnBackClick -> navigateToBack()
            TrainerInviteUiEvent.OnSkipClick -> navigateToHome()
            is TrainerInviteUiEvent.OnCodeClick -> {
                sendEffect(TrainerInviteSideEffect.CopyToClipBoard(event.code))
                sendEffect(TrainerInviteSideEffect.ShowToast("코드가 복사되었어요!"))
            }
        }
    }

    init {
        generateCode()
    }

    private fun generateCode() {
        viewModelScope.launch {
            runCatching {
                connectRepository.getInviteCode()
            }.onSuccess { result ->
                updateState { copy(inviteCode = result.invitationCode) }
            }.onFailure {
                // TODO 컴포넌트 사용
                sendEffect(TrainerInviteSideEffect.ShowToast("서버 요청에 실패했어요"))
            }
        }
    }

    private fun regenerateCode() {
        viewModelScope.launch {
            runCatching {
                connectRepository.regenerateInviteCode()
            }.onSuccess { result ->
                updateState { copy(inviteCode = result.invitationCode) }
            }.onFailure {
                // TODO 컴포넌트 사용
                sendEffect(TrainerInviteSideEffect.ShowToast("서버 요청에 실패했어요"))
            }
        }
    }

    private fun navigateToBack() {
        sendEffect(TrainerInviteSideEffect.NavigateToBack)
    }

    private fun navigateToHome() {
        sendEffect(TrainerInviteSideEffect.NavigateToHome)
    }
}
