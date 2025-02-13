package co.kr.data.network.model.trainee

import co.kr.tnt.domain.model.trainee.TraineeMealRecordDetail
import co.kr.tnt.domain.utils.DateFormatter
import kotlinx.serialization.Serializable

@Serializable
data class MealRecordDetailResponse(
    val dietId: Long,
    val date: String,
    val dietImageUrl: String?,
    val dietType: String,
    val memo: String,
)

fun MealRecordDetailResponse.toDomain(dateFormatter: DateFormatter) =
    TraineeMealRecordDetail(
        date = dateFormatter.parseDateTime(date),
        imageUrl = dietImageUrl,
        dietType = dietType,
        memo = memo,
    )
