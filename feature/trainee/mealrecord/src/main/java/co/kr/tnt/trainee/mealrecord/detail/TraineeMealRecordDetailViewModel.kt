package co.kr.tnt.trainee.mealrecord.detail

import androidx.lifecycle.viewModelScope
import co.kr.tnt.domain.model.RecordType.MealType
import co.kr.tnt.domain.repository.TraineeRepository
import co.kr.tnt.trainee.mealrecord.detail.TraineeMealRecordDetailContract.TraineeMealRecordDetailSideEffect
import co.kr.tnt.trainee.mealrecord.detail.TraineeMealRecordDetailContract.TraineeMealRecordDetailUiEvent
import co.kr.tnt.trainee.mealrecord.detail.TraineeMealRecordDetailContract.TraineeMealRecordDetailUiState
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TraineeMealRecordDetailViewModel @Inject constructor(
    private val traineeRepository: TraineeRepository,
) :
    BaseViewModel<TraineeMealRecordDetailUiState, TraineeMealRecordDetailUiEvent, TraineeMealRecordDetailSideEffect>(
            TraineeMealRecordDetailUiState(),
        ) {
        override suspend fun handleEvent(event: TraineeMealRecordDetailUiEvent) {
            when (event) {
                is TraineeMealRecordDetailUiEvent.LoadRecordDetail -> fetchRecordDetail(event.id)
                TraineeMealRecordDetailUiEvent.OnClickMore -> {}
            }
        }

        private fun fetchRecordDetail(id: Long) {
            viewModelScope.launch {
                runCatching {
                    traineeRepository.getMealRecord(id)
                }.onSuccess { result ->
                    updateState {
                        copy(
                            image = result.imageUrl,
                            date = result.date,
                            mealType = convertToMealType(result.dietType),
                            memo = result.memo,
                        )
                    }
                }.onFailure {
                    sendEffect(TraineeMealRecordDetailSideEffect.ShowToast("데이터 불러오기에 실패했어요"))
                }
            }
        }

        private fun convertToMealType(mealType: String): MealType {
            return when (mealType.uppercase()) {
                "BREAKFAST" -> MealType.BREAKFAST
                "LUNCH" -> MealType.LUNCH
                "DINNER" -> MealType.DINNER
                "SNACK" -> MealType.SNACK
                else -> MealType.BREAKFAST
            }
        }
    }
