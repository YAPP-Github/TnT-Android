package co.kr.data.network.model

import co.kr.tnt.domain.model.InviteCodeResult
import kotlinx.serialization.Serializable

@Serializable
data class InviteCodeResponse(
    val invitationCode: String,
)

fun InviteCodeResponse.toDomain(): InviteCodeResult {
    return InviteCodeResult(
        invitationCode = invitationCode,
    )
}
