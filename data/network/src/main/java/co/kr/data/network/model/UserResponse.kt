package co.kr.data.network.model

import co.kr.data.network.model.enum.MemberType
import co.kr.tnt.domain.model.User
import co.kr.tnt.domain.utils.DateFormatter
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val name: String,
    val email: String,
    val profileImageUrl: String,
    val birthday: String?,
    val memberType: MemberType,
    val socialType: String,
    val invitationCode: String,
    val height: Double?,
    val weight: Double?,
    val cautionNote: String?,
    val goalContents: List<String>,
)

fun UserResponse.toDomain(dateFormatter: DateFormatter): User {
    return when (memberType) {
        MemberType.TRAINER -> User.Trainer(
            id = "TODO",
            name = name,
            image = profileImageUrl,
        )

        MemberType.TRAINEE -> User.Trainee(
            id = "TODO",
            name = name,
            image = profileImageUrl,
            birthday = birthday?.let(dateFormatter::parse),
            weight = requireNotNull(weight),
            height = requireNotNull(height?.toInt()),
            ptPurpose = goalContents,
            caution = cautionNote,
        )

        MemberType.UNREGISTERED -> error("등록되지 않은 유저입니다.")
    }
}
