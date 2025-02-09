package co.kr.data.network.model.trainee

import co.kr.tnt.domain.model.trainee.TraineeDailyRecordStatus
import co.kr.tnt.domain.utils.DateFormatter
import kotlinx.serialization.Serializable

@Serializable
data class MonthlyRecordedDatesResponse(
    val calendarRecordInfo: List<RecordedDateResponse>,
)

@Serializable
data class RecordedDateResponse(
    val date: String,
)

fun RecordedDateResponse.toDomain(dateFormatter: DateFormatter) =
    TraineeDailyRecordStatus(
        date = dateFormatter.parse(date),
    )
