package co.kr.tnt.trainee.mealrecord.record

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import co.kr.tnt.domain.repository.TraineeRepository
import co.kr.tnt.domain.utils.DateFormatter
import co.kr.tnt.trainee.mealrecord.record.TraineeMealRecordContract.TraineeMealRecordSideEffect
import co.kr.tnt.trainee.mealrecord.record.TraineeMealRecordContract.TraineeMealRecordUiEvent
import co.kr.tnt.trainee.mealrecord.record.TraineeMealRecordContract.TraineeMealRecordUiState
import co.kr.tnt.trainee.mealrecord.record.TraineeMealRecordContract.TraineeMealRecordUiState.DialogState
import co.kr.tnt.ui.base.BaseViewModel
import co.kr.tnt.ui.utils.convertToAllowedImageFormat
import co.kr.tnt.ui.utils.isAllowedImageFormat
import co.kr.tnt.ui.utils.toFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
internal class TraineeMealRecordViewModel @Inject constructor(
    private val traineeRepository: TraineeRepository,
    private val dateFormatter: DateFormatter,
) :
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
                    ).validateMealRecord()
                }

                is TraineeMealRecordUiEvent.OnClickMealTime -> updateState { copy(isTimeFieldFocused = true) }
                is TraineeMealRecordUiEvent.OnSelectMealTime -> updateState {
                    copy(
                        time = event.time,
                        isTimeFieldFocused = false,
                    ).validateMealRecord()
                }

                TraineeMealRecordUiEvent.OnClickCloseBottomSheet -> clearFocusState()

                is TraineeMealRecordUiEvent.OnSelectMealType -> updateState {
                    copy(mealType = event.mealType).validateMealRecord()
                }

                is TraineeMealRecordUiEvent.OnChangeMemo -> updateMemo(event.memo)
                TraineeMealRecordUiEvent.OnClickBack -> handleBackClick()
                is TraineeMealRecordUiEvent.OnClickSave -> postMealRecord(event.context)
                TraineeMealRecordUiEvent.OnClickDialogConfirm -> {
                    updateState { copy(dialogState = DialogState.NONE) }
                    sendEffect(TraineeMealRecordSideEffect.NavigateToHome)
                }

                TraineeMealRecordUiEvent.OnDismissDialog -> updateState { copy(dialogState = DialogState.NONE) }
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
            if (value.length >= 100) {
                updateState { copy(showWarning = true, memo = value).validateMealRecord() }
            } else {
                updateState { copy(showWarning = false, memo = value).validateMealRecord() }
            }
        }

        private fun handleBackClick() {
            if (currentState.dialogState == DialogState.EXIT) {
                updateState { copy(dialogState = DialogState.NONE) }
                sendEffect(TraineeMealRecordSideEffect.NavigateToHome)
            } else if (currentState.isMealRecordValid) {
                updateState { copy(dialogState = DialogState.EXIT) }
            } else {
                sendEffect(TraineeMealRecordSideEffect.NavigateToHome)
            }
        }

        private fun postMealRecord(context: Context) {
            updateState { copy(isLoading = true) }

            val mealDateTime = dateFormatter.format(
                LocalDateTime.of(currentState.date, currentState.time),
                "yyyy-MM-dd'T'HH:mm:ss",
            )

            viewModelScope.launch {
                val state = currentState
                val imageFile: File? = state.image?.toFile(context)?.let { file ->
                    if (!isAllowedImageFormat(file)) {
                        file.toUri().convertToAllowedImageFormat(context)
                    } else {
                        file
                    }
                }
                runCatching {
                    traineeRepository.postMealRecord(
                        mealImage = imageFile,
                        date = mealDateTime,
                        mealType = state.mealType,
                        memo = state.memo,
                    )
                }.onSuccess {
                    updateState { copy(dialogState = DialogState.COMPLETED) }
                }.onFailure {
                    sendEffect(TraineeMealRecordSideEffect.ShowToast("식단 기록에 실패했어요"))
                }.also {
                    updateState { copy(isLoading = false) }
                }
            }
        }
    }
