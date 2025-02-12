package co.kr.data.network.model.trainee

import kotlinx.serialization.Serializable

@Serializable
data class MealRecordRequest(
    val date: String,
    val dietType: String,
    val memo: String,
)
