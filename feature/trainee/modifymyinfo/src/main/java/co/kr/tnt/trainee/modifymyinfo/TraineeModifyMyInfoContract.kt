package co.kr.tnt.trainee.modifymyinfo

import co.kr.tnt.domain.UserProfilePolicy
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState
import co.kr.tnt.ui.resource.DisplayText
import java.io.File
import java.time.LocalDate

internal class TraineeModifyMyInfoContract {
    data class TraineeModifyMyInfoUiState(
        val profileImage: String? = null,
        val name: String = "",
        val birthday: LocalDate? = null,
        val height: String? = null,
        val weight: String? = null,
        val ptPurpose: List<String>? = emptyList(),
        val caution: String? = "",
        val dialogState: DialogState = DialogState.NONE,
        val isEnableComplete: Boolean = false,
    ) : UiState {
        val isNameValid
            get() = name.isNotBlank() &&
                name.matches(UserProfilePolicy.USER_NAME_REGEX) &&
                name.length <= UserProfilePolicy.USER_NAME_MAX_LENGTH

        val isHeightValid
            get() = height.isNullOrBlank() || (
                height.toIntOrNull() != null &&
                    height.startsWith("0").not() &&
                    height.length <= UserProfilePolicy.USER_HEIGHT_MAX_LENGTH
            )

        val isWeightValid
            get() = weight.isNullOrBlank() || (
                weight.matches(UserProfilePolicy.USER_WEIGHT_REGEX) &&
                    weight.startsWith("0").not() &&
                    weight.length <= UserProfilePolicy.USER_WEIGHT_MAX_LENGTH
            )

        val isCautionNoteValid
            get() = caution.isNullOrBlank() || (
                caution.length < UserProfilePolicy.USER_CAUTION_MAX_LENGTH
            )

        enum class DialogState {
            NONE,
            CONFIRM_EXIT,
        }
    }

    sealed interface TraineeModifyMyInfoUiEvent : UiEvent {
        data object OnDeleteProfileImage : TraineeModifyMyInfoUiEvent
        data class OnProfileImageSelect(val image: File) : TraineeModifyMyInfoUiEvent
        data class OnChangeName(val name: String) : TraineeModifyMyInfoUiEvent
        data class OnChangeHeight(val height: String) : TraineeModifyMyInfoUiEvent
        data class OnChangeWeight(val weight: String) : TraineeModifyMyInfoUiEvent
        data class OnChangeBirthday(val birthday: LocalDate) : TraineeModifyMyInfoUiEvent
        data class OnSelectPurpose(val purpose: String) : TraineeModifyMyInfoUiEvent
        data class OnChangeCaution(val text: String) : TraineeModifyMyInfoUiEvent
        data object OnDismissDialog : TraineeModifyMyInfoUiEvent
        data object OnClickDialogConfirm : TraineeModifyMyInfoUiEvent
        data object OnClickBack : TraineeModifyMyInfoUiEvent
        data object OnClickComplete : TraineeModifyMyInfoUiEvent
    }

    sealed interface TraineeModifyMyInfoEffect : UiSideEffect {
        data class ShowToast(val message: DisplayText) : TraineeModifyMyInfoEffect
        data object NavigateToBack : TraineeModifyMyInfoEffect
    }
}
