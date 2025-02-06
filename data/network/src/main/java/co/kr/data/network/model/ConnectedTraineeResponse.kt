package co.kr.data.network.model

import co.kr.tnt.domain.model.ConnectedResult
import kotlinx.serialization.Serializable

@Serializable
data class ConnectedTraineeResponse(
    val trainerName: String,
    val traineeName: String,
    val trainerProfileImageUrl: String?,
    val traineeProfileImageUrl: String?,
    val traineeAge: Int,
    val height: Double,
    val weight: Double,
    val ptGoal: String,
    val cautionNote: String?,
)

fun ConnectedTraineeResponse.toDomain(): ConnectedResult =
    ConnectedResult(
        trainerName = trainerName,
        traineeName = traineeName,
        trainerImage = trainerProfileImageUrl,
        traineeImage = traineeProfileImageUrl,
        age = traineeAge,
        height = height,
        weight = weight,
        ptGoal = ptGoal,
        cautionNote = cautionNote,
    )
