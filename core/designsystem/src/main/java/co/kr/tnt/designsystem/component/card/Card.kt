package co.kr.tnt.designsystem.component.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.component.chip.TnTChip
import co.kr.tnt.designsystem.component.chip.model.ChipStyle
import co.kr.tnt.designsystem.theme.TnTTheme

@Composable
fun TnTRecordCard(
    style: ChipStyle,
    record: String,
    tagText: String,
    time: String,
    modifier: Modifier = Modifier,
    image: Painter? = null,
    leadingEmoji: String? = null,
    feedbackCount: Int? = null,
) {
    val maxLines = if (image == null) 2 else 4
    // 사진이 없으면 기록 아래 20.dp 간격 (20 - 12 = 8.dp)
    val recordBottomPadding = if (image == null) 8.dp else 0.dp
    // 사진이 없고 피드백이 있으면 사이에 20.dp 간격 (20 - 8 = 12.dp)
    val feedBackTopPadding = if (image == null) 12.dp else 0.dp
    val imageBottomPadding = if (feedbackCount == null) 0.dp else 12.dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(TnTTheme.colors.commonColors.Common0)
            .padding(horizontal = 12.dp, vertical = 12.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            image?.let {
                Column {
                    Image(
                        painter = image,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(140.dp)
                            .clip(RoundedCornerShape(16.dp)),
                    )
                    Spacer(modifier = Modifier.height(imageBottomPadding))
                }
                Spacer(modifier = Modifier.width(12.dp))
            }
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TnTChip(
                        text = tagText,
                        style = style,
                        leadingEmoji = leadingEmoji,
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_clock),
                            contentDescription = "clock icon",
                        )
                        Spacer(Modifier.width(2.dp))
                        Text(
                            text = time,
                            color = TnTTheme.colors.neutralColors.Neutral500,
                            style = TnTTheme.typography.label2Medium,
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    text = record,
                    color = TnTTheme.colors.neutralColors.Neutral800,
                    style = TnTTheme.typography.body1Bold,
                    textAlign = TextAlign.Start,
                    maxLines = maxLines,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                )
                Spacer(Modifier.height(recordBottomPadding))
            }
        }
        feedbackCount?.let {
            Spacer(Modifier.height(feedBackTopPadding))
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_feedback),
                    contentDescription = "feedback icon",
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = TnTTheme.typography.label2Medium.toSpanStyle(),
                        ) {
                            append(stringResource(R.string.received_feedback))
                        }
                        append(" ")
                        withStyle(
                            style = TnTTheme.typography.label2Bold.toSpanStyle(),
                        ) {
                            append(feedbackCount.toString())
                        }
                    },
                    color = TnTTheme.colors.neutralColors.Neutral500,
                    modifier = Modifier.alignBy(LastBaseline),
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 358)
@Composable
private fun TnTRecordCardPreview() {
    TnTTheme {
        TnTRecordCard(
            style = ChipStyle.BLUE,
            record = "닭가슴살 샐러드랑 고구마 먹었다 냐냠냐먀냐먀냠냠냠냠  냐냠냐먀냐먀냠냠냠냠먀냠냠냠냠  냐냠냐먀냐먀냠냠냠먀냠냠냠냠  냐냠냐먀냐먀냠냠냠",
            tagText = "상체 운동",
            time = "오전 7:30",
            modifier = Modifier.padding(10.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 358)
@Composable
private fun TnTRecordCardWithFeedbackPreview() {
    TnTTheme {
        TnTRecordCard(
            style = ChipStyle.BLUE,
            record = "닭가슴살 샐러드랑 고구마 먹었다 냐냠냐먀냐먀냠냠냠냠  냐냠냐먀냐먀냠냠냠냠먀냠냠냠냠  냐냠냐먀냐먀냠냠냠먀냠냠냠냠  냐냠냐먀냐먀냠냠냠",
            tagText = "상체 운동",
            time = "오전 7:30",
            modifier = Modifier.padding(10.dp),
            feedbackCount = 1,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 358)
@Composable
private fun TnTRecordCardWithImagePreview() {
    TnTTheme {
        TnTRecordCard(
            style = ChipStyle.PINK,
            record = "닭가슴살 샐러드랑 고구마 먹었다 냐냠냐먀냐먀냠냠냠냠  냐냠냐먀냐먀냠냠냠냠먀냠냠냠냠  냐냠냐먀냐먀냠냠냠먀냠냠냠냠  냐냠냐먀냐먀냠냠냠",
            tagText = "아침",
            time = "오전 7:30",
            modifier = Modifier.padding(10.dp),
            image = ColorPainter(Color.Gray),
            leadingEmoji = "\uD83C\uDF1E",
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 358)
@Composable
private fun TnTRecordCardWithImageAndFeedbackPreview() {
    TnTTheme {
        TnTRecordCard(
            style = ChipStyle.PINK,
            record = "닭가슴살 샐러드랑 고구마 먹었다 냐냠냐먀냐먀냠냠냠냠  냐냠냐먀냐먀냠냠냠냠먀냠냠냠냠  냐냠냐먀냐먀냠냠냠먀냠냠냠냠  냐냠냐먀냐먀냠냠냠",
            tagText = "아침",
            time = "오전 7:30",
            modifier = Modifier.padding(10.dp),
            image = ColorPainter(Color.Gray),
            leadingEmoji = "\uD83C\uDF1E",
            feedbackCount = 2,
        )
    }
}
