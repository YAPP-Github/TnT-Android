package co.kr.data.network.model.trainer

import co.kr.tnt.domain.model.MemberInfo
import kotlinx.serialization.Serializable

@Serializable
data class MemberListResponse(
    val memberList: List<MemberResponse>,
)

@Serializable
data class MemberResponse(
    val traineeName: String,
    val profileUrl: String,
    val ptPurpose: String,
    val memo: String,
    val finishedPtCount: Int,
    val totalPtCount: Int,
)

fun MemberResponse.toDomain() =
    MemberInfo(
        traineeName = traineeName,
        profileUrl = profileUrl,
        ptPurpose = ptPurpose,
        memo = memo,
        finishedPtCount = finishedPtCount,
        totalPtCount = totalPtCount,
    )
