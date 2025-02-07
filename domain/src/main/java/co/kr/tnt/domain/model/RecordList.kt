package co.kr.tnt.domain.model

import java.time.LocalDate

sealed class TraineeHomeData {
    data class Record(
        val recordId: String,
        val recordDate: LocalDate,
        val recordType: RecordType,
        val recordTime: String,
        val recordImage: String?,
        val recordContents: String,
        val feedbackCount: Int,
    ) : TraineeHomeData()

    data class PTLesson(
        val ptLessonId: String,
        val date: LocalDate,
        val trainerName: String,
        val trainerImage: String,
        val session: Int,
        val startTime: String,
        val endTime: String,
        val hasRecord: Boolean,
    )
}
