package co.kr.tnt.login

sealed class LoginException(override val message: String?) : Exception(message) {
    /** 유저가 뒤로가기 동작 등으로 로그인 자체를 취소한 경우 */
    data class CancelException(override val message: String?) : LoginException(message)

    /** 유저가 실제 로그인을 시도하였으나 SDK의 문제로 로그인에 실패한 경우 */
    data class AuthException(override val message: String?) : LoginException(message)
}
