package co.kr.tnt.ui.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import co.kr.tnt.core.ui.R.string.core_exercise_back
import co.kr.tnt.core.ui.R.string.core_exercise_cardio
import co.kr.tnt.core.ui.R.string.core_exercise_lower_body
import co.kr.tnt.core.ui.R.string.core_exercise_shoulder
import co.kr.tnt.core.ui.R.string.core_exercise_upper_body
import co.kr.tnt.core.ui.R.string.core_meal_breakfast
import co.kr.tnt.core.ui.R.string.core_meal_dinner
import co.kr.tnt.core.ui.R.string.core_meal_lunch
import co.kr.tnt.core.ui.R.string.core_meal_snack
import co.kr.tnt.core.ui.R.string.core_pt_session
import co.kr.tnt.designsystem.component.chip.model.ChipStyle
import co.kr.tnt.domain.model.RecordType

sealed interface RecordChip {
    val title: String
    val emoji: String?
    val chipStyle: ChipStyle

    data class MealChip(
        override val title: String,
        override val emoji: String,
        override val chipStyle: ChipStyle,
    ) : RecordChip
    data class ExerciseChip(
        override val title: String,
        override val emoji: String? = null,
        override val chipStyle: ChipStyle,
    ) : RecordChip
    data class PTSessionChip(
        override val title: String,
        val sessionCount: Int,
        override val emoji: String,
        override val chipStyle: ChipStyle,
    ) : RecordChip

    companion object {
        @Composable
        fun create(type: RecordType): RecordChip {
            return when (type) {
                is RecordType.MealType -> {
                    val title = when (type) {
                        RecordType.MealType.BREAKFAST -> stringResource(core_meal_breakfast)
                        RecordType.MealType.LUNCH -> stringResource(core_meal_lunch)
                        RecordType.MealType.DINNER -> stringResource(core_meal_dinner)
                        RecordType.MealType.SNACK -> stringResource(core_meal_snack)
                    }
                    val emoji = when (type) {
                        RecordType.MealType.BREAKFAST -> "🌞"
                        RecordType.MealType.LUNCH -> "⛅"
                        RecordType.MealType.DINNER -> "🌙"
                        RecordType.MealType.SNACK -> "🍰"
                    }
                    MealChip(title, emoji, ChipStyle.PINK)
                }

                is RecordType.ExerciseType -> {
                    val title = when (type) {
                        RecordType.ExerciseType.UPPER_BODY -> stringResource(core_exercise_upper_body)
                        RecordType.ExerciseType.LOWER_BODY -> stringResource(core_exercise_lower_body)
                        RecordType.ExerciseType.BACK -> stringResource(core_exercise_back)
                        RecordType.ExerciseType.SHOULDER -> stringResource(core_exercise_shoulder)
                        RecordType.ExerciseType.CARDIO -> stringResource(core_exercise_cardio)
                    }
                    ExerciseChip(
                        title = title,
                        chipStyle = ChipStyle.BLUE,
                    )
                }

                is RecordType.PTSessionType -> {
                    val title = stringResource(core_pt_session, type.sessionCount)
                    val emoji = "💪"
                    PTSessionChip(title, type.sessionCount, emoji, ChipStyle.BLUE)
                }
            }
        }
    }
}
