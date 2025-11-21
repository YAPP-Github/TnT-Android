package co.kr.tnt.domain.model

data class ConnectedResult(
    val trainerName: String,
    val traineeName: String,
    val trainerImage: String?,
    val traineeImage: String?,
    val age: Int?,
    val height: Double?,
    val weight: Double?,
    val ptGoals: List<String>,
    val cautionNote: String?,
)
