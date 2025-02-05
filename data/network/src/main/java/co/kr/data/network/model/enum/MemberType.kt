package co.kr.data.network.model.enum

import kotlinx.serialization.Serializable

@Serializable
enum class MemberType {
    TRAINER,
    TRAINEE,
    UNREGISTERED,
}
