package co.kr.tnt.domain.model

enum class AuthType {
    KAKAO,
    ;

    companion object {
        fun from(value: String): AuthType {
            return when (value) {
                "KAKAO" -> KAKAO
                else -> throw IllegalArgumentException("지원하지 않는 $value 입니다.")
            }
        }
    }
}
