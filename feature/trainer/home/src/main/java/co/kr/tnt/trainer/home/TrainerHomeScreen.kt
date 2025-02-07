package co.kr.tnt.trainer.home

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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.designsystem.component.calendar.TnTIndicatorMonthCalendar
import co.kr.tnt.designsystem.component.calendar.model.DayIndicatorState
import co.kr.tnt.designsystem.component.calendar.model.DayState
import co.kr.tnt.designsystem.component.calendar.utils.rememberMostVisibleMonth
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.trainer.home.TrainerHomeContract.TrainerHomeSideEffect
import co.kr.tnt.trainer.home.TrainerHomeContract.TrainerHomeUiEvent
import co.kr.tnt.trainer.home.TrainerHomeContract.TrainerHomeUiState
import co.kr.tnt.ui.component.TnTHomeTopBar
import com.kizitonwose.calendar.compose.rememberCalendarState
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@Composable
internal fun TrainerHomeRoute(
    viewModel: TrainerHomeViewModel = hiltViewModel(),
    navigateToNotification: () -> Unit,
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    TrainerHomeScreen(
        state = state,
        onClickNotification = { viewModel.setEvent(TrainerHomeUiEvent.OnClickNotification) },
        onChangeVisibleMonth = { viewModel.setEvent(TrainerHomeUiEvent.OnChangeVisibleMonth(it)) },
        onClickDay = { viewModel.setEvent(TrainerHomeUiEvent.OnClickDay(it)) },
    )

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                TrainerHomeSideEffect.NavigateToNotification -> navigateToNotification()
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
) {
    val now = remember { YearMonth.now() }
    val calendarState = rememberCalendarState(
        firstVisibleMonth = now,
        firstDayOfWeek = DayOfWeek.SUNDAY,
        startMonth = now.minusYears(10),
        endMonth = now.plusYears(10),
    )
    val coroutineScope = rememberCoroutineScope()
    val visibleYearMonth = rememberMostVisibleMonth(calendarState)

    Scaffold(
        topBar = {
            Column {
                Spacer(modifier = Modifier.height(12.dp))
                TnTHomeTopBar(
                    yearMonth = visibleYearMonth,
                    onClickNotification = onClickNotification,
                    onClickSelectorPrevious = {
                        coroutineScope.launch {
                            calendarState.animateScrollToMonth(visibleYearMonth.minusMonths(1))
                        }
                    },
                    onClickSelectorNext = {
                        coroutineScope.launch {
                            calendarState.animateScrollToMonth(visibleYearMonth.plusMonths(1))
                        }
                    },
                )
            }
        },
        containerColor = TnTTheme.colors.commonColors.Common0,
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Spacer(modifier = Modifier.height(16.dp))
            TnTIndicatorMonthCalendar(
                state = calendarState,
                onClickDay = onClickDay,
                dayState = { day -> DayState(isSelected = day == state.selectedDay) },
                indicatorState = { day ->
                    val count = state.dailyPtSessionCount[day] ?: 0

                    DayIndicatorState(
                        count = count,
                        showIcon = count != 0,
                        showText = count != 0,
                    )
                },
            )
        }
    }

    LaunchedEffect(calendarState.firstVisibleMonth) {
        val currentCalendarYearMonth = calendarState.firstVisibleMonth.yearMonth
        onChangeVisibleMonth(currentCalendarYearMonth)
    }
}
