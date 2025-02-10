package co.kr.data.network.model.trainer

import co.kr.tnt.domain.model.MemberInfo
import kotlinx.serialization.Serializable

@Serializable
data class ActiveTraineesResponse(
    val traineeCount: Int,
    val memberList: List<TraineeInfoResponse>,
)

@Serializable
data class TraineeInfoResponse(
    val id: Int,
    val name: String,
    val profileUrl: String,
    val finishedPtCount: Int,
    val totalPtCount: Int,
    val memo: String?,
    val goalContents: String,
)

fun TraineeInfoResponse.toDomain() =
    MemberInfo(
        traineeName = name,
        profileUrl = profileUrl,
        ptPurpose = goalContents,
        memo = memo,
        finishedPtCount = finishedPtCount,
        totalPtCount = totalPtCount,
    )
