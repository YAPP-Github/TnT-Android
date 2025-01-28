package co.kr.tnt.domain.model

data class SignUpResult(
    val memberType: String,
    val sessionId: String,
    val name: String,
    val profileImageUrl: String,
)
