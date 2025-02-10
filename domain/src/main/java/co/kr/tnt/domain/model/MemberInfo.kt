package co.kr.tnt.domain.model

data class MemberInfo(
    val traineeName: String,
    val profileUrl: String,
    val ptPurpose: String,
    val memo: String,
    val finishedPtCount: Int,
    val totalPtCount: Int,
)
