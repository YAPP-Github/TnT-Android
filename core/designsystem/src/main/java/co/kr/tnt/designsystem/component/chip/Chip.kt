package co.kr.tnt.designsystem.component.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.kr.tnt.designsystem.component.chip.model.ChipStyle
import co.kr.tnt.designsystem.theme.TnTTheme

@Composable
fun TnTChip(
    text: String,
    chipStyle: ChipStyle,
    modifier: Modifier = Modifier,
    leadingEmoji: String? = null,
) {
    val backgroundColor = chipStyle.backgroundColor()
    val textColor = chipStyle.textColor()

    Row(
        modifier = modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .defaultMinSize(28.dp)
            .padding(horizontal = 8.dp, vertical = 6.dp),
    ) {
        leadingEmoji?.let {
            Text(
                text = it,
                style = TnTTheme.typography.label1Bold,
            )
            Spacer(modifier = Modifier.width(2.dp))
        }
        Text(
            text = text,
            color = textColor,
            style = TnTTheme.typography.label1Bold,
        )
    }
}

@Composable
fun TnTProfileSessionChip(
    completedSessions: String,
    totalSessions: String,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = ChipStyle.BLUE.backgroundColor()
    val textColor = ChipStyle.BLUE.textColor()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .defaultMinSize(minHeight = 24.dp)
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        Text(
            text = "\uD83D\uDCAA",
            style = TnTTheme.typography.label1Bold,
        )
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = TnTTheme.typography.caption1.toSpanStyle()
                        .copy(fontWeight = FontWeight.Bold),
                ) {
                    append(completedSessions)
                }
                withStyle(
                    style = TnTTheme.typography.caption1.toSpanStyle()
                        .copy(letterSpacing = 2.sp),
                ) {
                    append("/")
                }
                append(totalSessions)
                append("회")
            },
            color = textColor,
            textAlign = TextAlign.Center,
            style = TnTTheme.typography.caption1,
        )
    }
}

@Preview(showBackground = true, widthDp = 100, heightDp = 40)
@Composable
private fun TnTExerciseChipPreview() {
    TnTTheme {
        TnTChip(
            text = "8회차 수업",
            chipStyle = ChipStyle.BLUE,
            leadingEmoji = "💪",
        )
    }
}

@Preview(showBackground = true, widthDp = 100, heightDp = 40)
@Composable
private fun TnTChipWithoutEmojiPreview() {
    TnTTheme {
        TnTChip(
            text = "등 운동",
            chipStyle = ChipStyle.BLUE,
        )
    }
}

@Preview(showBackground = true, widthDp = 100, heightDp = 40)
@Composable
private fun TnTMealChipPreview() {
    TnTTheme {
        TnTChip(
            text = "아침",
            chipStyle = ChipStyle.PINK,
            leadingEmoji = "\uD83C\uDF1E",
        )
    }
}

@Preview(showBackground = true, widthDp = 100, heightDp = 40)
@Composable
private fun TnTProfileChipPreview() {
    TnTTheme {
        TnTProfileSessionChip(
            completedSessions = "9",
            totalSessions = "20",
        )
    }
}
