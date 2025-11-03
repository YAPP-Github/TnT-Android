package co.kr.data.network.model

import co.kr.data.network.model.enum.MemberType
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserInfoRequest(
    val removeImage: Boolean,
    val memberType: MemberType,
    val name: String,
    val birthDay: String? = null,
    val height: Double? = null,
    val weight: Double? = null,
    val cautionNote: String? = null,
    val ptGoals: List<String>? = null,
)
