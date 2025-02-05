package co.kr.data.network.model.enum

import co.kr.tnt.domain.model.UserType
import kotlinx.serialization.Serializable

@Serializable
enum class MemberType {
    TRAINER,
    TRAINEE,
    UNREGISTERED,
}

fun MemberType.toDomain(): UserType =
    when (this) {
        MemberType.TRAINER -> UserType.TRAINER
        MemberType.TRAINEE -> UserType.TRAINEE
        MemberType.UNREGISTERED -> error("등록되지 않은 유저입니다.")
    }
