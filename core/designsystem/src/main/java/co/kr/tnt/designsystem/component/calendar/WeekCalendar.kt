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
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.daysOfWeek
import java.time.LocalDate

@Composable
fun TnTWeekCalendar(
    modifier: Modifier = Modifier,
    state: WeekCalendarState = rememberWeekCalendarState(),
    dayState: (day: LocalDate) -> DayState = { DayState() },
    onClickDay: ((day: LocalDate) -> Unit)? = null,
) {
    val daysOfWeek = remember { daysOfWeek(state.firstDayOfWeek) }

    WeekCalendar(
        modifier = modifier,
        state = state,
        weekHeader = {
            WeekLabels(daysOfWeek = daysOfWeek)
        },
        dayContent = { day ->
            CalendarCell(
                date = day.date,
                state = dayState(day.date),
                onClick = { onClickDay?.invoke(day.date) },
            )
        },
    )
}

@Composable
fun TnTIndicatorWeekCalendar(
    modifier: Modifier = Modifier,
    state: WeekCalendarState = rememberWeekCalendarState(),
    dayState: (day: LocalDate) -> DayState = { DayState() },
    indicatorState: (day: LocalDate) -> DayIndicatorState = { DayIndicatorState() },
    onClickDay: ((day: LocalDate) -> Unit)? = null,
) {
    val daysOfWeek = remember { daysOfWeek(state.firstDayOfWeek) }

    WeekCalendar(
        modifier = modifier,
        state = state,
        weekHeader = {
            WeekLabels(daysOfWeek = daysOfWeek)
        },
        dayContent = { day ->
            CalendarCellWithIndicator(
                date = day.date,
                dayState = dayState(day.date),
                indicatorState = indicatorState(day.date),
                onClick = { onClickDay?.invoke(day.date) },
            )
        },
    )
}

@Preview(showBackground = true)
@Preview(showBackground = true, widthDp = 500)
@Composable
private fun TnTWeekCalendarPreview() {
    val now = LocalDate.now()

    TnTTheme {
        TnTWeekCalendar(
            state = rememberWeekCalendarState(
                startDate = now.minusMonths(6),
                endDate = now.plusMonths(6),
                firstVisibleWeekDate = now,
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
private fun TnTIndicatorWeekCalendarPreview() {
    val now = LocalDate.now()

    TnTTheme {
        TnTIndicatorWeekCalendar(
            state = rememberWeekCalendarState(
                startDate = now.minusMonths(6),
                endDate = now.plusMonths(6),
                firstVisibleWeekDate = now,
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
