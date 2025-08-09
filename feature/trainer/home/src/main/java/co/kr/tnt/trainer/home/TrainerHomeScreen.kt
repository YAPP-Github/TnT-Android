package co.kr.tnt.trainer.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.core.designsystem.R.drawable.ic_add
import co.kr.tnt.core.designsystem.R.drawable.ic_fill_check_false
import co.kr.tnt.core.designsystem.R.drawable.ic_fill_check_true
import co.kr.tnt.core.designsystem.R.drawable.img_default
import co.kr.tnt.core.ui.R.string.core_connect
import co.kr.tnt.core.ui.R.string.core_do_not_see_for_three_days
import co.kr.tnt.core.ui.R.string.core_next_time
import co.kr.tnt.designsystem.component.TnTPopupDialog
import co.kr.tnt.designsystem.component.button.TnTFabButton
import co.kr.tnt.designsystem.component.calendar.TnTIndicatorMonthCalendar
import co.kr.tnt.designsystem.component.calendar.model.DayIndicatorState
import co.kr.tnt.designsystem.component.calendar.model.DayState
import co.kr.tnt.designsystem.component.calendar.utils.rememberMostVisibleMonth
import co.kr.tnt.designsystem.component.card.TnTSessionRecordCard
import co.kr.tnt.designsystem.snackbar.LocalSnackbar
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.domain.model.PtSession
import co.kr.tnt.domain.utils.DateFormatter
import co.kr.tnt.feature.trainer.home.R
import co.kr.tnt.navigation.model.ScreenMode
import co.kr.tnt.trainer.home.TrainerHomeContract.TrainerHomeSideEffect
import co.kr.tnt.trainer.home.TrainerHomeContract.TrainerHomeUiEvent
import co.kr.tnt.trainer.home.TrainerHomeContract.TrainerHomeUiState
import co.kr.tnt.ui.component.TnTCheckToggleDialog
import co.kr.tnt.ui.component.TnTHomeTopBar
import co.kr.tnt.ui.utils.throttled
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
    padding: PaddingValues,
    navigateToNotification: () -> Unit,
    navigateToAddPtSession: (selectedDate: String) -> Unit,
    navigateToInvite: (ScreenMode) -> Unit,
) {
    val toast = LocalSnackbar.current
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    TrainerHomeScreen(
        state = state,
        padding = padding,
        onClickNotification = { viewModel.setEvent(TrainerHomeUiEvent.OnClickNotification) },
        onChangeVisibleMonth = { viewModel.setEvent(TrainerHomeUiEvent.OnChangeVisibleMonth(it)) },
        onClickDay = { viewModel.setEvent(TrainerHomeUiEvent.OnClickDay(it)) },
        onClickAddPtSession = { viewModel.setEvent(TrainerHomeUiEvent.OnClickAddPtSession) },
        onClickPtSessionComplete = { viewModel.setEvent(TrainerHomeUiEvent.OnClickPtSessionComplete(it)) },
    )

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                TrainerHomeSideEffect.NavigateToNotification -> navigateToNotification()
                TrainerHomeSideEffect.NavigateToAddPtSession -> navigateToAddPtSession(state.selectedDay.toString())
                TrainerHomeSideEffect.NavigateToInvite -> navigateToInvite(ScreenMode.CLOSE)
                is TrainerHomeSideEffect.ShowToast -> toast.show(
                    message = effect.message.asString(context),
                    icon = effect.type.iconRes,
                )
            }
        }
    }

    when (state.dialogState) {
        TrainerHomeUiState.DialogState.NONE -> Unit
        TrainerHomeUiState.DialogState.HOME_CONNECT -> {
            TnTCheckToggleDialog(
                title = stringResource(R.string.please_connect_member),
                content = stringResource(R.string.cannot_add_pt_without_connection),
                isChecked = state.isDialogHiddenForThreeDays,
                checkToggleText = stringResource(core_do_not_see_for_three_days),
                leftButtonText = stringResource(core_next_time),
                rightButtonText = stringResource(core_connect),
                onLeftButtonClick = { viewModel.setEvent(TrainerHomeUiEvent.OnDismissDialog) },
                onRightButtonClick = { viewModel.setEvent(TrainerHomeUiEvent.OnConfirmConnectDialog) },
                onClickCheck = { viewModel.setEvent(TrainerHomeUiEvent.OnChangeHideDialogOption) },
                onDismiss = { viewModel.setEvent(TrainerHomeUiEvent.OnDismissDialog) },
            )
        }

        TrainerHomeUiState.DialogState.ADD_PT_CONNECT -> {
            TnTPopupDialog(
                title = stringResource(R.string.please_connect_member),
                content = stringResource(R.string.cannot_add_pt_without_connection),
                leftButtonText = stringResource(core_next_time),
                rightButtonText = stringResource(core_connect),
                onLeftButtonClick = { viewModel.setEvent(TrainerHomeUiEvent.OnDismissDialog) },
                onRightButtonClick = { viewModel.setEvent(TrainerHomeUiEvent.OnConfirmConnectDialog) },
                onDismiss = { viewModel.setEvent(TrainerHomeUiEvent.OnDismissDialog) },
            )
        }
    }

    // TODO 홈 화면 진입 시마다 데이터 조회 재고 필요
    LaunchedEffect(true) { viewModel.setEvent(TrainerHomeUiEvent.OnScreen) }
}

