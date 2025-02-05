package co.kr.data.network.model

import co.kr.data.network.model.enum.MemberType
import co.kr.tnt.domain.model.UserType
import kotlinx.serialization.Serializable

@Serializable
data class CheckSessionResponse(
    val memberType: MemberType,
)

fun CheckSessionResponse.toDomain(): UserType =
    when (memberType) {
        MemberType.TRAINER -> UserType.TRAINER
        MemberType.TRAINEE -> UserType.TRAINEE
        MemberType.UNREGISTERED -> throw IllegalArgumentException("등록되지 않은 유저입니다.")
    }
