package co.kr.data.network.model

import co.kr.tnt.domain.model.ConnectRequestResult
import kotlinx.serialization.Serializable

@Serializable
data class ConnectRequestResponse(
    val trainerName: String,
    val traineeName: String,
    val trainerProfileImageUrl: String,
    val traineeProfileImageUrl: String,
)

fun ConnectRequestResponse.toDomain(): ConnectRequestResult =
    ConnectRequestResult(
        trainerName = trainerName,
        traineeName = traineeName,
        trainerImage = trainerProfileImageUrl,
        traineeImage = traineeProfileImageUrl,
    )
