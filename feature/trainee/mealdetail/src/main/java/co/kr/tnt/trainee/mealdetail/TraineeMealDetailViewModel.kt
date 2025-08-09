package co.kr.tnt.trainee.mealdetail

import androidx.lifecycle.viewModelScope
import co.kr.tnt.core.ui.R.string.core_failed_to_server_request
import co.kr.tnt.domain.model.RecordType.MealType
import co.kr.tnt.domain.repository.TraineeRepository
import co.kr.tnt.trainee.mealdetail.TraineeMealDetailContract.TraineeMealDetailSideEffect
import co.kr.tnt.trainee.mealdetail.TraineeMealDetailContract.TraineeMealDetailUiEvent
import co.kr.tnt.trainee.mealdetail.TraineeMealDetailContract.TraineeMealDetailUiState
import co.kr.tnt.ui.base.BaseViewModel
import co.kr.tnt.ui.resource.DisplayText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TraineeMealDetailViewModel @Inject constructor(
    private val traineeRepository: TraineeRepository,
) :
    BaseViewModel<TraineeMealDetailUiState, TraineeMealDetailUiEvent, TraineeMealDetailSideEffect>(
            TraineeMealDetailUiState(),
        ) {
        override suspend fun handleEvent(event: TraineeMealDetailUiEvent) {
            when (event) {
                is TraineeMealDetailUiEvent.LoadMealDetail -> fetchRecordDetail(event.id)
                TraineeMealDetailUiEvent.OnClickMore -> {}
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
                    sendEffect(
                        TraineeMealDetailSideEffect.ShowToast(
                            DisplayText.Resource(core_failed_to_server_request),
                        ),
                    )
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
