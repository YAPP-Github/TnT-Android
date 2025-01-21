package co.kr.tnt.domain.model

enum class Term(val isRequired: Boolean) {
    // 서비스 이용 약관
    TERMS_OF_SERVICE(true),

    // 개인정보 처리방침
    PRIVACY_POLICY(true),
}
