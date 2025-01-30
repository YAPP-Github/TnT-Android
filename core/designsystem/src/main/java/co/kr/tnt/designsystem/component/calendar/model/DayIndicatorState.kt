package co.kr.tnt.designsystem.component.calendar.model

import androidx.compose.runtime.Stable

@Stable
data class DayIndicatorState(
    val count: Int = 0,
    val showIcon: Boolean = true,
    val showText: Boolean = false,
)
