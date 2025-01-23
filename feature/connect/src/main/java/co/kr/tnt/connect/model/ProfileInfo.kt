package co.kr.tnt.connect.model

import co.kr.tnt.domain.model.UserType

data class ProfileInfo(
    val name: String,
    val image: String?,
    val type: UserType,
)
