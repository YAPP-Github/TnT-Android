package co.kr.tnt.trainee.mealrecord

import android.net.Uri
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState
import java.time.LocalDate
import java.time.LocalTime

internal class TraineeMealRecordContract {
    data class TraineeMealRecordUiState(
        val image: Uri? = null,
        val date: LocalDate = LocalDate.now(),
        val time: LocalTime = LocalTime.now(),
        val mealType: String = "",
        val memo: String = "",
        val isDateFieldFocused: Boolean = false,
        val isTimeFieldFocused: Boolean = false,
        val showWarning: Boolean = false,
    ) : UiState

    sealed interface TraineeMealRecordUiEvent : UiEvent {
        data class OnSelectImage(val imageUri: Uri) : TraineeMealRecordUiEvent
        data object OnClickDeleteImage : TraineeMealRecordUiEvent
        data object OnClickMealDate : TraineeMealRecordUiEvent
        data class OnSelectMealDate(val date: LocalDate) : TraineeMealRecordUiEvent
        data object OnClickMealTime : TraineeMealRecordUiEvent
        data class OnSelectMealTime(val time: LocalTime) : TraineeMealRecordUiEvent
        data class OnSelectMealType(val mealType: String) : TraineeMealRecordUiEvent
        data class OnChangeMemo(val memo: String) : TraineeMealRecordUiEvent
        data object OnClickSave : TraineeMealRecordUiEvent
        data object OnClickBack : TraineeMealRecordUiEvent
    }

    sealed interface TraineeMealRecordSideEffect : UiSideEffect {
        data object NavigateToHome : TraineeMealRecordSideEffect
        data class ShowToast(val message: String) : TraineeMealRecordSideEffect
    }
}
