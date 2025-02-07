package co.kr.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class VerifyCodeResponse(
    val isVerified: Boolean,
)
