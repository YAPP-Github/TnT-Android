package co.kr.tnt.designsystem.component.calendar.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.component.calendar.model.DayIndicatorState
import co.kr.tnt.designsystem.component.calendar.model.DayState
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.designsystem.utils.nonScaledSp
import java.time.LocalDate

@Composable
internal fun CalendarCell(
    date: LocalDate,
    state: DayState = DayState(),
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                },
            ),
        contentAlignment = Alignment.Center,
    ) {
        CalendarDay(
            date = date,
            isSelected = state.isSelected,
        )
    }
}

@Composable
internal fun CalendarCellWithIndicator(
    date: LocalDate,
    dayState: DayState = DayState(),
    indicatorState: DayIndicatorState = DayIndicatorState(),
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onClick,
                    )
                } else {
                    Modifier
                },
            ),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CalendarDay(
                date = date,
                isSelected = dayState.isSelected,
            )
            Spacer(modifier = Modifier.height(5.dp))
            CalendarIndicator(state = indicatorState)
        }
    }
}

@Composable
private fun CalendarDay(
    date: LocalDate,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    val today = remember { LocalDate.now() }
    val isToday = date == today

    val backgroundColor = when {
        isSelected -> TnTTheme.colors.neutralColors.Neutral900
        isToday -> TnTTheme.colors.neutralColors.Neutral200
        else -> null
    }

    Box(
        modifier = modifier
            .size(32.dp)
            .then(
                if (backgroundColor != null) {
                    Modifier.background(
                        color = backgroundColor,
                        shape = RoundedCornerShape(8.dp),
                    )
                } else {
                    Modifier
                },
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            date.dayOfMonth.toString(),
            style = TnTTheme.typography.body2Medium,
            color = if (isSelected) {
                TnTTheme.colors.commonColors.Common0
            } else {
                TnTTheme.colors.neutralColors.Neutral600
            },
            fontSize = 15.sp.nonScaledSp,
        )
    }
}

@Composable
private fun CalendarIndicator(
    state: DayIndicatorState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (state.showIcon) {
            Icon(
                painter = painterResource(id = R.drawable.ic_star_candy),
                contentDescription = "star",
                tint = TnTTheme.colors.redColors.Red300,
            )
        }
        if (state.showText) {
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                state.count.toString(),
                style = TnTTheme.typography.label2Medium,
                color = TnTTheme.colors.neutralColors.Neutral400,
                fontSize = 12.sp.nonScaledSp,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarCellPreview() {
    TnTTheme {
        CalendarCell(
            date = LocalDate.now().minusDays(1),
            state = DayState(
                isSelected = false,
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarTodayCellPreview() {
    TnTTheme {
        CalendarCell(
            date = LocalDate.now(),
            state = DayState(
                isSelected = false,
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarCellSelectedPreview() {
    TnTTheme {
        CalendarCell(
            date = LocalDate.now(),
            state = DayState(
                isSelected = true,
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarCellWithIndicatorPreview() {
    TnTTheme {
        CalendarCellWithIndicator(
            date = LocalDate.now(),
            dayState = DayState(
                isSelected = false,
            ),
            indicatorState = DayIndicatorState(
                count = 6,
                showText = true,
            ),
        )
    }
}
