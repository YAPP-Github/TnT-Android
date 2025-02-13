package co.kr.tnt.trainee.mealrecord.detail

import co.kr.tnt.domain.model.RecordType.MealType
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState
import java.time.LocalDateTime

internal class TraineeMealRecordDetailContract {
    data class TraineeMealRecordDetailUiState(
        val id: Long = 0,
        val image: String? = null,
        val date: LocalDateTime = LocalDateTime.now(),
        val mealType: MealType = MealType.BREAKFAST,
        val memo: String = "",
    ) : UiState

    sealed interface TraineeMealRecordDetailUiEvent : UiEvent {
        data class LoadRecordDetail(val id: Long) : TraineeMealRecordDetailUiEvent
        data object OnClickMore : TraineeMealRecordDetailUiEvent
    }

    sealed interface TraineeMealRecordDetailSideEffect : UiSideEffect {
        data object NavigateToHome : TraineeMealRecordDetailSideEffect
        data class ShowToast(val message: String) : TraineeMealRecordDetailSideEffect
    }
}
