package co.kr.tnt.domain.model

data class LoginResult(
    val authId: String,
    val email: String,
    val authType: AuthType,
    val isSignUp: Boolean,
    val userType: UserType?,
)
