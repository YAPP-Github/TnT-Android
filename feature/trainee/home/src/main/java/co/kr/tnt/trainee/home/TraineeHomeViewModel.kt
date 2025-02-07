package co.kr.tnt.trainee.home

import co.kr.tnt.domain.model.RecordType
import co.kr.tnt.domain.model.TraineeHomeData
import co.kr.tnt.trainee.home.TraineeHomeContract.TraineeHomeEffect
import co.kr.tnt.trainee.home.TraineeHomeContract.TraineeHomeUiEvent
import co.kr.tnt.trainee.home.TraineeHomeContract.TraineeHomeUiState
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class TraineeHomeViewModel @Inject constructor() :
    BaseViewModel<TraineeHomeUiState, TraineeHomeUiEvent, TraineeHomeEffect>(
        TraineeHomeUiState(),
    ) {
        init {
            updateCalenderState()
        }

        override suspend fun handleEvent(event: TraineeHomeUiEvent) {
            when (event) {
                TraineeHomeUiEvent.OnClickNextWeek -> moveToNextWeek()
                TraineeHomeUiEvent.OnClickPreviousWeek -> moveToPreviousWeek()
                is TraineeHomeUiEvent.OnClickDay -> selectDate(event.date)
                is TraineeHomeUiEvent.OnClickPtSessionCard -> checkSessionRecord(event.ptSessionId)
            }
        }

        // TODO : 주간 캘린더 API 연동
        private fun updateCalenderState() {
            val today = LocalDate.now()
            val list = List(10) {
                today.minusDays((0..30).random().toLong())
            }

            // TODO : recordType -> RecordType 변환
            val recordList = listOf(
                TraineeHomeData.Record(
                    recordId = "VDF1D907",
                    recordDate = LocalDate.of(2025, 2, 8),
                    recordType = RecordType.MealType.BREAKFAST,
                    recordTime = "2025-02-08T13:00:00.000Z",
                    recordImage = "https://buly.kr/BpESNP5",
                    recordContents = "아침으로 계란 2개 먹었습니다.",
                    feedbackCount = 1,
                ),
                TraineeHomeData.Record(
                    recordId = "VDF1D907",
                    recordDate = LocalDate.of(2025, 2, 8),
                    recordType = RecordType.MealType.BREAKFAST,
                    recordTime = "2025-02-08T13:00:00.000Z",
                    recordImage = "https://buly.kr/BpESNP5",
                    recordContents = "아침으로 계란 2개 먹었습니다.",
                    feedbackCount = 0,
                ),
                TraineeHomeData.Record(
                    recordId = "VDF1D907",
                    recordDate = LocalDate.of(2025, 2, 8),
                    recordType = RecordType.MealType.DINNER,
                    recordTime = "2025-02-08T18:40:00.000Z",
                    recordImage = null,
                    recordContents = "저녁으로 소고기 먹었습니다.",
                    feedbackCount = 2,
                ),
                TraineeHomeData.Record(
                    recordId = "VDF1D907",
                    recordDate = LocalDate.of(2025, 2, 5),
                    recordType = RecordType.ExerciseType.CARDIO,
                    recordTime = "2025-02-05T19:40:00.000Z",
                    recordImage = null,
                    recordContents = "런닝머신 1시간",
                    feedbackCount = 0,
                ),
            )
            val ptLessons = listOf(
                TraineeHomeData.PTLesson(
                    ptLessonId = "lesson_1",
                    trainerName = "김코치",
                    trainerImage = "https://buly.kr/44x6xFN",
                    date = LocalDate.of(2025, 2, 7),
                    session = 7,
                    startTime = "2025-02-07T13:00:00.000Z",
                    endTime = "2025-02-07T13:50:00.000Z",
                    hasRecord = true,
                ),
                TraineeHomeData.PTLesson(
                    ptLessonId = "lesson_2",
                    trainerName = "박트레이너",
                    trainerImage = "https://buly.kr/44x6xFN",
                    date = LocalDate.of(2025, 2, 10),
                    session = 10,
                    startTime = "2025-02-10T14:30:00.000Z",
                    endTime = "2025-02-10T15:30:00.000Z",
                    hasRecord = false,
                ),
                TraineeHomeData.PTLesson(
                    ptLessonId = "lesson_3",
                    trainerName = "이강사",
                    trainerImage = "https://buly.kr/44x6xFN",
                    date = LocalDate.of(2025, 2, 15),
                    session = 15,
                    startTime = "2025-02-15T18:00:00.000Z",
                    endTime = "2025-02-15T19:00:00.000Z",
                    hasRecord = true,
                ),
                TraineeHomeData.PTLesson(
                    ptLessonId = "lesson_4",
                    trainerName = "최코치",
                    trainerImage = "https://buly.kr/44x6xFN",
                    date = LocalDate.of(2025, 2, 20),
                    session = 20,
                    startTime = "2025-02-20T07:00:00.000Z",
                    endTime = "2025-02-20T08:00:00.000Z",
                    hasRecord = false,
                ),
                TraineeHomeData.PTLesson(
                    ptLessonId = "lesson_5",
                    trainerName = "정트레이너",
                    trainerImage = "https://buly.kr/44x6xFN",
                    date = LocalDate.of(2025, 2, 25),
                    session = 25,
                    startTime = "2025-02-25T16:00:00.000Z",
                    endTime = "2025-02-25T17:00:00.000Z",
                    hasRecord = true,
                ),
            )
            updateState { copy(markedDates = list, recordList = recordList, ptLessons = ptLessons) }
        }

        private fun checkSessionRecord(ptSessionId: String) {
            // TODO: pt 수업 기록 확인 화면 이동
        }

        private fun selectDate(date: LocalDate) {
            updateState { copy(selectedDate = date) }
        }

        private fun moveToNextWeek() {
            selectDate(currentState.selectedDate.plusWeeks(1))
        }

        private fun moveToPreviousWeek() {
            selectDate(currentState.selectedDate.minusWeeks(1))
        }
    }
