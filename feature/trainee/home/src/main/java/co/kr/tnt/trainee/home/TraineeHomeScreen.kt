package co.kr.tnt.trainee.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.designsystem.component.calendar.TnTIndicatorWeekCalendar
import co.kr.tnt.designsystem.component.calendar.model.DayIndicatorState
import co.kr.tnt.designsystem.component.calendar.model.DayState
import co.kr.tnt.designsystem.component.calendar.utils.rememberMostVisibleYearMonth
import co.kr.tnt.designsystem.component.card.TnTRecordCard
import co.kr.tnt.designsystem.component.card.TnTSessionRecordCard
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.domain.model.RecordType
import co.kr.tnt.domain.model.RecordType.PTSessionType
import co.kr.tnt.domain.model.TraineeHomeData
import co.kr.tnt.feature.trainee.home.R
import co.kr.tnt.trainee.home.TraineeHomeContract.TraineeHomeUiEvent
import co.kr.tnt.trainee.home.TraineeHomeContract.TraineeHomeUiState
import co.kr.tnt.ui.component.TnTHomeTopBar
import co.kr.tnt.ui.model.DefaultUserProfile
import co.kr.tnt.ui.model.RecordChip
import coil.compose.rememberAsyncImagePainter
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import co.kr.tnt.core.designsystem.R as uiResource

