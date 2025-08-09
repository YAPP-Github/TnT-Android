package co.kr.tnt.trainer.members

import androidx.lifecycle.viewModelScope
import co.kr.tnt.core.ui.R.string.core_failed_to_server_request
import co.kr.tnt.domain.repository.TrainerRepository
import co.kr.tnt.trainer.members.TrainerMemberContract.TrainerMemberSideEffect
import co.kr.tnt.trainer.members.TrainerMemberContract.TrainerMemberUiEvent
import co.kr.tnt.trainer.members.TrainerMemberContract.TrainerMemberUiState
import co.kr.tnt.ui.base.BaseViewModel
import co.kr.tnt.ui.resource.DisplayText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TrainerMembersViewModel @Inject constructor(
    private val trainerRepository: TrainerRepository,
) : BaseViewModel<TrainerMemberUiState, TrainerMemberUiEvent, TrainerMemberSideEffect>(
        TrainerMemberUiState(),
    ) {
    init {
        setMembers()
    }
    override suspend fun handleEvent(event: TrainerMemberUiEvent) {
        when (event) {
            TrainerMemberUiEvent.OnClickMember -> TODO()
        }
    }

    private fun setMembers() {
        viewModelScope.launch {
            runCatching {
                trainerRepository.getActiveMembers()
            }.onSuccess { members ->
                updateState { copy(memberList = members) }
            }.onFailure {
                sendEffect(TrainerMemberSideEffect.ShowToast(DisplayText.Resource(core_failed_to_server_request)))
            }
        }
    }
}
