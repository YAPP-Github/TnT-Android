package co.kr.tnt.designsystem.component.calendar.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import com.kizitonwose.calendar.compose.CalendarLayoutInfo
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarState
import com.kizitonwose.calendar.core.CalendarMonth
import kotlinx.coroutines.flow.filterNotNull
import java.time.LocalDate
import java.time.YearMonth

/**
 * 주어진 [viewportPercent] 를 기준으로, 현재 화면상으로 가장 많이 보이는 '월' 을 반환합니다.
 */
@Composable
fun rememberMostVisibleMonth(
    state: CalendarState,
    viewportPercent: Float = 90f,
): YearMonth {
    val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
    LaunchedEffect(state) {
        snapshotFlow { state.layoutInfo.firstMostVisibleMonth(viewportPercent) }
            .filterNotNull()
            .collect { month -> visibleMonth.value = month }
    }
    return visibleMonth.value.yearMonth
}

private fun CalendarLayoutInfo.firstMostVisibleMonth(viewportPercent: Float = 50f): CalendarMonth? {
    return if (visibleMonthsInfo.isEmpty()) {
        null
    } else {
        val viewportSize = (viewportEndOffset + viewportStartOffset) * viewportPercent / 100f
        visibleMonthsInfo.firstOrNull { itemInfo ->
            if (itemInfo.offset < 0) {
                itemInfo.offset + itemInfo.size >= viewportSize
            } else {
                itemInfo.size - itemInfo.offset >= viewportSize
            }
        }?.month
    }
}

/**
 * 화면에 보여지는 7일 중 과반 이상 (4일) 보여지는 '월' 을 반환합니다.
 */
@Composable
fun rememberMostVisibleYearMonth(
    state: WeekCalendarState,
): YearMonth {
    val visibleYearMonth = remember { mutableStateOf(YearMonth.now()) }

    LaunchedEffect(state) {
        snapshotFlow { state.firstVisibleWeek.days.map { it.date } }
            .collect { visibleDays ->
                val dominantYearMonth = getDominantYearMonth(visibleDays)
                visibleYearMonth.value = dominantYearMonth
            }
    }

    return visibleYearMonth.value
}

private fun getDominantYearMonth(visibleDays: List<LocalDate>): YearMonth {
    val yearMonthCount = visibleDays.groupingBy { YearMonth.from(it) }.eachCount()

    return yearMonthCount.entries
        .sortedByDescending { it.value }
        .firstOrNull { it.value >= 4 }?.key
        ?: YearMonth.from(visibleDays.first())
}
