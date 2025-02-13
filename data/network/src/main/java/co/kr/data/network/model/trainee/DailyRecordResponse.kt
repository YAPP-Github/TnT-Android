package co.kr.data.network.model.trainee

import co.kr.tnt.domain.model.DailyRecord
import co.kr.tnt.domain.model.RecordType
import co.kr.tnt.domain.model.RecordType.ExerciseType
import co.kr.tnt.domain.model.RecordType.MealType
import co.kr.tnt.domain.model.trainee.TraineeDailyRecord
import co.kr.tnt.domain.model.trainee.TraineePtSession
import co.kr.tnt.domain.utils.DateFormatter
import kotlinx.serialization.Serializable

@Serializable
data class DailyRecordsResponse(
    val date: String,
    val ptInfo: PtSessionResponse?,
    val diets: List<DietRecordResponse>,
)

@Serializable
data class PtSessionResponse(
    val trainerName: String,
    val trainerProfileImage: String?,
    val session: Int,
    val lessonStart: String,
    val lessonEnd: String,
)

@Serializable
data class DietRecordResponse(
    val dietId: Long,
    val date: String,
    val dietImageUrl: String?,
    val dietType: String,
    val memo: String,
)

fun DailyRecordsResponse.toDomain(dateFormatter: DateFormatter) =
    TraineeDailyRecord(
        date = dateFormatter.parse(date),
        ptSession = ptInfo?.toDomain(dateFormatter),
        record = diets.map { it.toDomain(dateFormatter) },
    )

fun PtSessionResponse.toDomain(dateFormatter: DateFormatter) = TraineePtSession(
    // TODO : pt 수업 id
    ptSessionId = 0L,
    trainerName = trainerName,
    trainerImage = trainerProfileImage,
    session = session,
    startTime = dateFormatter.parseDateTime(lessonStart),
    endTime = dateFormatter.parseDateTime(lessonEnd),
    // TODO : 수업 기록 존재 여부 반영
    hasRecord = false,
)

fun DietRecordResponse.toDomain(dateFormatter: DateFormatter) = DailyRecord(
    recordId = dietId,
    recordType = dietType.toRecordType() ?: MealType.BREAKFAST,
    recordTime = dateFormatter.parseDateTime(date),
    recordImage = dietImageUrl,
    recordContents = memo,
    // TODO 피드백 존재 여부 반영
    hasFeedback = false,
)

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
