package co.kr.tnt.trainer.addptsession

import co.kr.tnt.domain.model.MemberInfo
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

internal class AddPtSessionContract {
    data class AddPtSessionUiState(
        val members: List<MemberInfo> = emptyList(),
        val selectedMember: MemberInfo? = null,
        val selectedDate: LocalDate? = null,
        val selectedStartTime: LocalTime? = null,
        val selectedEndTime: LocalTime? = null,
        val memo: String = "",
        val dialogState: DialogState = DialogState.NONE,
        val sheetType: BottomSheetType = BottomSheetType.NONE,
    ) : UiState {
        val totalSessionMinute: Int?
            get() {
                val selectedStartTime = selectedStartTime ?: return null
                val selectedEndTime = selectedEndTime ?: return null

                return ChronoUnit.MINUTES.between(selectedStartTime, selectedEndTime).toInt()
            }

        val isErrorTime: Boolean
            get() {
                val selectedStartTime = selectedStartTime ?: return false
                val selectedEndTime = selectedEndTime ?: return false

                return selectedEndTime.isBefore(selectedStartTime) ||
                    (
                        selectedStartTime.hour == selectedEndTime.hour &&
                            selectedStartTime.minute == selectedEndTime.minute
                    )
            }

        val isErrorMemo: Boolean
            get() = memo.length >= 30

        val isEnableComplete: Boolean
            get() = selectedMember != null && selectedDate != null && selectedStartTime != null &&
                selectedEndTime != null && isErrorTime.not()

        enum class BottomSheetType {
            NONE,
            SELECT_MEMBER,
            SELECT_DATE,
            SELECT_START_TIME,
            SELECT_END_TIME,
        }

        enum class DialogState {
            NONE,
            SUCCESS_ADD,
            CHECK_CANCEL_ADD,
        }
    }

    sealed interface AddPtSessionUiEvent : UiEvent {
        data object OnClickBack : AddPtSessionUiEvent
        data object OnClickMember : AddPtSessionUiEvent
        data class OnSelectMember(val member: MemberInfo) : AddPtSessionUiEvent
        data object OnClickDate : AddPtSessionUiEvent
        data class OnSelectDate(val date: LocalDate) : AddPtSessionUiEvent
        data object OnClickStartTime : AddPtSessionUiEvent
        data class OnSelectStartTime(val startTime: LocalTime) : AddPtSessionUiEvent
        data object OnClickEndTime : AddPtSessionUiEvent
        data class OnSelectEndTime(val endTime: LocalTime) : AddPtSessionUiEvent
        data class OnClickMinuteChip(val minute: Int) : AddPtSessionUiEvent
        data class OnChangeMemo(val memo: String) : AddPtSessionUiEvent
        data object OnClickComplete : AddPtSessionUiEvent
        data object OnClickDialogConfirm : AddPtSessionUiEvent
        data object OnDismissDialog : AddPtSessionUiEvent
    }

    sealed interface AddPtSessionSideEffect : UiSideEffect {
        data object ShowBottomSheet : AddPtSessionSideEffect
        data object HideBottomSheet : AddPtSessionSideEffect
        data class ShowToast(val message: String) : AddPtSessionSideEffect
        data object NavigateToPrevious : AddPtSessionSideEffect
    }
}
