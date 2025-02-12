package co.kr.tnt.trainee.mealrecord

import co.kr.tnt.trainee.mealrecord.TraineeMealRecordContract.TraineeMealRecordSideEffect
import co.kr.tnt.trainee.mealrecord.TraineeMealRecordContract.TraineeMealRecordUiEvent
import co.kr.tnt.trainee.mealrecord.TraineeMealRecordContract.TraineeMealRecordUiState
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class TraineeMealRecordViewModel @Inject constructor() :
    BaseViewModel<TraineeMealRecordUiState, TraineeMealRecordUiEvent, TraineeMealRecordSideEffect>(
        TraineeMealRecordUiState(),
    ) {
        override suspend fun handleEvent(event: TraineeMealRecordUiEvent) {
            when (event) {
                is TraineeMealRecordUiEvent.OnSelectImage -> updateState { copy(image = event.imageUri) }
                TraineeMealRecordUiEvent.OnClickDeleteImage -> updateState { copy(image = null) }
                is TraineeMealRecordUiEvent.OnClickMealDate -> updateState { copy(isDateFieldFocused = true) }
                is TraineeMealRecordUiEvent.OnSelectMealDate -> updateState {
                    copy(
                        date = event.date,
                        isDateFieldFocused = false,
                    )
                }

                is TraineeMealRecordUiEvent.OnClickMealTime -> updateState { copy(isTimeFieldFocused = true) }
                is TraineeMealRecordUiEvent.OnSelectMealTime -> updateState {
                    copy(
                        time = event.time,
                        isTimeFieldFocused = false,
                    )
                }

                TraineeMealRecordUiEvent.OnClickCloseBottomSheet -> clearFocusState()

                is TraineeMealRecordUiEvent.OnSelectMealType -> updateState { copy(mealType = event.mealType) }
                is TraineeMealRecordUiEvent.OnChangeMemo -> updateMemo(event.memo)
                TraineeMealRecordUiEvent.OnClickBack -> sendEffect(TraineeMealRecordSideEffect.NavigateToHome)
                TraineeMealRecordUiEvent.OnClickSave -> TODO()
            }
        }

        private fun clearFocusState() {
            updateState {
                copy(
                    isDateFieldFocused = false,
                    isTimeFieldFocused = false,
                )
            }
        }

        private fun updateMemo(value: String) {
            if (value.length == 100) {
                updateState { copy(showWarning = true, memo = value) }
            } else {
                updateState { copy(showWarning = false, memo = value) }
            }
        }
    }
