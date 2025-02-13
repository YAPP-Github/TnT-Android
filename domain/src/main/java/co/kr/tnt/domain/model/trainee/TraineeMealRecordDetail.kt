package co.kr.tnt.domain.model.trainee

import java.time.LocalDateTime

data class TraineeMealRecordDetail(
    val date: LocalDateTime,
    val imageUrl: String?,
    val dietType: String,
    val memo: String,
)
