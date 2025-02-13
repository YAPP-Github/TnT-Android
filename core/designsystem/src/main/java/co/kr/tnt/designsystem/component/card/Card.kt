package co.kr.tnt.designsystem.component.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.component.chip.TnTChip
import co.kr.tnt.designsystem.component.chip.TnTProfileSessionChip
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
    showFeedback: Boolean = false,
) {
    val maxLines = if (image == null) 3 else 2

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
                        painter = it,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(140.dp)
                            .clip(RoundedCornerShape(16.dp)),
                    )
                    if (showFeedback) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
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
                        chipStyle = style,
                        leadingEmoji = leadingEmoji,
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_clock),
                            contentDescription = "clock icon",
                        )
                        Spacer(Modifier.width(4.dp))
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
                // 사진이 없으면 기록 아래 20.dp 간격
                // 20 - 12(기본 vertical padding) = 8.dp
                if (image == null) {
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
        if (showFeedback) {
            // 사진이 없고 피드백이 있으면 기록과 피드백 사이에 20.dp 간격
            // 20 - 8(위의 기록 아래 간격) = 12.dp
            if (image == null) {
                Spacer(Modifier.height(12.dp))
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_feedback),
                    contentDescription = "feedback icon",
                )
                Spacer(Modifier.width(2.dp))
                Text(
                    text = stringResource(R.string.see_received_feedback),
                    style = TnTTheme.typography.label2Medium,
                    color = TnTTheme.colors.neutralColors.Neutral500,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun TnTSessionRecordCard(
    name: String,
    tagText: String,
    startTime: String,
    endTime: String,
    isTrainer: Boolean,
    leadingEmoji: String,
    defaultImage: Painter,
    modifier: Modifier = Modifier,
    profileImage: Painter? = null,
    showSessionRecordCreation: Boolean? = false,
    showSessionRecordDetails: Boolean? = false,
    onClick: () -> Unit,
) {
    val backgroundColor = if (isTrainer) {
        TnTTheme.colors.commonColors.Common0
    } else {
        TnTTheme.colors.neutralColors.Neutral100
    }
    val clockIcon = if (isTrainer) {
        R.drawable.ic_clock
    } else {
        R.drawable.ic_clock_dark
    }
    val image = profileImage ?: defaultImage

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp)
            .padding(top = 12.dp, bottom = 16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TnTChip(
                text = tagText,
                chipStyle = ChipStyle.BLUE,
                leadingEmoji = leadingEmoji,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(clockIcon),
                    contentDescription = "clock icon",
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = buildString {
                        append(startTime)
                        append(" ~ ")
                        append(endTime)
                    },
                    color = TnTTheme.colors.neutralColors.Neutral500,
                    style = TnTTheme.typography.label2Medium,
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = image,
                contentDescription = "clock icon",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape),
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = name,
                color = TnTTheme.colors.neutralColors.Neutral800,
                style = TnTTheme.typography.body1Bold,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp),
            )
        }
        // Trainer
        if (showSessionRecordCreation == true) {
            Spacer(Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onClick)
                    .background(TnTTheme.colors.neutralColors.Neutral100)
                    .padding(vertical = 12.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_edit_gray),
                    contentDescription = null,
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.make_pt_session_record),
                    style = TnTTheme.typography.label2Medium,
                    color = TnTTheme.colors.neutralColors.Neutral400,
                )
            }
        }
        // Trainee
        if (showSessionRecordDetails == true) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .clickable(onClick = onClick),
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_feedback),
                    contentDescription = "feedback icon",
                )
                Spacer(Modifier.width(2.dp))
                Text(
                    text = stringResource(R.string.see_pt_session_record),
                    color = TnTTheme.colors.neutralColors.Neutral500,
                    style = TnTTheme.typography.label2Medium,
                )
            }
        }
    }
}

@Composable
fun TnTMemberProfileCard(
    name: String,
    profileImage: Painter,
    purpose: String,
    memo: String,
    completedSessions: String,
    totalSessions: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(TnTTheme.colors.commonColors.Common0)
            .clickable(onClick = onClick)
            .padding(12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Image(
                painter = profileImage,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(16.dp)),
            )
            Spacer(Modifier.width(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 9.dp),
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = name,
                        color = TnTTheme.colors.neutralColors.Neutral900,
                        style = TnTTheme.typography.body1Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = purpose,
                        color = TnTTheme.colors.neutralColors.Neutral500,
                        style = TnTTheme.typography.label2Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Spacer(Modifier.width(4.dp))
                TnTProfileSessionChip(
                    completedSessions = completedSessions,
                    totalSessions = totalSessions,
                )
            }
        }
        if (memo.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = "메모",
                color = TnTTheme.colors.neutralColors.Neutral600,
                style = TnTTheme.typography.label2Bold,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = memo,
                color = TnTTheme.colors.neutralColors.Neutral500,
                style = TnTTheme.typography.label2Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview(widthDp = 358)
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

@Preview(widthDp = 358)
@Composable
private fun TnTRecordCardWithFeedbackPreview() {
    TnTTheme {
        TnTRecordCard(
            style = ChipStyle.BLUE,
            record = "닭가슴살 샐러드랑 고구마 먹었다 냐냠냐먀냐먀냠냠냠냠  냐냠냐먀냐먀냠냠냠냠먀냠냠냠냠  냐냠냐먀냐먀냠냠냠먀냠냠냠냠  냐냠냐먀냐먀냠냠냠",
            tagText = "상체 운동",
            time = "오전 7:30",
            modifier = Modifier.padding(10.dp),
            showFeedback = true,
        )
    }
}

@Preview(widthDp = 358)
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

@Preview(widthDp = 358)
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
            showFeedback = true,
        )
    }
}

@Preview(widthDp = 358)
@Composable
private fun TnTTrainerSessionRecordCardPreview() {
    TnTTheme {
        TnTSessionRecordCard(
            name = "김회원",
            tagText = "8회차 수업",
            startTime = "오후 17:00",
            endTime = "오후 18:00",
            defaultImage = painterResource(R.drawable.img_default),
            isTrainer = true,
            modifier = Modifier.padding(10.dp),
            leadingEmoji = "\uD83D\uDCAA",
            showSessionRecordCreation = true,
            onClick = { },
        )
    }
}

@Preview(widthDp = 358)
@Composable
private fun TnTTraineeSessionRecordCardPreview() {
    TnTTheme {
        TnTSessionRecordCard(
            name = "김민수 트레이너",
            tagText = "6회차 수업",
            startTime = "오후 17:00",
            endTime = "오후 18:00",
            defaultImage = painterResource(R.drawable.img_default),
            isTrainer = false,
            modifier = Modifier.padding(10.dp),
            leadingEmoji = "\uD83D\uDCAA",
            profileImage = painterResource(R.drawable.ic_edit),
            showSessionRecordDetails = true,
            onClick = { },
        )
    }
}

@Preview(widthDp = 358)
@Composable
private fun TnTMemberProfileCardPreview() {
    TnTTheme {
        TnTMemberProfileCard(
            name = "김회원",
            profileImage = painterResource(R.drawable.img_default),
            purpose = "체중 감량, 근력 향상, 자세 교정, 바디프로필",
            memo = "발목 안 좋고 식단 관리 원함. 하체가 약한 편임, 공복에 운동하면 쓰러질 위험 있음",
            completedSessions = "8",
            totalSessions = "60",
            onClick = { },
        )
    }
}
