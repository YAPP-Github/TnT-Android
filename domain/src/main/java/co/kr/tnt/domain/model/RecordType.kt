package co.kr.tnt.domain.model

sealed class RecordType {
    sealed class DietType : RecordType() {
        data object BREAKFAST : DietType()
        data object LUNCH : DietType()
        data object DINNER : DietType()
        data object SNACK : DietType()
    }

    sealed class ExerciseType : RecordType() {
        data object UPPER_BODY : ExerciseType()
        data object LOWER_BODY : ExerciseType()
        data object BACK : ExerciseType()
        data object SHOULDER : ExerciseType()
        data object CARDIO : ExerciseType()
    }

    sealed class PTSessionType : RecordType() {
        data object SESSION : PTSessionType()
    }
}
