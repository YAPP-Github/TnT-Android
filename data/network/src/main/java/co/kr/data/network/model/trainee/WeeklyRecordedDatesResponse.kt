package co.kr.data.network.model.trainee

import co.kr.tnt.domain.model.trainee.TraineeDailyRecordStatus
import co.kr.tnt.domain.utils.DateFormatter
import kotlinx.serialization.Serializable

@Serializable
data class WeeklyRecordedDatesResponse(
    val ptLessonDates: List<String>,
)

fun WeeklyRecordedDatesResponse.toDomain(dateFormatter: DateFormatter) =
    TraineeDailyRecordStatus(
        ptLessonDates.map { date ->
            dateFormatter.parse(date)
        },
    )
