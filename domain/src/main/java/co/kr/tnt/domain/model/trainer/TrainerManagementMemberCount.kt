package co.kr.tnt.domain.model.trainer

data class TrainerManagementMemberCount(
    val activeCount: Int,
    val totalCount: Int,
) {
    companion object {
        val ZERO = TrainerManagementMemberCount(
            activeCount = 0,
            totalCount = 0,
        )
    }
}