@Composable
internal fun TraineeHomeRoute(
    viewModel: TraineeHomeViewModel = hiltViewModel(),
    navigateToNotification: () -> Unit,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TraineeHomeScreen(
        state = uiState,
        onClickNotification = navigateToNotification,
        onSelectDate = { date ->
            viewModel.setEvent(TraineeHomeUiEvent.OnClickDay(date))
        },
        onClickNextWeek = { viewModel.setEvent(TraineeHomeUiEvent.OnClickNextWeek) },
        onClickPreviousWeek = { viewModel.setEvent(TraineeHomeUiEvent.OnClickPreviousWeek) },
        onClickPtSessionCard = { id ->
            viewModel.setEvent(TraineeHomeUiEvent.OnClickPtSessionCard(id))
        },
    )

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TraineeHomeContract.TraineeHomeEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@Composable
private fun TraineeHomeScreen(
    state: TraineeHomeUiState,
    onSelectDate: (LocalDate) -> Unit,
    onClickNextWeek: () -> Unit,
    onClickPreviousWeek: () -> Unit,
    onClickPtSessionCard: (String) -> Unit,
    onClickNotification: () -> Unit,
) {
    val now = LocalDate.now()
    val coroutineScope = rememberCoroutineScope()

    val weekCalendarState = rememberWeekCalendarState(
        firstDayOfWeek = DayOfWeek.SUNDAY,
        firstVisibleWeekDate = state.selectedDate,
        startDate = now.minusYears(10),
        endDate = now.plusYears(10),
    )

    val visibleYearMonth = rememberMostVisibleYearMonth(weekCalendarState)

    val filteredPTLesson = state.ptLessons.filter { it.date == state.selectedDate }
    val filteredRecord = state.recordList.filter { it.recordDate == state.selectedDate }

    val markedDates: Set<LocalDate> =
        (state.ptLessons.map { it.date } + state.recordList.map { it.recordDate }).toSet()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(TnTTheme.colors.neutralColors.Neutral100),
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(TnTTheme.colors.commonColors.Common0),
            ) {
                Spacer(Modifier.height(12.dp))
                TnTHomeTopBar(
                    yearMonth = visibleYearMonth,
                    onClickSelectorPrevious = {
                        coroutineScope.launch {
                            onClickPreviousWeek()
                            weekCalendarState.animateScrollToWeek(state.selectedDate)
                        }
                    },
                    onClickSelectorNext = {
                        coroutineScope.launch {
                            onClickNextWeek()
                            weekCalendarState.animateScrollToWeek(state.selectedDate)
                        }
                    },
                    onClickNotification = onClickNotification,
                )
                Spacer(modifier = Modifier.height(16.dp))
                TnTIndicatorWeekCalendar(
                    state = weekCalendarState,
                    dayState = { date ->
                        DayState(isSelected = date == state.selectedDate)
                    },
                    indicatorState = { date ->
                        DayIndicatorState(showIcon = date in markedDates)
                    },
                    onClickDay = { date ->
                        onSelectDate(date)
                    },
                    modifier = Modifier.background(TnTTheme.colors.commonColors.Common0),
                )
                Spacer(modifier = Modifier.height(12.dp))
                // PT Session
                if (filteredPTLesson.isNotEmpty()) {
                    filteredPTLesson.forEach { lesson ->
                        val chip = RecordChip.create(PTSessionType(lesson.session))
                        TnTSessionRecordCard(
                            name = lesson.trainerName,
                            tagText = chip.title,
                            startTime = formatTime(lesson.startTime),
                            endTime = formatTime(lesson.endTime),
                            isTrainer = false,
                            defaultImage = painterResource(DefaultUserProfile.Trainer.image),
                            leadingEmoji = chip.emoji ?: "",
                            profileImage = rememberAsyncImagePainter(lesson.trainerImage),
                            showSessionRecordCreation = false,
                            showSessionRecordDetails = lesson.hasRecord,
                            onClick = { onClickPtSessionCard(lesson.ptLessonId) },
                            modifier = Modifier.padding(
                                start = 20.dp,
                                end = 20.dp,
                                bottom = 16.dp,
                            ),
                        )
                    }
                } else {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 28.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.no_scheduled_lesson),
                            color = TnTTheme.colors.neutralColors.Neutral400,
                            style = TnTTheme.typography.label1Medium,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
            // Record
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
            ) {
                Text(
                    text = formatDateWithDay(state.selectedDate),
                    color = TnTTheme.colors.neutralColors.Neutral800,
                    style = TnTTheme.typography.h3,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        if (filteredRecord.isEmpty()) {
            item {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Column {
                        Spacer(Modifier.height(80.dp))
                        Text(
                            text = stringResource(R.string.no_record_now),
                            color = TnTTheme.colors.neutralColors.Neutral600,
                            style = TnTTheme.typography.body2Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.record_meal_and_exercise_by_click_button),
                            color = TnTTheme.colors.neutralColors.Neutral400,
                            style = TnTTheme.typography.label1Medium,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        } else {
            items(filteredRecord) { record ->
                val chip = RecordChip.create(record.recordType)
                TnTRecordCard(
                    style = chip.chipStyle,
                    record = record.recordContents,
                    tagText = chip.title,
                    time = formatTime(record.recordTime),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
                    image = record.recordImage?.let { rememberAsyncImagePainter(it) },
                    leadingEmoji = chip.emoji,
                    feedbackCount = if (record.feedbackCount == 0) null else record.feedbackCount,
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .height(200.dp)
                    .background(TnTTheme.colors.neutralColors.Neutral100),
            )
        }
    }
}

// 시간을 "오후 14:00"의 형식으로 바꿔준다
@Composable
private fun formatTime(isoString: String): String {
    val timePart = isoString.substringAfter('T').substring(0, 5)
    val hour = timePart.substring(0, 2).toInt()

    val amPm = if (hour < 12) {
        stringResource(uiResource.string.morning)
    } else {
        stringResource(uiResource.string.afternoon)
    }

    return "$amPm $timePart"
}

// 선택된 날짜를 "0월 0일 0요일"의 형식으로 바꿔준다
private fun formatDateWithDay(date: LocalDate): String {
    val monthDayFormatter = DateTimeFormatter.ofPattern("M월 d일", Locale.KOREAN)
    val monthDay = date.format(monthDayFormatter)

    val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN)

    return "$monthDay $dayOfWeek"
}

@Preview
@Composable
private fun TraineeHomeScreenPreview() {
    val now = LocalDate.now()

    val dummyUiState = TraineeHomeUiState(
        selectedDate = now,
        recordList = listOf(
            TraineeHomeData.Record(
                recordId = "VDF1D907",
                recordDate = LocalDate.of(2025, 2, 8),
                recordType = RecordType.MealType.BREAKFAST,
                recordTime = "2025-02-08T13:00:00.000Z",
                recordImage = null,
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
                recordDate = LocalDate.of(2025, 2, 8),
                recordType = RecordType.ExerciseType.CARDIO,
                recordTime = "2025-02-08T19:40:00.000Z",
                recordImage = null,
                recordContents = "런닝머신 1시간",
                feedbackCount = 0,
            ),
        ),
        ptLessons = listOf(
            TraineeHomeData.PTLesson(
                ptLessonId = "VDF1D923",
                trainerName = "김코치",
                trainerImage = "https://buly.kr/DaO1v4V",
                date = LocalDate.of(2025, 2, 8),
                session = 7,
                startTime = "2025-02-08T13:00:00.000Z",
                endTime = "2025-02-08T13:50:00.000Z",
                hasRecord = false,
            ),
            TraineeHomeData.PTLesson(
                ptLessonId = "CDK392DF",
                trainerName = "박트레이너",
                trainerImage = "https://buly.kr/DaO1v4V",
                date = LocalDate.of(2025, 2, 10),
                session = 10,
                startTime = "2025-02-10T14:30:00.000Z",
                endTime = "2025-02-10T15:30:00.000Z",
                hasRecord = false,
            ),
            TraineeHomeData.PTLesson(
                ptLessonId = "OSI93DG1",
                trainerName = "이강사",
                trainerImage = "https://buly.kr/DaO1v4V",
                date = LocalDate.of(2025, 2, 15),
                session = 15,
                startTime = "2025-02-15T18:00:00.000Z",
                endTime = "2025-02-15T19:00:00.000Z",
                hasRecord = true,
            ),
            TraineeHomeData.PTLesson(
                ptLessonId = "ASD3F013",
                trainerName = "최코치",
                trainerImage = "https://buly.kr/DaO1v4V",
                date = LocalDate.of(2025, 2, 20),
                session = 20,
                startTime = "2025-02-20T07:00:00.000Z",
                endTime = "2025-02-20T08:00:00.000Z",
                hasRecord = false,
            ),
            TraineeHomeData.PTLesson(
                ptLessonId = "CDE35K32",
                trainerName = "정트레이너",
                trainerImage = "https://buly.kr/DaO1v4V",
                date = LocalDate.of(2025, 2, 25),
                session = 25,
                startTime = "2025-02-25T16:00:00.000Z",
                endTime = "2025-02-25T17:00:00.000Z",
                hasRecord = true,
            ),
        ),
    )

    TnTTheme {
        TraineeHomeScreen(
            state = dummyUiState,
            onClickNotification = { },
            onSelectDate = {},
            onClickNextWeek = { },
            onClickPreviousWeek = { },
            onClickPtSessionCard = {},
        )
    }
}
