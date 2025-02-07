package co.kr.tnt.trainee.home

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.designsystem.component.calendar.TnTIndicatorWeekCalendar
import co.kr.tnt.designsystem.component.calendar.model.DayIndicatorState
import co.kr.tnt.designsystem.component.calendar.model.DayState
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.trainee.home.TraineeHomeContract.TraineeHomeUiEvent
import co.kr.tnt.trainee.home.TraineeHomeContract.TraineeHomeUiState
import co.kr.tnt.ui.component.TnTHomeTopBar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
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
        onSelectDate = { date ->
            viewModel.setEvent(TraineeHomeUiEvent.OnClickDay(date))
        },
        onClickNextWeek = { viewModel.setEvent(TraineeHomeUiEvent.OnClickNextWeek) },
        onClickPreviousWeek = { viewModel.setEvent(TraineeHomeUiEvent.OnClickPreviousWeek) },
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
    onClickNotification: () -> Unit,
) {
    val now = LocalDate.now()
    val coroutineScope = rememberCoroutineScope()

    val weekCalendarState = rememberWeekCalendarState(
        firstDayOfWeek = DayOfWeek.SUNDAY,
        firstVisibleWeekDate = state.selectedDate,
        startDate = now.minusWeeks(6),
        endDate = now.plusWeeks(6),
    )

    Scaffold(
        containerColor = TnTTheme.colors.commonColors.Common0,
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TnTHomeTopBar(
                yearMonth = state.visibleYearMonth,
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
                    DayIndicatorState(showIcon = date in state.markedDates)
                },
                onClickDay = { date ->
                    onSelectDate(date)
                },
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Preview
@Composable
private fun TraineeHomeScreenPreview() {
    val now = LocalDate.now()

    val dummyUiState = TraineeHomeUiState(
        selectedDate = now,
        visibleYearMonth = YearMonth.from(now),
        markedDates = List(5) { now.minusDays(it.toLong() * 2) },
    )

    TnTTheme {
        TraineeHomeScreen(
            state = dummyUiState,
            onClickNotification = { },
            onSelectDate = {},
            onClickNextWeek = { },
            onClickPreviousWeek = { },
        )
    }
}
