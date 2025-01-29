package co.kr.tnt.ui.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import co.kr.tnt.core.ui.R
import co.kr.tnt.domain.model.RecordType

sealed class RecordChip(val title: String, val emoji: String?) {
    class DietChip(
        title: String,
        emoji: String,
    ) : RecordChip(title, emoji)
    class ExerciseChip(
        title: String,
        emoji: String? = null,
    ) : RecordChip(title, emoji)
    class PTSessionChip(
        title: String,
        val sessionCount: Int,
        emoji: String,
    ) : RecordChip(title, emoji)

    companion object {
        @Composable
        fun create(type: RecordType, sessionCount: Int? = null): RecordChip {
            return when (type) {
                is RecordType.DietType -> {
                    val title = when (type) {
                        RecordType.DietType.BREAKFAST -> stringResource(R.string.diet_breakfast)
                        RecordType.DietType.LUNCH -> stringResource(R.string.diet_lunch)
                        RecordType.DietType.DINNER -> stringResource(R.string.diet_dinner)
                        RecordType.DietType.SNACK -> stringResource(R.string.diet_snack)
                    }
                    val emoji = when (type) {
                        RecordType.DietType.BREAKFAST -> "🌞"
                        RecordType.DietType.LUNCH -> "⛅"
                        RecordType.DietType.DINNER -> "🌙"
                        RecordType.DietType.SNACK -> "🍰"
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
                    val title = stringResource(R.string.pt_session, sessionCount ?: 1)
                    val emoji = "💪"
                    PTSessionChip(title, sessionCount ?: 1, emoji)
                }
            }
        }
    }
}
