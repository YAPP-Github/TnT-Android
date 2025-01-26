package co.kr.data.network.model

import co.kr.tnt.domain.model.AuthType
import kotlinx.serialization.Serializable

// TODO fcm token
@Serializable
data class LoginRequest(
    val socialType: AuthType,
    val fcmToken: String = "EMPTY",
    val socialAccessToken: String,
)
