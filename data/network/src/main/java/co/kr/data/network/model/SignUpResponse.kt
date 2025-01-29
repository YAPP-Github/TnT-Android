package co.kr.data.network.model

import co.kr.tnt.domain.model.SignUpResult
import kotlinx.serialization.Serializable

@Serializable
data class SignUpResponse(
    val memberType: String,
    val sessionId: String,
    val name: String,
    val profileImageUrl: String,
)

fun SignUpResponse.toDomain(): SignUpResult {
    return SignUpResult(
        memberType = memberType,
        sessionId = sessionId,
        name = name,
        profileImageUrl = profileImageUrl,
    )
}
