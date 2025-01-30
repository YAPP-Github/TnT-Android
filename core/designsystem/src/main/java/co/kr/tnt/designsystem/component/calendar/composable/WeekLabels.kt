package co.kr.tnt.designsystem.component.calendar.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.ConfigurationCompat
import androidx.core.os.LocaleListCompat
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.designsystem.utils.nonScaledSp
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun WeekLabels(
    daysOfWeek: List<DayOfWeek>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
    ) {
        daysOfWeek.forEach { dayOfWeek ->
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp.nonScaledSp,
                style = TnTTheme.typography.label2Medium,
                color =
                    if (dayOfWeek == DayOfWeek.SUNDAY) {
                        TnTTheme.colors.redColors.Red500
                    } else {
                        TnTTheme.colors.neutralColors.Neutral400
                    },
                text = dayOfWeek.getDisplayName(
                    TextStyle.SHORT,
                    getLocale(),
                ),
            )
        }
    }
}

@Composable
@ReadOnlyComposable
private fun getLocale(): Locale {
    val configuration = LocalConfiguration.current
    return ConfigurationCompat.getLocales(configuration).get(0)
        ?: requireNotNull(LocaleListCompat.getDefault()[0])
}
