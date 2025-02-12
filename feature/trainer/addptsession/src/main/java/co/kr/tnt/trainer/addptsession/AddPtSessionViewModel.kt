package co.kr.tnt.trainer.addptsession

import androidx.lifecycle.viewModelScope
import co.kr.tnt.domain.repository.TrainerRepository
import co.kr.tnt.trainer.addptsession.AddPtSessionContract.AddPtSessionSideEffect
import co.kr.tnt.trainer.addptsession.AddPtSessionContract.AddPtSessionUiEvent
import co.kr.tnt.trainer.addptsession.AddPtSessionContract.AddPtSessionUiState
import co.kr.tnt.trainer.addptsession.AddPtSessionContract.AddPtSessionUiState.BottomSheetType
import co.kr.tnt.trainer.addptsession.AddPtSessionContract.AddPtSessionUiState.DialogState
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AddPtSessionViewModel @Inject constructor(
    private val trainerRepository: TrainerRepository,
) : BaseViewModel<AddPtSessionUiState, AddPtSessionUiEvent, AddPtSessionSideEffect>(
        AddPtSessionUiState(),
    ) {
    init {
        viewModelScope.launch {
            runCatching {
                trainerRepository.getActiveMembers()
            }.onSuccess { members ->
                updateState { copy(members = members) }
            }.onFailure {
                sendEffect(AddPtSessionSideEffect.ShowToast("서버 요청에 실패했어요"))
            }
        }
    }

    override suspend fun handleEvent(event: AddPtSessionUiEvent) {
        when (event) {
            is AddPtSessionUiEvent.OnChangeMemo -> updateState { copy(memo = event.memo) }
            AddPtSessionUiEvent.OnClickBack -> handleClickBack()
            AddPtSessionUiEvent.OnClickComplete -> postPtSession()
            AddPtSessionUiEvent.OnClickMember -> showBottomSheet(BottomSheetType.SELECT_MEMBER)
            is AddPtSessionUiEvent.OnSelectMember -> {
                updateState { copy(selectedMember = event.member) }
                sendEffect(AddPtSessionSideEffect.HideBottomSheet)
            }

            AddPtSessionUiEvent.OnClickDate -> showBottomSheet(BottomSheetType.SELECT_DATE)
            is AddPtSessionUiEvent.OnSelectDate -> {
                updateState { copy(selectedDate = event.date) }
                sendEffect(AddPtSessionSideEffect.HideBottomSheet)
            }

            AddPtSessionUiEvent.OnClickStartTime -> showBottomSheet(BottomSheetType.SELECT_START_TIME)
            is AddPtSessionUiEvent.OnSelectStartTime -> {
                updateState {
                    copy(
                        selectedStartTime = event.startTime,
                        selectedEndTime = null,
                    )
                }
                sendEffect(AddPtSessionSideEffect.HideBottomSheet)
            }

            AddPtSessionUiEvent.OnClickEndTime -> showBottomSheet(BottomSheetType.SELECT_END_TIME)
            is AddPtSessionUiEvent.OnSelectEndTime -> {
                updateState { copy(selectedEndTime = event.endTime) }
                sendEffect(AddPtSessionSideEffect.HideBottomSheet)
            }

            is AddPtSessionUiEvent.OnClickMinuteChip -> handleClickMinuteChip(event.minute)
            AddPtSessionUiEvent.OnClickDialogConfirm -> {
                updateState { copy(dialogState = DialogState.NONE) }
                sendEffect(AddPtSessionSideEffect.NavigateToPrevious)
            }

            AddPtSessionUiEvent.OnDismissDialog -> updateState { copy(dialogState = DialogState.NONE) }
        }
    }

    private fun showBottomSheet(sheetType: BottomSheetType) {
        updateState { copy(sheetType = sheetType) }
        sendEffect(AddPtSessionSideEffect.ShowBottomSheet)
    }

    private fun handleClickBack() {
        if (currentState.isEnableComplete) {
            updateState { copy(dialogState = DialogState.CHECK_CANCEL_ADD) }
            return
        }

        sendEffect(AddPtSessionSideEffect.NavigateToPrevious)
    }

    private fun handleClickMinuteChip(minute: Int) {
        val startTime = currentState.selectedStartTime ?: return
        updateState { copy(selectedEndTime = startTime.plusMinutes(minute.toLong())) }
    }

    private fun postPtSession() {
        if (currentState.isEnableComplete.not()) {
            sendEffect(AddPtSessionSideEffect.ShowToast("필수 입력 항목을 모두 입력해주세요."))
            return
        }

        val startDateTime = currentState.selectedDate?.atTime(currentState.selectedStartTime) ?: return
        val endDateTime = currentState.selectedDate?.atTime(currentState.selectedEndTime) ?: return

        viewModelScope.launch {
            runCatching {
                trainerRepository.postPtSession(
                    startDateTime = startDateTime,
                    endLocalDateTime = endDateTime,
                    memo = currentState.memo,
                    traineeId = currentState.selectedMember?.id ?: return@launch,
                )
            }.onSuccess {
                updateState { copy(dialogState = DialogState.SUCCESS_ADD) }
            }.onFailure {
                sendEffect(AddPtSessionSideEffect.ShowToast("서버 요청에 실패했어요"))
            }
        }
    }
}
