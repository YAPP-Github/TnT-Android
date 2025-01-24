package co.kr.data.network.tnt.response

import co.kr.tnt.domain.model.TnT
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TnTResponse(
    @SerialName("id")
    val id: String,
)

fun TnTResponse.toDomain(): TnT = TnT(
    id = id,
)
