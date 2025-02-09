package co.kr.data.network.model.trainee

import co.kr.tnt.domain.model.RecordType
import co.kr.tnt.domain.model.RecordType.ExerciseType
import co.kr.tnt.domain.model.RecordType.MealType
import co.kr.tnt.domain.model.trainee.DailyRecord
import co.kr.tnt.domain.model.trainee.PtSession
import co.kr.tnt.domain.model.trainee.TraineeDailyRecord
import co.kr.tnt.domain.utils.DateFormatter
import kotlinx.serialization.Serializable

@Serializable
data class DailyRecordsResponse(
    val date: String,
    val lessons: PtSessionResponse?,
    val records: List<RecordResponse>,
)

@Serializable
data class PtSessionResponse(
    val ptSessionId: String,
    val trainerName: String,
    val trainerImage: String?,
    val session: Int,
    val startTime: String,
    val endTime: String,
    val hasRecord: Boolean,
)

@Serializable
data class RecordResponse(
    val recordId: String,
    val recordType: String,
    val recordTime: String,
    val recordImage: String?,
    val recordContents: String,
    val feedbackCount: Int,
)

fun DailyRecordsResponse.toDomain(dateFormatter: DateFormatter) =
    TraineeDailyRecord(
        date = dateFormatter.parse(date),
        ptSession = lessons?.toDomain(dateFormatter),
        record = records.map { it.toDomain(dateFormatter) },
    )

fun PtSessionResponse.toDomain(dateFormatter: DateFormatter) = PtSession(
    ptSessionId = ptSessionId,
    trainerName = trainerName,
    trainerImage = trainerImage,
    session = session,
    startTime = dateFormatter.parseDateTime(startTime),
    endTime = dateFormatter.parseDateTime(endTime),
    hasRecord = hasRecord,
)

fun RecordResponse.toDomain(dateFormatter: DateFormatter) = DailyRecord(
    recordId = recordId,
    recordType = recordType.toRecordType() ?: MealType.BREAKFAST,
    recordTime = dateFormatter.parseDateTime(recordTime),
    recordImage = recordImage,
    recordContents = recordContents,
    feedbackCount = feedbackCount,
)

// TODO : 수정
fun String.toRecordType(): RecordType? {
    return when (this.uppercase()) {
        "BREAKFAST" -> MealType.BREAKFAST
        "LUNCH" -> MealType.LUNCH
        "DINNER" -> MealType.DINNER
        "SNACK" -> MealType.SNACK

        "UPPER_BODY" -> ExerciseType.UPPER_BODY
        "LOWER_BODY" -> ExerciseType.LOWER_BODY
        "BACK" -> ExerciseType.BACK
        "SHOULDER" -> ExerciseType.SHOULDER
        "CARDIO" -> ExerciseType.CARDIO

        else -> null
    }
}
