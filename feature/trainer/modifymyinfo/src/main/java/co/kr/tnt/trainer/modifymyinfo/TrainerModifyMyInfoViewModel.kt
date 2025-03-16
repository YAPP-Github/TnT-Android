package co.kr.tnt.trainer.modifymyinfo

import androidx.lifecycle.viewModelScope
import co.kr.tnt.domain.repository.TrainerRepository
import co.kr.tnt.trainer.modifymyinfo.TrainerModifyMyInfoContract.TrainerModifyMyInfoEffect
import co.kr.tnt.trainer.modifymyinfo.TrainerModifyMyInfoContract.TrainerModifyMyInfoUiEvent
import co.kr.tnt.trainer.modifymyinfo.TrainerModifyMyInfoContract.TrainerModifyMyInfoUiState
import co.kr.tnt.trainer.modifymyinfo.TrainerModifyMyInfoContract.TrainerModifyMyInfoUiState.DialogState
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TrainerModifyMyInfoViewModel @Inject constructor(
    private val trainerRepository: TrainerRepository,
) :
    BaseViewModel<TrainerModifyMyInfoUiState, TrainerModifyMyInfoUiEvent, TrainerModifyMyInfoEffect>(
            TrainerModifyMyInfoUiState(),
        ) {
        private var isUpdatedInfo = false

        init {
            initMyInfo()
        }

        override suspend fun handleEvent(event: TrainerModifyMyInfoUiEvent) {
            when (event) {
                TrainerModifyMyInfoUiEvent.OnClickBack -> {
                    if (isUpdatedInfo) {
                        updateState { copy(dialogState = DialogState.CONFIRM_EXIT) }
                        return
                    }

                    sendEffect(TrainerModifyMyInfoEffect.NavigateToPrevious)
                }
                TrainerModifyMyInfoUiEvent.OnClickComplete -> {
                    sendEffect(TrainerModifyMyInfoEffect.NavigateToPrevious)
                    // TODO API
                }

                is TrainerModifyMyInfoUiEvent.OnNameChange -> {
                    isUpdatedInfo = true
                    updateState { copy(name = event.name) }
                }
                is TrainerModifyMyInfoUiEvent.OnProfileImageSelect -> {
                    isUpdatedInfo = true
                    updateState { copy(profileImage = event.image.path) }
                }
                TrainerModifyMyInfoUiEvent.OnClickDialogConfirm -> {
                    updateState { copy(dialogState = DialogState.NONE) }
                    sendEffect(TrainerModifyMyInfoEffect.NavigateToPrevious)
                }
                TrainerModifyMyInfoUiEvent.OnDismissDialog -> updateState { copy(dialogState = DialogState.NONE) }
            }
        }

        private fun initMyInfo() {
            viewModelScope.launch {
                runCatching {
                    trainerRepository.getMyInfo()
                }.onSuccess { myInfo ->
                    updateState {
                        copy(
                            name = myInfo.name,
                            profileImage = myInfo.image,
                        )
                    }
                }.onFailure {
                    sendEffect(TrainerModifyMyInfoEffect.ShowToast("서버 요청에 실패했어요"))
                    sendEffect(TrainerModifyMyInfoEffect.NavigateToPrevious)
                }
            }
        }
    }
