package co.kr.tnt.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.designsystem.utils.nonScaledSp
import java.time.LocalTime
import java.util.Locale
import kotlin.math.abs

@Composable
fun TnTWheelTimePicker(
    modifier: Modifier = Modifier,
    initialTime: LocalTime = LocalTime.now(),
    minuteInterval: Int = 10,
    onTimeSelected: (LocalTime) -> Unit,
) {
    val roundedInitialTime by remember { mutableStateOf(getRoundedTime(initialTime, minuteInterval)) }
    val am = stringResource(R.string.morning)
    val pm = stringResource(R.string.afternoon)

    var selectedHour by remember {
        mutableIntStateOf(if (roundedInitialTime.hour % 12 == 0) 12 else roundedInitialTime.hour % 12)
    }
    var selectedMinute by remember { mutableIntStateOf(roundedInitialTime.minute) }
    var selectedDayPart by remember { mutableStateOf(if (roundedInitialTime.hour < 12) am else pm) }

    val hourList = (1..12).toList()
    val minuteList = (0..59 step minuteInterval).toList()
    val dayPartList = listOf(am, pm)

    LaunchedEffect(roundedInitialTime) {
        onTimeSelected(roundedInitialTime)
    }

    fun updateTime(
        newHour: Int = selectedHour,
        newMinute: Int = selectedMinute,
        newPeriod: String = selectedDayPart,
    ) {
        val hourIn24 = when {
            newPeriod == am && newHour == 12 -> 0
            newPeriod == pm && newHour != 12 -> newHour + 12
            else -> newHour
        }
        onTimeSelected(LocalTime.of(hourIn24, newMinute))
    }

    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(TnTTheme.colors.neutralColors.Neutral100),
        )
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        ) {
            TimeWheel(
                items = hourList,
                selectedItem = selectedHour,
                onItemSelected = { newHour ->
                    if (selectedHour != newHour) {
                        selectedHour = newHour
                        updateTime(newHour = newHour)
                    }
                },
                modifier = Modifier.weight(1f),
            )
            Text(
                text = ":",
                modifier = Modifier.align(Alignment.CenterVertically),
                style = TnTTheme.typography.h4,
                color = TnTTheme.colors.neutralColors.Neutral900,
            )
            TimeWheel(
                items = minuteList,
                selectedItem = selectedMinute,
                onItemSelected = { newMinute ->
                    if (selectedMinute != newMinute) {
                        selectedMinute = newMinute
                        updateTime(newMinute = newMinute)
                    }
                },
                isMinute = true,
                modifier = Modifier.weight(1f),
            )
            DayPartWheel(
                items = dayPartList,
                selectedItem = selectedDayPart,
                onItemSelected = { newPeriod ->
                    if (selectedDayPart != newPeriod) {
                        selectedDayPart = newPeriod
                        updateTime(newPeriod = newPeriod)
                    }
                },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

private fun getRoundedTime(inputTime: LocalTime, minuteInterval: Int): LocalTime {
    if (minuteInterval == 1 || inputTime.minute % minuteInterval == 0) {
        return inputTime
    }
    val roundedMinute = ((inputTime.minute / 10.0).toInt() + 1) * 10
    val newHour = if (roundedMinute == 60) (inputTime.hour + 1) % 24 else inputTime.hour
    val newMinute = if (roundedMinute == 60) 0 else roundedMinute

    return LocalTime.of(newHour, newMinute)
}

@Composable
private fun TimeWheel(
    items: List<Int>,
    selectedItem: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isMinute: Boolean = false,
    itemHeight: Dp = 27.dp,
    gap: Dp = 8.dp,
) {
    val containerHeight = (itemHeight * 5) + (gap * 4)
    val scrollStartIndex = (Int.MAX_VALUE / 2) - ((Int.MAX_VALUE / 2) % items.size)
    val initialScrollIndex = scrollStartIndex - 2 + items.indexOf(selectedItem)
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialScrollIndex)

    val snappingLayout = remember(listState) { SnapLayoutInfoProvider(listState) }
    val snapFlingBehavior = rememberSnapFlingBehavior(snappingLayout)

    val centeredItemIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val viewportCenter = layoutInfo.viewportSize.height / 2

            layoutInfo.visibleItemsInfo.minByOrNull { item ->
                abs((item.offset + item.size / 2) - viewportCenter)
            }?.index?.rem(items.size) ?: selectedItem
        }
    }

    LaunchedEffect(centeredItemIndex) {
        val newSelectedItem = items[centeredItemIndex]
        if (newSelectedItem != selectedItem) {
            onItemSelected(newSelectedItem)
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.height(containerHeight),
    ) {
        LazyColumn(
            state = listState,
            flingBehavior = snapFlingBehavior,
            verticalArrangement = Arrangement.spacedBy(gap),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(Int.MAX_VALUE) { index ->
                val item = items[index % items.size]
                val isSelected = index % items.size == centeredItemIndex

                Box(
                    modifier = Modifier.height(itemHeight),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = if (isMinute) {
                            String.format(Locale.getDefault(), "%02d", item)
                        } else {
                            item.toString()
                        },
                        textAlign = TextAlign.Center,
                        color = if (isSelected) {
                            TnTTheme.colors.neutralColors.Neutral900
                        } else {
                            TnTTheme.colors.neutralColors.Neutral400
                        },
                        style = TnTTheme.typography.h4,
                        fontSize = 18.sp.nonScaledSp,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Composable
fun DayPartWheel(
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    itemHeight: Dp = 27.dp,
    gap: Dp = 8.dp,
) {
    val containerHeight = (itemHeight * 3) + (gap * 2)

    val initialIndex = items.indexOf(selectedItem)

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val snapLayoutProvider = remember { SnapLayoutInfoProvider(listState) }
    val snapFlingBehavior = rememberSnapFlingBehavior(snapLayoutProvider)

    val centeredItemIndex by remember {
        derivedStateOf { listState.firstVisibleItemIndex }
    }

    LaunchedEffect(centeredItemIndex) {
        val newSelectedItem = items[centeredItemIndex]
        if (newSelectedItem != selectedItem) {
            onItemSelected(newSelectedItem)
        }
    }

    val height = (itemHeight * 5) + (gap * 4)
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.height(height),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.height(containerHeight),
        ) {
            LazyColumn(
                state = listState,
                flingBehavior = snapFlingBehavior,
                contentPadding = PaddingValues(vertical = itemHeight + gap),
                verticalArrangement = Arrangement.spacedBy(gap),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(items.size) { index ->
                    val item = items[index]
                    val isSelected = item == items[centeredItemIndex]

                    Box(
                        modifier = Modifier.height(itemHeight),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = item,
                            textAlign = TextAlign.Center,
                            color = if (isSelected) {
                                TnTTheme.colors.neutralColors.Neutral900
                            } else {
                                TnTTheme.colors.neutralColors.Neutral400
                            },
                            style = TnTTheme.typography.h4,
                            fontSize = 18.sp.nonScaledSp,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TnTTimePickerPreview() {
    TnTTheme {
        TnTWheelTimePicker(
            initialTime = LocalTime.now(),
            onTimeSelected = {},
        )
    }
}
