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
    ) : UiState

    sealed interface TraineeMealRecordUiEvent : UiEvent {
        data class OnClickMealDate(val date: LocalDate) : TraineeMealRecordUiEvent
        data class OnClickMealTime(val time: LocalTime) : TraineeMealRecordUiEvent
        data class OnClickMealTypeButton(val mealType: String) : TraineeMealRecordUiEvent
        data object OnClickSave : TraineeMealRecordUiEvent
        data object OnClickBack : TraineeMealRecordUiEvent
    }

    sealed interface TraineeMealRecordEffect : UiSideEffect {
        data object NavigateToHome : TraineeMealRecordEffect
        data class ShowToast(val message: String) : TraineeMealRecordEffect
    }
}
