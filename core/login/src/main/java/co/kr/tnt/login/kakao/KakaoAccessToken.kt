package co.kr.tnt.login.kakao

import co.kr.tnt.login.LoginAccessToken

@JvmInline
value class KakaoAccessToken(override val value: String) : LoginAccessToken
