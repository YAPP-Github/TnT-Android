package co.kr.tnt.trainer.invite

import androidx.lifecycle.viewModelScope
import co.kr.tnt.core.ui.R.string.core_failed_to_server_request
import co.kr.tnt.domain.repository.ConnectRepository
import co.kr.tnt.feature.trainer.invite.R
import co.kr.tnt.trainer.invite.TrainerInviteContract.TrainerInviteSideEffect
import co.kr.tnt.trainer.invite.TrainerInviteContract.TrainerInviteUiEvent
import co.kr.tnt.trainer.invite.TrainerInviteContract.TrainerInviteUiState
import co.kr.tnt.ui.base.BaseViewModel
import co.kr.tnt.ui.resource.DisplayText
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
            TrainerInviteUiEvent.OnClickRegenerate -> regenerateCode()
            TrainerInviteUiEvent.OnClickBack -> navigateToBack()
            TrainerInviteUiEvent.OnClickSkip -> navigateToHome()
            is TrainerInviteUiEvent.OnClickCode -> {
                sendEffect(TrainerInviteSideEffect.CopyToClipBoard(event.code))
                sendEffect(
                    TrainerInviteSideEffect.ShowToast(
                        DisplayText.Resource(R.string.code_is_copied),
                    ),
                )
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
                sendEffect(
                    TrainerInviteSideEffect.ShowToast(
                        DisplayText.Resource(core_failed_to_server_request),
                    ),
                )
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
                sendEffect(
                    TrainerInviteSideEffect.ShowToast(
                        DisplayText.Resource(core_failed_to_server_request),
                    ),
                )
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
