package co.kr.tnt.trainee.mealdetail

import co.kr.tnt.trainee.mealdetail.TraineeMealDetailContract.TraineeMealDetailSideEffect
import co.kr.tnt.trainee.mealdetail.TraineeMealDetailContract.TraineeMealDetailUiEvent
import co.kr.tnt.trainee.mealdetail.TraineeMealDetailContract.TraineeMealDetailUiState
import co.kr.tnt.ui.base.BaseViewModel
import javax.inject.Inject

internal class TraineeMealDetailViewModel @Inject constructor() :
    BaseViewModel<TraineeMealDetailUiState, TraineeMealDetailUiEvent, TraineeMealDetailSideEffect>(
        TraineeMealDetailUiState(),
    ) {
        override suspend fun handleEvent(event: TraineeMealDetailUiEvent) {
            TODO("Not yet implemented")
        }
    }
