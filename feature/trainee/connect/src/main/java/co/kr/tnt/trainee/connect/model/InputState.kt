package co.kr.tnt.trainee.connect.model

enum class InputState {
    UNFOCUSED,
    FOCUS,
    VALID,
    INVALID,
    ;

    val isValid: Boolean
        get() = this == VALID
}
