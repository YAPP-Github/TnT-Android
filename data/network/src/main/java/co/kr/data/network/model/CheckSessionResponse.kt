package co.kr.data.network.model

import co.kr.data.network.model.enum.MemberType
import kotlinx.serialization.Serializable

@Serializable
data class CheckSessionResponse(
    val memberType: MemberType,
    val isConnected: Boolean,
)
