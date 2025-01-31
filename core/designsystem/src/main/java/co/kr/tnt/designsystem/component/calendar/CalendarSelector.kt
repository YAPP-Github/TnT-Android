package co.kr.tnt.designsystem.component.calendar

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.theme.TnTTheme
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun TnTCalendarSelector(
    onClickPrevious: () -> Unit,
    onClickNext: () -> Unit,
    modifier: Modifier = Modifier,
    yearMonth: YearMonth = YearMonth.now(),
) {
    val context = LocalContext.current
    val dateFormatter = remember {
        DateTimeFormatter.ofPattern(context.getString(R.string.year_month_format))
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onClickPrevious) {
            Icon(
                painter = painterResource(id = R.drawable.ic_bold_arrow_left),
                contentDescription = "arrow left",
                tint = TnTTheme.colors.neutralColors.Neutral300,
            )
        }
        Text(
            dateFormatter.format(yearMonth),
            style = TnTTheme.typography.h3,
            color = TnTTheme.colors.neutralColors.Neutral900,
        )
        IconButton(onClick = onClickNext) {
            Icon(
                painter = painterResource(id = R.drawable.ic_bold_arrow_right),
                contentDescription = "arrow right",
                tint = TnTTheme.colors.neutralColors.Neutral300,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TnTCalendarSelectorPreview() {
    TnTTheme {
        TnTCalendarSelector(
            onClickPrevious = { },
            onClickNext = { },
        )
    }
}
