package co.kr.tnt.trainee.mealrecord.detail

import co.kr.tnt.trainee.mealrecord.detail.TraineeMealRecordDetailContract.TraineeMealRecordDetailSideEffect
import co.kr.tnt.trainee.mealrecord.detail.TraineeMealRecordDetailContract.TraineeMealRecordDetailUiEvent
import co.kr.tnt.trainee.mealrecord.detail.TraineeMealRecordDetailContract.TraineeMealRecordDetailUiState
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class TraineeMealRecordDetailViewModel @Inject constructor() :
    BaseViewModel<TraineeMealRecordDetailUiState, TraineeMealRecordDetailUiEvent, TraineeMealRecordDetailSideEffect>(
        TraineeMealRecordDetailUiState(),
    ) {
        override suspend fun handleEvent(event: TraineeMealRecordDetailUiEvent) {
            when (event) {
                is TraineeMealRecordDetailUiEvent.LoadRecordDetail -> fetchRecordDetail(event.id)
                TraineeMealRecordDetailUiEvent.OnClickMore -> TODO()
            }
        }

        private fun fetchRecordDetail(id: Int) {
            // TODO : 식단 정보 가져오기 API 호출
        }
    }
