package co.kr.tnt.domain.model

sealed class RecordType {
    sealed class MealType : RecordType() {
        data object BREAKFAST : MealType()
        data object LUNCH : MealType()
        data object DINNER : MealType()
        data object SNACK : MealType()
    }

    sealed class ExerciseType : RecordType() {
        data object UPPER_BODY : ExerciseType()
        data object LOWER_BODY : ExerciseType()
        data object BACK : ExerciseType()
        data object SHOULDER : ExerciseType()
        data object CARDIO : ExerciseType()
    }

    data class PTSessionType(val sessionCount: Int) : RecordType()
}
