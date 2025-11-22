package co.kr.tnt.domain

object UserProfilePolicy {
    // TnT 에서 사용자가 업로드할 수 있는 이미지의 최대 용량은 10MB이다.
    const val USER_IMAGE_MAX_SIZE = 10 * 1024 * 1024

    // TnT 에서 사용자가 입력할 수 있는 이름의 최대 길이는 15자이다.
    const val USER_NAME_MAX_LENGTH = 15

    // TnT 에서 사용자가 입력할 수 있는 키의 최대 길이는 3자이다.
    const val USER_HEIGHT_MAX_LENGTH = 3

    // TnT 에서 사용자가 입력할 수 있는 몸무게의 최대 길이는 5자이다. (000.0)
    const val USER_WEIGHT_MAX_LENGTH = 5

    // TnT 에서 사용자가 입력할 수 있는 주의사항의 최대 길이는 100자이다.
    const val USER_CAUTION_MAX_LENGTH = 100

    // TnT 에서 사용자가 입력할 수 있는 이름은 한글, 영어, 공백만 허용한다.
    val USER_NAME_REGEX = Regex("^[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣 ]+\$")

    // TnT 에서 사용자가 입력할 수 있는 몸무게는 소수점 이하 한 자리까지만 허용한다. (000, 00, 00.0, 000.0)
    val USER_WEIGHT_REGEX = Regex("^(\\d{1,3}(\\.\\d)?)?\$")
}