@Composable
private fun TrainerHomeScreen(
    state: TrainerHomeUiState,
    padding: PaddingValues,
    onClickNotification: () -> Unit,
    onChangeVisibleMonth: (yearMonth: YearMonth) -> Unit,
    onClickDay: (date: LocalDate) -> Unit,
    onClickAddPtSession: () -> Unit,
    onClickPtSessionComplete: (PtSession) -> Unit,
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

    Box(modifier = Modifier.padding(padding)) {
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
                    Spacer(modifier = Modifier.height(24.dp))
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
                        onClickComplete = onClickPtSessionComplete,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
                item { Spacer(modifier = Modifier.height(84.dp)) }
            }
        }
        TnTFabButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    bottom = 22.dp,
                    end = 28.dp,
                ),
            text = stringResource(R.string.add_pt_session),
            enabled = true,
            leadingComposable = {
                Icon(
                    painter = painterResource(ic_add),
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
                append(stringResource(R.string.pt_session_count_suffix))
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
            text = stringResource(R.string.no_pt_session_yet),
            style = TnTTheme.typography.body2Bold,
            color = TnTTheme.colors.neutralColors.Neutral600,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.press_add_button_to_add_pt_session),
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
    onClickComplete: (PtSession) -> Unit,
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(ptSession.traineeProfileUrl)
            .placeholder(img_default)
            .error(img_default)
            .build(),
    )

    Row(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .animateContentSize(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .then(
                    if (ptSession.isCompleted.not()) {
                        Modifier.clickable(onClick = throttled { onClickComplete(ptSession) })
                    } else {
                        Modifier
                    },
                )
                .padding(4.dp),
            painter = painterResource(
                if (ptSession.isCompleted) {
                    ic_fill_check_true
                } else {
                    ic_fill_check_false
                },
            ),
            contentDescription = null,
            tint = Color.Unspecified,
        )
        Spacer(modifier = Modifier.width(20.dp))
        ptSession.run {
            // TODO 컴포넌트 개선
            // TODO 정식 출시 후 PT 수업 기록 남기기 활성화
            TnTSessionRecordCard(
                isTrainer = true,
                name = traineeName,
                tagText = stringResource(R.string.pt_session_count, round),
                startTime = dateFormatter.format(startTime, "a hh:mm"),
                endTime = dateFormatter.format(endTime, "a hh:mm"),
                defaultImage = painterResource(img_default),
                profileImage = painter,
                leadingEmoji = "\uD83D\uDCAA",
                showSessionRecordCreation = false,
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
            padding = PaddingValues(),
            onClickNotification = { },
            onChangeVisibleMonth = { },
            onClickDay = { },
            onClickAddPtSession = { },
            onClickPtSessionComplete = { },
        )
    }
}
