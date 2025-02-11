package co.kr.tnt.trainer.addptsession

import co.kr.tnt.domain.model.User
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

internal class AddPtSessionContract {
    data class AddPtSessionUiState(
        val members: List<User.Trainee> = emptyList(),
        val selectedMember: User.Trainee = User.Trainee.EMPTY,
        val selectedDate: LocalDate? = null,
        val selectedStartTime: LocalTime? = null,
        val selectedEndTime: LocalTime? = null,
        val memo: String = "",
        val sheetType: BottomSheetType = BottomSheetType.NONE,
    ) : UiState {
        val totalSessionMinute: Int?
            get() {
                val selectedStartTime = selectedStartTime ?: return null
                val selectedEndTime = selectedEndTime ?: return null

                return ChronoUnit.MINUTES.between(selectedStartTime, selectedEndTime).toInt()
            }

        enum class BottomSheetType {
            NONE,
            SELECT_MEMBER,
            SELECT_DATE,
            SELECT_START_TIME,
            SELECT_END_TIME,
        }
    }

    sealed interface AddPtSessionUiEvent : UiEvent {
        data object OnClickBack : AddPtSessionUiEvent
        data object OnClickMember : AddPtSessionUiEvent
        data class OnSelectMember(val member: User.Trainee) : AddPtSessionUiEvent
        data object OnClickDate : AddPtSessionUiEvent
        data class OnSelectDate(val date: LocalDate) : AddPtSessionUiEvent
        data object OnClickStartTime : AddPtSessionUiEvent
        data class OnSelectStartTime(val startTime: LocalTime) : AddPtSessionUiEvent
        data object OnClickEndTime : AddPtSessionUiEvent
        data class OnSelectEndTime(val endTime: LocalTime) : AddPtSessionUiEvent
        data class OnClickMinuteChip(val minute: Int) : AddPtSessionUiEvent
        data class OnChangeMemo(val memo: String) : AddPtSessionUiEvent
        data object OnClickComplete : AddPtSessionUiEvent
    }

    sealed interface AddPtSessionSideEffect : UiSideEffect {
        data object ShowBottomSheet : AddPtSessionSideEffect
        data object HideBottomSheet : AddPtSessionSideEffect
        data class ShowToast(val message: String) : AddPtSessionSideEffect
        data object NavigateToPrevious : AddPtSessionSideEffect
    }
}
