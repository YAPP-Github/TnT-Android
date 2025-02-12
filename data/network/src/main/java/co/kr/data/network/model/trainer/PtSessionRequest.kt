package co.kr.data.network.model.trainer

import kotlinx.serialization.Serializable

@Serializable
data class PtSessionRequest(
    val start: String,
    val end: String,
    val memo: String,
    val traineeId: Long,
)
