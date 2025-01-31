package co.kr.tnt.designsystem.component.calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import co.kr.tnt.designsystem.component.calendar.composable.CalendarCell
import co.kr.tnt.designsystem.component.calendar.composable.CalendarCellWithIndicator
import co.kr.tnt.designsystem.component.calendar.composable.WeekLabels
import co.kr.tnt.designsystem.component.calendar.model.DayIndicatorState
import co.kr.tnt.designsystem.component.calendar.model.DayState
import co.kr.tnt.designsystem.theme.TnTTheme
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.yearMonth
import java.time.LocalDate

@Composable
fun TnTMonthCalendar(
    modifier: Modifier = Modifier,
    state: CalendarState = rememberCalendarState(),
    dayState: (day: LocalDate) -> DayState = { DayState() },
    onClickDay: ((day: LocalDate) -> Unit)? = null,
) {
    val daysOfWeek = remember { daysOfWeek(state.firstDayOfWeek) }

    HorizontalCalendar(
        modifier = modifier,
        state = state,
        monthHeader = {
            WeekLabels(daysOfWeek = daysOfWeek)
        },
        dayContent = { day ->
            if (day.position == DayPosition.MonthDate) {
                CalendarCell(
                    date = day.date,
                    state = dayState(day.date),
                    onClick = { onClickDay?.invoke(day.date) },
                )
            }
        },
    )
}

@Composable
fun TnTIndicatorMonthCalendar(
    modifier: Modifier = Modifier,
    state: CalendarState = rememberCalendarState(),
    dayState: (day: LocalDate) -> DayState = { DayState() },
    indicatorState: (day: LocalDate) -> DayIndicatorState = { DayIndicatorState() },
    onClickDay: ((day: LocalDate) -> Unit)? = null,
) {
    val daysOfWeek = remember { daysOfWeek(state.firstDayOfWeek) }

    HorizontalCalendar(
        modifier = modifier,
        state = state,
        monthHeader = {
            WeekLabels(daysOfWeek = daysOfWeek)
        },
        dayContent = { day ->
            if (day.position == DayPosition.MonthDate) {
                CalendarCellWithIndicator(
                    date = day.date,
                    dayState = dayState(day.date),
                    indicatorState = indicatorState(day.date),
                    onClick = { onClickDay?.invoke(day.date) },
                )
            }
        },
    )
}

@Preview(showBackground = true)
@Preview(showBackground = true, widthDp = 500)
@Composable
private fun TnTMonthCalendarPreview() {
    val now = LocalDate.now()

    TnTTheme {
        TnTMonthCalendar(
            state = rememberCalendarState(
                startMonth = now.yearMonth.minusMonths(12),
                endMonth = now.yearMonth.plusMonths(12),
                firstVisibleMonth = now.yearMonth,
            ),
            dayState = { date ->
                DayState(isSelected = date == now)
            },
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, widthDp = 500)
@Composable
private fun TnTIndicatorMonthCalendarPreview() {
    val now = LocalDate.now()

    TnTTheme {
        TnTIndicatorMonthCalendar(
            state = rememberCalendarState(
                startMonth = now.yearMonth.minusMonths(12),
                endMonth = now.yearMonth.plusMonths(12),
                firstVisibleMonth = now.yearMonth,
            ),
            dayState = { date ->
                DayState(isSelected = date == now)
            },
            indicatorState = indicator@{ date ->
                if (date.dayOfMonth % 2 == 0) {
                    return@indicator DayIndicatorState(
                        count = 10,
                        showText = true,
                    )
                }

                DayIndicatorState(
                    showIcon = true,
                )
            },
        )
    }
}
