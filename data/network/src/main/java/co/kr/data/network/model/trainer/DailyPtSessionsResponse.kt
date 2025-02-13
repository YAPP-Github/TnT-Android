package co.kr.data.network.model.trainer

import co.kr.tnt.domain.model.PtSession
import co.kr.tnt.domain.model.trainer.TrainerDailyPtSession
import co.kr.tnt.domain.utils.DateFormatter
import kotlinx.serialization.Serializable

@Serializable
data class DailyPtSessionsResponse(
    val count: Int,
    val date: String,
    val lessons: List<PtSessionResponse>,
)

@Serializable
data class PtSessionResponse(
    val ptLessonId: String,
    val traineeId: String,
    val traineeName: String,
    val traineeProfileImageUrl: String,
    val session: Int,
    val startTime: String,
    val endTime: String,
    val isCompleted: Boolean,
)

fun DailyPtSessionsResponse.toDomain(dateFormatter: DateFormatter): TrainerDailyPtSession = TrainerDailyPtSession(
    date = dateFormatter.parse(date),
    sessions = lessons.map { it.toDomain(dateFormatter) },
)

fun PtSessionResponse.toDomain(dateFormatter: DateFormatter): PtSession = PtSession(
    id = ptLessonId,
    round = session,
    traineeId = traineeId,
    traineeName = traineeName,
    traineeProfileUrl = traineeProfileImageUrl,
    startTime = dateFormatter.parseDateTime(startTime, "yyyy-MM-dd'T'HH:mm:ss"),
    endTime = dateFormatter.parseDateTime(endTime, "yyyy-MM-dd'T'HH:mm:ss"),
    isCompleted = isCompleted,
)
