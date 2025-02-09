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
import androidx.compose.runtime.remember
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
import co.kr.tnt.domain.model.trainee.DailyRecord
import co.kr.tnt.domain.model.trainee.PtSession
import co.kr.tnt.domain.model.trainee.TraineeDailyRecordStatus
import co.kr.tnt.domain.utils.DateFormatter
import co.kr.tnt.feature.trainee.home.R
import co.kr.tnt.trainee.home.TraineeHomeContract.TraineeHomeUiEvent
import co.kr.tnt.trainee.home.TraineeHomeContract.TraineeHomeUiState
import co.kr.tnt.ui.component.TnTHomeTopBar
import co.kr.tnt.ui.model.DefaultUserProfile
import co.kr.tnt.ui.model.RecordChip
import coil.compose.rememberAsyncImagePainter
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

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
        onChangeVisibleMonth = { yearMonth ->
            viewModel.setEvent(TraineeHomeUiEvent.OnChangeVisibleMonth(yearMonth))
        },
        onClickDay = { day ->
            viewModel.setEvent(TraineeHomeUiEvent.OnClickDay(day))
        },
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
    onClickNotification: () -> Unit,
    onChangeVisibleMonth: (YearMonth) -> Unit,
    onClickDay: (LocalDate) -> Unit,
    onClickPtSessionCard: (String) -> Unit,
) {
    val dateFormatter = remember { DateFormatter() }
    val coroutineScope = rememberCoroutineScope()

    val weekCalendarState = rememberWeekCalendarState(
        firstDayOfWeek = DayOfWeek.SUNDAY,
        firstVisibleWeekDate = state.selectedDay,
        startDate = state.selectedDay.minusYears(10),
        endDate = state.selectedDay.plusYears(10),
    )
    var visibleYearMonth = rememberMostVisibleYearMonth(weekCalendarState)

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
                            val previousWeek = weekCalendarState.firstVisibleWeek.days.first().date.minusWeeks(1)
                            visibleYearMonth = YearMonth.from(previousWeek)
                            weekCalendarState.animateScrollToWeek(previousWeek)
                        }
                    },
                    onClickSelectorNext = {
                        coroutineScope.launch {
                            val nextWeek = weekCalendarState.firstVisibleWeek.days.first().date.plusWeeks(1)
                            visibleYearMonth = YearMonth.from(nextWeek)
                            weekCalendarState.animateScrollToWeek(nextWeek)
                        }
                    },
                    onClickNotification = onClickNotification,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Calendar(
                    weekCalendarState = weekCalendarState,
                    selectedDay = state.selectedDay,
                    dailyDataState = state.dailyRecordStatus,
                    onClickDay = onClickDay,
                )
                Spacer(modifier = Modifier.height(12.dp))
                if (state.ptSessions != null) {
                    DailyPtSession(
                        session = state.ptSessions,
                        dateFormatter = dateFormatter,
                        onClickPtSessionCard = onClickPtSessionCard,
                    )
                } else {
                    EmptyPtSession()
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
            ) {
                Text(
                    text = dateFormatter.format(
                        date = state.selectedDay,
                        pattern = "M월 d일 EEEE",
                    ),
                    color = TnTTheme.colors.neutralColors.Neutral800,
                    style = TnTTheme.typography.h3,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        if (state.recordList.isEmpty()) {
            item { EmptyDailyRecords() }
        } else {
            items(state.recordList) { record ->
                DailyRecords(
                    record = record,
                    dateFormatter = dateFormatter,
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .height(100.dp)
                    .background(TnTTheme.colors.neutralColors.Neutral100),
            )
        }
    }

    LaunchedEffect(visibleYearMonth) {
        onChangeVisibleMonth(visibleYearMonth)
    }
}

@Composable
private fun Calendar(
    weekCalendarState: WeekCalendarState,
    selectedDay: LocalDate,
    dailyDataState: List<TraineeDailyRecordStatus>,
    onClickDay: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    TnTIndicatorWeekCalendar(
        state = weekCalendarState,
        dayState = { day -> DayState(isSelected = day == selectedDay) },
        indicatorState = { day ->
            DayIndicatorState(showIcon = dailyDataState.any { it.date == day })
        },
        onClickDay = onClickDay,
        modifier = modifier.background(TnTTheme.colors.commonColors.Common0),
    )
}

@Composable
private fun DailyPtSession(
    session: PtSession,
    dateFormatter: DateFormatter,
    onClickPtSessionCard: (sessionId: String) -> Unit,
) {
    val chip = RecordChip.create(PTSessionType(session.session))
    TnTSessionRecordCard(
        name = session.trainerName,
        tagText = chip.title,
        startTime = dateFormatter.format(session.startTime, "a hh:mm"),
        endTime = dateFormatter.format(session.endTime, "a hh:mm"),
        isTrainer = false,
        defaultImage = painterResource(DefaultUserProfile.Trainer.image),
        leadingEmoji = chip.emoji ?: "",
        profileImage = session.trainerImage?.let { rememberAsyncImagePainter(it) },
        showSessionRecordCreation = false,
        showSessionRecordDetails = session.hasRecord,
        onClick = { onClickPtSessionCard(session.ptSessionId) },
        modifier = Modifier.padding(
            start = 20.dp,
            end = 20.dp,
            bottom = 16.dp,
        ),
    )
}

@Composable
private fun EmptyPtSession() {
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

@Composable
private fun DailyRecords(
    record: DailyRecord,
    dateFormatter: DateFormatter,
) {
    val chip = RecordChip.create(record.recordType)
    TnTRecordCard(
        style = chip.chipStyle,
        record = record.recordContents,
        tagText = chip.title,
        time = dateFormatter.format(record.recordTime, "a hh:mm"),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
        image = record.recordImage?.let { rememberAsyncImagePainter(it) },
        leadingEmoji = chip.emoji,
        feedbackCount = if (record.feedbackCount == 0) null else record.feedbackCount,
    )
}

@Composable
private fun EmptyDailyRecords() {
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

@Preview
@Composable
private fun TraineeHomeScreenPreview() {
    val date = LocalDate.of(2025, 2, 8)

    val dummyUiState = TraineeHomeUiState(
        selectedDay = date,
        recordList = listOf(
            DailyRecord(
                recordId = "VDF1D907",
                recordType = RecordType.MealType.BREAKFAST,
                recordTime = LocalDateTime.of(2025, 2, 8, 8, 0, 0),
                recordImage = "https://buly.kr/BpESNP5",
                recordContents = "아침으로 계란 2개 먹었습니다.",
                feedbackCount = 1,
            ),
            DailyRecord(
                recordId = "VDF1D907",
                recordType = RecordType.MealType.LUNCH,
                recordTime = LocalDateTime.of(2025, 2, 8, 13, 0, 0),
                recordImage = "https://buly.kr/BpESNP5",
                recordContents = "점심으로 계란 5개 먹었습니다.",
                feedbackCount = 0,
            ),
        ),
        ptSessions = PtSession(
            ptSessionId = "OSI93DG1",
            trainerName = "이강사",
            trainerImage = "https://buly.kr/DaO1v4V",
            session = 15,
            startTime = LocalDateTime.of(2025, 2, 3, 18, 0, 0),
            endTime = LocalDateTime.of(2025, 2, 3, 19, 0, 0),
            hasRecord = true,
        ),
    )

    TnTTheme {
        TraineeHomeScreen(
            state = dummyUiState,
            onClickNotification = { },
            onClickDay = { },
            onClickPtSessionCard = { },
            onChangeVisibleMonth = { },
        )
    }
}
