package co.kr.data.network.model

import co.kr.tnt.domain.model.AuthType
import co.kr.tnt.domain.model.LoginResult
import kotlinx.serialization.Serializable

/**
 * @property sessionId 세션 ID, 비회원인 경우 null
 */
@Serializable
data class LoginResponse(
    val sessionId: String?,
    val socialId: String,
    val socialEmail: String,
    val socialType: AuthType,
    val isSignUp: Boolean,
)

fun LoginResponse.toDomain(): LoginResult =
    LoginResult(
        authId = socialId,
        email = socialEmail,
        authType = socialType,
        isSignUp = isSignUp,
    )
