package co.kr.tnt.trainer.home

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.component.button.TnTFabButton
import co.kr.tnt.designsystem.component.calendar.TnTIndicatorMonthCalendar
import co.kr.tnt.designsystem.component.calendar.model.DayIndicatorState
import co.kr.tnt.designsystem.component.calendar.model.DayState
import co.kr.tnt.designsystem.component.calendar.utils.rememberMostVisibleMonth
import co.kr.tnt.designsystem.component.card.TnTSessionRecordCard
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.domain.model.PtSession
import co.kr.tnt.domain.utils.DateFormatter
import co.kr.tnt.trainer.home.TrainerHomeContract.TrainerHomeSideEffect
import co.kr.tnt.trainer.home.TrainerHomeContract.TrainerHomeUiEvent
import co.kr.tnt.trainer.home.TrainerHomeContract.TrainerHomeUiState
import co.kr.tnt.ui.component.TnTHomeTopBar
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.rememberCalendarState
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@Composable
internal fun TrainerHomeRoute(
    viewModel: TrainerHomeViewModel = hiltViewModel(),
    navigateToNotification: () -> Unit,
    navigateToAddPtSession: () -> Unit,
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    TrainerHomeScreen(
        state = state,
        onClickNotification = { viewModel.setEvent(TrainerHomeUiEvent.OnClickNotification) },
        onChangeVisibleMonth = { viewModel.setEvent(TrainerHomeUiEvent.OnChangeVisibleMonth(it)) },
        onClickDay = { viewModel.setEvent(TrainerHomeUiEvent.OnClickDay(it)) },
        onClickAddPtSession = { viewModel.setEvent(TrainerHomeUiEvent.OnClickAddPtSession) },
    )

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                TrainerHomeSideEffect.NavigateToNotification -> navigateToNotification()
                TrainerHomeSideEffect.NavigateToAddPtSession -> navigateToAddPtSession()
                is TrainerHomeSideEffect.ShowToast -> Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
private fun TrainerHomeScreen(
    state: TrainerHomeUiState,
    onClickNotification: () -> Unit,
    onChangeVisibleMonth: (yearMonth: YearMonth) -> Unit,
    onClickDay: (date: LocalDate) -> Unit,
    onClickAddPtSession: () -> Unit,
) {
    val now = remember { YearMonth.now() }
    val calendarState = rememberCalendarState(
        firstVisibleMonth = now,
        firstDayOfWeek = DayOfWeek.SUNDAY,
        startMonth = now.minusYears(10),
        endMonth = now.plusYears(10),
    )
    val coroutineScope = rememberCoroutineScope()
    val visibleMonth = rememberMostVisibleMonth(calendarState)
    val dateFormatter = remember { DateFormatter() }

    Box {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(TnTTheme.colors.neutralColors.Neutral100),
        ) {
            item {
                Column(
                    modifier = Modifier.background(color = TnTTheme.colors.commonColors.Common0),
                ) {
                    Spacer(modifier = Modifier.height(12.dp))
                    TnTHomeTopBar(
                        modifier = Modifier.fillMaxWidth(),
                        yearMonth = visibleMonth,
                        onClickNotification = onClickNotification,
                        onClickSelectorPrevious = {
                            coroutineScope.launch {
                                calendarState.animateScrollToMonth(visibleMonth.minusMonths(1))
                            }
                        },
                        onClickSelectorNext = {
                            coroutineScope.launch {
                                calendarState.animateScrollToMonth(visibleMonth.plusMonths(1))
                            }
                        },
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Calendar(
                        modifier = Modifier.background(color = TnTTheme.colors.commonColors.Common0),
                        calendarState = calendarState,
                        selectedDay = state.selectedDay,
                        dailyPtSessionCount = state.dailyPtSessionCount,
                        onClickDay = onClickDay,
                    )
                }
                Column {
                    Spacer(modifier = Modifier.height(24.dp))
                    DailyPtSessionTitle(
                        day = state.selectedDay,
                        sessionCount = state.selectedDayPtSessions?.size ?: 0,
                        dateFormatter = dateFormatter,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
            state.selectedDayPtSessions?.let { selectDayPtSessions ->
                if (selectDayPtSessions.isEmpty()) {
                    item { EmptyPtSessions() }
                    return@let
                }

                items(count = selectDayPtSessions.size) { index ->
                    PtSessionCard(
                        ptSession = selectDayPtSessions[index],
                        dateFormatter = dateFormatter,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
        TnTFabButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    bottom = 22.dp,
                    end = 28.dp,
                ),
            text = "수업 추가",
            enabled = true,
            leadingComposable = {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = "add pt session",
                )
            },
            onClick = onClickAddPtSession,
        )
    }

    LaunchedEffect(visibleMonth) {
        onChangeVisibleMonth(visibleMonth)
    }
}

@Composable
private fun Calendar(
    calendarState: CalendarState,
    selectedDay: LocalDate,
    dailyPtSessionCount: Map<LocalDate, Int>,
    onClickDay: (date: LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    TnTIndicatorMonthCalendar(
        modifier = modifier,
        state = calendarState,
        onClickDay = onClickDay,
        dayState = { day -> DayState(isSelected = day == selectedDay) },
        indicatorState = { day ->
            val count = dailyPtSessionCount[day] ?: 0

            DayIndicatorState(
                count = count,
                showIcon = count != 0,
                showText = count != 0,
            )
        },
    )
}

@Composable
private fun DailyPtSessionTitle(
    day: LocalDate,
    sessionCount: Int,
    dateFormatter: DateFormatter,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = dateFormatter.format(
                date = day,
                pattern = "M월 d일 EEEE",
            ),
            color = TnTTheme.colors.neutralColors.Neutral800,
            style = TnTTheme.typography.h3,
        )
        Text(
            text = buildAnnotatedString {
                append("\uD83E\uDDE8 ")
                withStyle(
                    style = SpanStyle(
                        color = TnTTheme.colors.redColors.Red500,
                        fontWeight = FontWeight.Bold,
                    ),
                ) {
                    append(sessionCount.toString())
                }
                append("개의 수업이 있어요")
            },
            style = TnTTheme.typography.label2Medium,
            color = TnTTheme.colors.neutralColors.Neutral800,
        )
    }
}

@Composable
private fun EmptyPtSessions(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(282.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "아직 등록된 수업이 없어요.",
            style = TnTTheme.typography.body2Bold,
            color = TnTTheme.colors.neutralColors.Neutral600,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "추가 버튼을 눌러 PT 수업 일정을 추가해 보세요",
            style = TnTTheme.typography.label1Medium,
            color = TnTTheme.colors.neutralColors.Neutral400,
        )
    }
}

@Composable
private fun PtSessionCard(
    ptSession: PtSession,
    dateFormatter: DateFormatter,
    modifier: Modifier = Modifier,
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(ptSession.traineeProfileUrl)
            .placeholder(R.drawable.img_default)
            .error(R.drawable.img_default)
            .build(),
    )

    Row(
        modifier = modifier.padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = { })
                .padding(4.dp),
            painter = painterResource(
                if (ptSession.isCompleted) {
                    R.drawable.ic_fill_check_true
                } else {
                    R.drawable.ic_fill_check_false
                },
            ),
            contentDescription = null,
            tint = Color.Unspecified,
        )
        Spacer(modifier = Modifier.width(20.dp))
        ptSession.run {
            // TODO 컴포넌트 개선
            TnTSessionRecordCard(
                isTrainer = true,
                name = traineeName,
                tagText = "${round}회차 수업",
                startTime = dateFormatter.format(startTime, "a hh:mm"),
                endTime = dateFormatter.format(endTime, "a hh:mm"),
                defaultImage = painterResource(R.drawable.img_default),
                profileImage = painter,
                leadingEmoji = "\uD83D\uDCAA",
                showSessionRecordCreation = ptSession.isCompleted,
                onClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun TrainerHomeScreenPreview() {
    TnTTheme {
        TrainerHomeScreen(
            state = TrainerHomeUiState(),
            onClickNotification = { },
            onChangeVisibleMonth = { },
            onClickDay = { },
            onClickAddPtSession = { },
        )
    }
}
