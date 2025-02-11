package co.kr.tnt.domain.model

sealed class RecordType {
    sealed class MealType(val name: String) : RecordType() {
        data object BREAKFAST : MealType("BREAKFAST")
        data object LUNCH : MealType("LUNCH")
        data object DINNER : MealType("DINNER")
        data object SNACK : MealType("SNACK")
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
