package co.kr.tnt.domain

object UserProfilePolicy {
    // TnT 에서 사용자가 업로드할 수 있는 이미지의 최대 용량은 10MB이다.
    const val USER_IMAGE_MAX_SIZE = 10 * 1024 * 1024

    // TnT 에서 사용자가 입력할 수 있는 이름의 최대 길이는 15자이다.
    const val USER_NAME_MAX_LENGTH = 15

    // TnT 에서 사용자가 입력할 수 있는 이름은 한글, 영어, 공백만 허용한다.
    val USER_NAME_REGEX = Regex("^[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣 ]+\$")
}
