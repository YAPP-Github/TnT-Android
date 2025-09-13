package co.kr.tnt.trainee.modifymyinfo

import co.kr.tnt.domain.UserProfilePolicy
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState
import co.kr.tnt.ui.resource.DisplayText
import java.io.File
import java.time.LocalDate

private const val MAX_HEIGHT_LENGTH = 3
private const val MAX_WEIGHT_LENGTH = 5

internal class TraineeModifyMyInfoContract {
    data class TraineeModifyMyInfoUiState(
        val profileImage: String? = null,
        val name: String = "",
        val birthday: LocalDate? = null,
        val height: String? = null,
        val weight: String? = null,
        val ptPurpose: List<String>? = emptyList(),
        val caution: String? = "",
    ) : UiState {
        val isNameValid
            get() = name.isBlank() ||
                name.matches(UserProfilePolicy.USER_NAME_REGEX) &&
                name.length <= UserProfilePolicy.USER_NAME_MAX_LENGTH

        /**
         * 키가 유효한 입력값인지 검사
         * 형식: 정수 3자
         */
        val isHeightValid
            get() = height.isNullOrBlank() || (
                height.toIntOrNull() != null &&
                    !height.startsWith("0") &&
                    height.length <= MAX_HEIGHT_LENGTH
            )

        /**
         * 몸무게가 유효한 입력값인지 검사
         * 형식: 5자 이하의 실수 (000, 00, 00.0, 000.0)
         */
        private val weightRegex = Regex("^(\\d{1,3}(\\.\\d)?)?\$")
        val isWeightValid
            get() = weight.isNullOrBlank() || (
                weight.matches(weightRegex) &&
                    !weight.startsWith("0") &&
                    weight.length <= MAX_WEIGHT_LENGTH
            )

        // TODO 완료 버튼 활성화 조건 만들기
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
        data object OnClickBack : TraineeModifyMyInfoUiEvent
        data object OnClickNext : TraineeModifyMyInfoUiEvent
    }

    sealed interface TraineeModifyMyInfoEffect : UiSideEffect {
        data class ShowToast(val message: DisplayText) : TraineeModifyMyInfoEffect
        data object NavigateToBack : TraineeModifyMyInfoEffect
    }
}
