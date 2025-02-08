package co.kr.data.network.model.trainer

import co.kr.tnt.domain.model.trainer.DailyPtSessionCount
import co.kr.tnt.domain.utils.DateFormatter
import kotlinx.serialization.Serializable

@Serializable
data class MonthlyPtSessionCountsResponse(
    val calendarPtLessonCounts: List<PtSessionCountsResponse>,
)

@Serializable
data class PtSessionCountsResponse(
    val date: String,
    val count: Int,
)

fun PtSessionCountsResponse.toDomain(dateFormatter: DateFormatter) =
    DailyPtSessionCount(
        date = dateFormatter.parse(date),
        count = count,
    )
