package co.kr.data.network.model.trainer

import co.kr.tnt.domain.model.MemberInfo
import kotlinx.serialization.Serializable

@Serializable
data class ActiveTraineesResponse(
    val traineeCount: Int,
    val trainees: List<TraineeInfoResponse>,
)

@Serializable
data class TraineeInfoResponse(
    val id: Long,
    val name: String,
    val profileImageUrl: String,
    val finishedPtCount: Int,
    val totalPtCount: Int,
    val memo: String?,
    val ptGoals: List<String>,
)

fun TraineeInfoResponse.toDomain() =
    MemberInfo(
        traineeName = name,
        profileUrl = profileImageUrl,
        ptPurpose = ptGoals.joinToString(", "),
        memo = memo,
        finishedPtCount = finishedPtCount,
        totalPtCount = totalPtCount,
    )
