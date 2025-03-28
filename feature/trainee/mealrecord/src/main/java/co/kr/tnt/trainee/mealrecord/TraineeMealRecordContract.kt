package co.kr.tnt.trainee.mealrecord

import android.content.Context
import android.net.Uri
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

internal class TraineeMealRecordContract {
    data class TraineeMealRecordUiState(
        val image: Uri? = null,
        val date: LocalDate = LocalDate.now(),
        val time: LocalTime? = null,
        val mealType: String = "",
        val memo: String = "",
        val isDateFieldFocused: Boolean = false,
        val isTimeFieldFocused: Boolean = false,
        val isMealRecordValid: Boolean = false,
        val showWarning: Boolean = false,
        val dialogState: DialogState = DialogState.NONE,
        val isLoading: Boolean = false,
    ) : UiState {
        enum class DialogState {
            NONE,
            COMPLETED,
            EXIT,
        }

        fun validateMealRecord(): TraineeMealRecordUiState {
            val now = LocalDateTime.now()
            val selectedDateTime = time?.let { LocalDateTime.of(date, it) }

            return copy(
                isMealRecordValid = selectedDateTime != null && selectedDateTime <= now &&
                    mealType.isNotBlank() &&
                    memo.isNotBlank() &&
                    memo.length < 100,
            )
        }
    }

    sealed interface TraineeMealRecordUiEvent : UiEvent {
        data class OnSelectImage(val imageUri: Uri) : TraineeMealRecordUiEvent
        data object OnClickDeleteImage : TraineeMealRecordUiEvent
        data object OnClickMealDate : TraineeMealRecordUiEvent
        data class OnSelectMealDate(val date: LocalDate) : TraineeMealRecordUiEvent
        data object OnClickMealTime : TraineeMealRecordUiEvent
        data class OnSelectMealTime(val time: LocalTime) : TraineeMealRecordUiEvent
        data object OnClickCloseBottomSheet : TraineeMealRecordUiEvent
        data class OnSelectMealType(val mealType: String) : TraineeMealRecordUiEvent
        data class OnChangeMemo(val memo: String) : TraineeMealRecordUiEvent
        data class OnClickSave(val context: Context) : TraineeMealRecordUiEvent
        data object OnClickBack : TraineeMealRecordUiEvent
        data object OnClickDialogConfirm : TraineeMealRecordUiEvent
        data object OnDismissDialog : TraineeMealRecordUiEvent
    }

    sealed interface TraineeMealRecordSideEffect : UiSideEffect {
        data object NavigateToHome : TraineeMealRecordSideEffect
        data class ShowToast(val message: String) : TraineeMealRecordSideEffect
    }
}
