package co.kr.data.network.model

import co.kr.data.network.model.enum.MemberType
import co.kr.tnt.domain.model.User
import co.kr.tnt.domain.model.trainer.TrainerManagementMemberCount
import co.kr.tnt.domain.utils.DateFormatter
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val name: String,
    val email: String,
    val profileImageUrl: String,
    val memberType: MemberType,
    val socialType: String,
    val trainer: TrainerResponse?,
    val trainee: TraineeResponse?,
)

@Serializable
data class TrainerResponse(
    val activeTraineeCount: Int?,
    val totalTraineeCount: Int?,
)

@Serializable
data class TraineeResponse(
    val birthday: String?,
    val age: Int?,
    val height: Double,
    val weight: Double,
    val cautionNote: String?,
    val ptGoals: List<String>,
)

fun UserResponse.toDomain(dateFormatter: DateFormatter): User {
    return when (memberType) {
        MemberType.TRAINER -> User.Trainer(
            id = "TODO",
            name = name,
            image = profileImageUrl,
            memberCounts = TrainerManagementMemberCount(
                activeCount = trainer?.activeTraineeCount ?: 0,
                totalCount = trainer?.totalTraineeCount ?: 0,
            ),
        )

        MemberType.TRAINEE -> User.Trainee(
            id = "TODO",
            name = name,
            image = profileImageUrl,
            birthday = trainee?.birthday?.let(dateFormatter::parse),
            weight = requireNotNull(trainee?.weight),
            height = requireNotNull(trainee?.height?.toInt()),
            ptPurpose = trainee?.ptGoals ?: emptyList(),
            caution = trainee?.cautionNote,
            isConnected = false,
        )

        MemberType.UNREGISTERED -> error("등록되지 않은 유저입니다.")
    }
}
