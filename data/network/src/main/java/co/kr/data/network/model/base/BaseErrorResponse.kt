package co.kr.data.network.model.base

import kotlinx.serialization.Serializable

@Serializable
data class BaseErrorResponse(
    val message: String,
)
