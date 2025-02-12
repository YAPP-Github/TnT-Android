package co.kr.tnt.trainee.mealrecord.detail

import co.kr.tnt.domain.utils.DateFormatter
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState
import java.time.LocalDateTime

internal class TraineeMealRecordDetailContract {
    data class TraineeMealRecordDetailUiState(
        val id: Int = 0,
        val image: String? = "",
        val date: LocalDateTime = LocalDateTime.now(),
        val mealType: String = "",
        val memo: String = "",
    ) : UiState {
        private val dateFormatter = DateFormatter()

        val titleDate: String
            get() = dateFormatter.format(date.toLocalDate(), "M월 d일")

        val contentDate: String
            get() = dateFormatter.format(date, "yyyy/MM/dd")

        val contentTime: String
            get() = dateFormatter.format(date, "a hh:mm")
    }

    sealed interface TraineeMealRecordDetailUiEvent : UiEvent {
        data class LoadRecordDetail(val id: Int) : TraineeMealRecordDetailUiEvent
        data object OnClickMore : TraineeMealRecordDetailUiEvent
    }

    sealed interface TraineeMealRecordDetailSideEffect : UiSideEffect {
        data object NavigateToHome : TraineeMealRecordDetailSideEffect
        data class ShowToast(val message: String) : TraineeMealRecordDetailSideEffect
    }
}
