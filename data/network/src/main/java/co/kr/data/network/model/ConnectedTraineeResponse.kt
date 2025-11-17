package co.kr.data.network.model

import co.kr.tnt.domain.model.ConnectedResult
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConnectedTraineeResponse(
    val trainee: Trainee,
    val trainer: Trainer,
)

@Serializable
data class Trainee(
    val traineeName: String,
    val traineeProfileImageUrl: String,
    val cautionNote: String?,
    val height: Double?,
    val ptGoals: List<String>,
    val traineeAge: Int?,
    val weight: Double?,
)

@Serializable
data class Trainer(
    @SerialName("trainerName")
    val trainerName: String,
    @SerialName("trainerProfileImageUrl")
    val trainerProfileImageUrl: String,
)

fun ConnectedTraineeResponse.toDomain(): ConnectedResult =
    ConnectedResult(
        trainerName = trainer.trainerName,
        traineeName = trainee.traineeName,
        trainerImage = trainer.trainerProfileImageUrl,
        traineeImage = trainee.traineeProfileImageUrl,
        age = trainee.traineeAge,
        height = trainee.height,
        weight = trainee.weight,
        ptGoals = trainee.ptGoals,
        cautionNote = trainee.cautionNote,
    )
