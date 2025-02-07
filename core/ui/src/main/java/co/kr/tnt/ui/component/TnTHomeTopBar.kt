package co.kr.tnt.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.component.calendar.TnTCalendarSelector
import co.kr.tnt.designsystem.theme.TnTTheme
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TnTHomeTopBar(
    modifier: Modifier = Modifier,
    yearMonth: YearMonth = YearMonth.now(),
    onClickSelectorPrevious: () -> Unit = { },
    onClickSelectorNext: () -> Unit = { },
    onClickNotification: () -> Unit = { },
    windowInsets: WindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
) {
    Column {
        CenterAlignedTopAppBar(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    vertical = 8.dp,
                    horizontal = 16.dp,
                ),
            title = {
                TnTCalendarSelector(
                    yearMonth = yearMonth,
                    onClickPrevious = onClickSelectorPrevious,
                    onClickNext = onClickSelectorNext,
                )
            },
            actions = {
                IconButton(
                    onClick = onClickNotification,
                    modifier = Modifier.size(32.dp),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_alarm),
                        contentDescription = "Go to notification screen",
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = TnTTheme.colors.commonColors.Common0,
            ),
            windowInsets = windowInsets,
            expandedHeight = 48.dp,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TnTHomeTopBarPreview() {
    TnTTheme {
        TnTHomeTopBar()
    }
}
