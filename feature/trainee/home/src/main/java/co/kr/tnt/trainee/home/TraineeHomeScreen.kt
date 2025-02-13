package co.kr.tnt.trainee.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.designsystem.component.TnTModalBottomSheet
import co.kr.tnt.designsystem.component.calendar.TnTIndicatorWeekCalendar
import co.kr.tnt.designsystem.component.calendar.model.DayIndicatorState
import co.kr.tnt.designsystem.component.calendar.model.DayState
import co.kr.tnt.designsystem.component.calendar.utils.rememberMostVisibleYearMonth
import co.kr.tnt.designsystem.component.card.TnTRecordCard
import co.kr.tnt.designsystem.component.card.TnTSessionRecordCard
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.domain.model.DailyRecord
import co.kr.tnt.domain.model.RecordType
import co.kr.tnt.domain.model.RecordType.PTSessionType
import co.kr.tnt.domain.model.trainee.TraineeDailyRecordStatus
import co.kr.tnt.domain.model.trainee.TraineePtSession
import co.kr.tnt.domain.utils.DateFormatter
import co.kr.tnt.feature.trainee.home.R
import co.kr.tnt.trainee.home.TraineeHomeContract.TraineeHomeEffect
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TraineeHomeRoute(
    viewModel: TraineeHomeViewModel = hiltViewModel(),
    navigateToNotification: () -> Unit,
    navigateToExerciseRecord: () -> Unit,
    navigateToMealRecord: () -> Unit,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

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
        onClickFloatingButton = { showBottomSheet = true },
    )

    if (showBottomSheet) {
        TnTModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                showBottomSheet = false
            },
            content = {
                RecordBottomSheetContent(
                    onClickExercise = { viewModel.setEvent(TraineeHomeUiEvent.OnClickExerciseRecord) },
                    onClickDiet = { viewModel.setEvent(TraineeHomeUiEvent.OnClickMealRecord) },
                )
            },
        )
    }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TraineeHomeEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }

                TraineeHomeEffect.NavigateToExerciseRecord -> {
                    showBottomSheet = false
                    navigateToExerciseRecord()
                }

                TraineeHomeEffect.NavigateToMealRecord -> {
                    showBottomSheet = false
                    navigateToMealRecord()
                }
            }
        }
    }

    // TODO 홈 화면 진입 시마다 데이터 조회 재고 필요
    LaunchedEffect(true) { viewModel.setEvent(TraineeHomeUiEvent.OnScreen) }
}

@Composable
private fun TraineeHomeScreen(
    state: TraineeHomeUiState,
    onClickNotification: () -> Unit,
    onChangeVisibleMonth: (YearMonth) -> Unit,
    onClickDay: (day: LocalDate) -> Unit,
    onClickPtSessionCard: (id: Long) -> Unit,
    onClickFloatingButton: () -> Unit,
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TnTTheme.colors.neutralColors.Neutral100),
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
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
                                val previousWeek =
                                    weekCalendarState.firstVisibleWeek.days.first().date.minusWeeks(
                                        1,
                                    )
                                visibleYearMonth = YearMonth.from(previousWeek)
                                weekCalendarState.animateScrollToWeek(previousWeek)
                            }
                        },
                        onClickSelectorNext = {
                            coroutineScope.launch {
                                val nextWeek =
                                    weekCalendarState.firstVisibleWeek.days.first().date.plusWeeks(1)
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
                        dailyDataState = state.recordedDates,
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
        FloatingActionButton(
            onClick = onClickFloatingButton,
            shape = RoundedCornerShape(12.dp),
            containerColor = TnTTheme.colors.neutralColors.Neutral900,
            elevation = FloatingActionButtonDefaults.elevation(0.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 20.dp, end = 12.dp),
        ) {
            Icon(
                painter = painterResource(co.kr.tnt.core.designsystem.R.drawable.ic_add),
                contentDescription = null,
                tint = Color.White,
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
    dailyDataState: TraineeDailyRecordStatus?,
    onClickDay: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    TnTIndicatorWeekCalendar(
        state = weekCalendarState,
        dayState = { day -> DayState(isSelected = day == selectedDay) },
        indicatorState = { day ->
            DayIndicatorState(showIcon = dailyDataState?.dates?.contains(day) ?: false)
        },
        onClickDay = onClickDay,
        modifier = modifier.background(TnTTheme.colors.commonColors.Common0),
    )
}

@Composable
private fun DailyPtSession(
    session: TraineePtSession,
    dateFormatter: DateFormatter,
    onClickPtSessionCard: (sessionId: Long) -> Unit,
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
        showFeedback = record.hasFeedback,
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

@Composable
private fun RecordBottomSheetContent(
    onClickExercise: () -> Unit,
    onClickDiet: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
    ) {
        Text(
            text = stringResource(R.string.what_kind_of_record_would_you_like_to_make),
            color = TnTTheme.colors.neutralColors.Neutral900,
            style = TnTTheme.typography.h3,
            modifier = Modifier.padding(vertical = 20.dp),
        )
        RecordItem(
            icon = "\uD83C\uDFCB\uD83C\uDFFB\u200D♀\uFE0F",
            text = "개인 운동",
            modifier = Modifier.clickable(onClick = onClickExercise),
        )
        Spacer(Modifier.height(12.dp))
        RecordItem(
            icon = "\uD83E\uDD57",
            text = "식단",
            modifier = Modifier.clickable(onClick = onClickDiet),
        )
        Spacer(Modifier.height(54.dp))
    }
}

@Composable
private fun RecordItem(
    icon: String,
    text: String,
    modifier: Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Text(
            text = icon,
            style = TnTTheme.typography.h3,
        )
        Text(
            text = text,
            color = TnTTheme.colors.neutralColors.Neutral600,
            style = TnTTheme.typography.body1SemiBold,
        )
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
                recordId = 0L,
                recordType = RecordType.MealType.BREAKFAST,
                recordTime = LocalDateTime.of(2025, 2, 8, 8, 0, 0),
                recordImage = "https://buly.kr/BpESNP5",
                recordContents = "아침으로 계란 2개 먹었습니다.",
                hasFeedback = false,
            ),
            DailyRecord(
                recordId = 0L,
                recordType = RecordType.MealType.LUNCH,
                recordTime = LocalDateTime.of(2025, 2, 8, 13, 0, 0),
                recordImage = "https://buly.kr/BpESNP5",
                recordContents = "점심으로 계란 5개 먹었습니다.",
                hasFeedback = true,
            ),
        ),
        ptSessions = TraineePtSession(
            ptSessionId = 0L,
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
            onClickFloatingButton = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecordBottomSheetContentPreview() {
    TnTTheme {
        RecordBottomSheetContent(
            onClickExercise = { },
            onClickDiet = { },
        )
    }
}
