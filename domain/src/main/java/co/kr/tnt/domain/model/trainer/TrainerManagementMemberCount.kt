package co.kr.tnt.domain.model.trainer

data class TrainerManagementMemberCount(
    val activeCount: Int,
    val cumulativeCount: Int,
) {
    companion object {
        val ZERO = TrainerManagementMemberCount(
            activeCount = 0,
            cumulativeCount = 0,
        )
    }
}
