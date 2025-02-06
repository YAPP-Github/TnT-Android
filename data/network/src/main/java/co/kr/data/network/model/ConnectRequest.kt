package co.kr.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ConnectRequest(
    val invitationCode: String,
    val startDate: String,
    val totalPtCount: Int,
    val finishedPtCount: Int,
)
