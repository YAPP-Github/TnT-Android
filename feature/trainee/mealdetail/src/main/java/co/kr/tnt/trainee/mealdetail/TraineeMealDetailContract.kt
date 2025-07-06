package co.kr.tnt.trainee.mealdetail

import co.kr.tnt.domain.model.RecordType.MealType
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState
import co.kr.tnt.ui.resource.DisplayText
import java.time.LocalDateTime

internal class TraineeMealDetailContract {
    data class TraineeMealDetailUiState(
        val id: Long = 0,
        val image: String? = null,
        val date: LocalDateTime = LocalDateTime.now(),
        val mealType: MealType = MealType.BREAKFAST,
        val memo: String = "",
    ) : UiState

    sealed interface TraineeMealDetailUiEvent : UiEvent {
        data class LoadMealDetail(val id: Long) : TraineeMealDetailUiEvent
        data object OnClickMore : TraineeMealDetailUiEvent
    }

    sealed interface TraineeMealDetailSideEffect : UiSideEffect {
        data object NavigateToHome : TraineeMealDetailSideEffect
        data class ShowToast(val message: DisplayText) : TraineeMealDetailSideEffect
    }
}
