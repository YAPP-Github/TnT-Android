package co.kr.data.network.tnt.request

import co.kr.tnt.domain.model.TnT
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TnTRequest(
    @SerialName("id")
    val id: String,
)

fun TnT.toRemote(): TnTRequest = TnTRequest(
    id = this.id,
)
