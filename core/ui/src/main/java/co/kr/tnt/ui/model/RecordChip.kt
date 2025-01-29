package co.kr.tnt.ui.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import co.kr.tnt.core.ui.R
import co.kr.tnt.domain.model.RecordType

sealed interface RecordChip {
    val title: String
    val emoji: String?

    data class DietChip(
        override val title: String,
        override val emoji: String,
    ) : RecordChip
    data class ExerciseChip(
        override val title: String,
        override val emoji: String? = null,
    ) : RecordChip
    data class PTSessionChip(
        override val title: String,
        val sessionCount: Int,
        override val emoji: String,
    ) : RecordChip

    companion object {
        @Composable
        fun create(type: RecordType): RecordChip {
            return when (type) {
                is RecordType.MealType -> {
                    val title = when (type) {
                        RecordType.MealType.BREAKFAST -> stringResource(R.string.diet_breakfast)
                        RecordType.MealType.LUNCH -> stringResource(R.string.diet_lunch)
                        RecordType.MealType.DINNER -> stringResource(R.string.diet_dinner)
                        RecordType.MealType.SNACK -> stringResource(R.string.diet_snack)
                    }
                    val emoji = when (type) {
                        RecordType.MealType.BREAKFAST -> "🌞"
                        RecordType.MealType.LUNCH -> "⛅"
                        RecordType.MealType.DINNER -> "🌙"
                        RecordType.MealType.SNACK -> "🍰"
                    }
                    DietChip(title, emoji)
                }

                is RecordType.ExerciseType -> {
                    val title = when (type) {
                        RecordType.ExerciseType.UPPER_BODY -> stringResource(R.string.exercise_upper_body)
                        RecordType.ExerciseType.LOWER_BODY -> stringResource(R.string.exercise_lower_body)
                        RecordType.ExerciseType.BACK -> stringResource(R.string.exercise_back)
                        RecordType.ExerciseType.SHOULDER -> stringResource(R.string.exercise_shoulder)
                        RecordType.ExerciseType.CARDIO -> stringResource(R.string.exercise_cardio)
                    }
                    ExerciseChip(title)
                }

                is RecordType.PTSessionType -> {
                    val title = stringResource(R.string.pt_session, type.sessionCount)
                    val emoji = "💪"
                    PTSessionChip(title, type.sessionCount, emoji)
                }
            }
        }
    }
}
