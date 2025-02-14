package co.kr.data.network.model

import co.kr.tnt.domain.model.AuthType
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val socialType: AuthType,
    val fcmToken: String,
    val socialAccessToken: String,
)
