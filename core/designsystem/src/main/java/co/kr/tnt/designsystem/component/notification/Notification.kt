package co.kr.tnt.designsystem.component.notification

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.designsystem.component.notification.model.NotificationIcon
import co.kr.tnt.designsystem.theme.TnTTheme

@Composable
fun TnTNotification(
    type: NotificationIcon,
    title: String,
    contents: String,
    time: String,
    isChecked: Boolean,
) {
    val backgroundColor = if (isChecked) {
        TnTTheme.colors.commonColors.Common0
    } else {
        TnTTheme.colors.neutralColors.Neutral100
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(20.dp),
    ) {
        Image(
            painter = painterResource(type.icon),
            contentDescription = null,
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
            ) {
                Text(
                    text = title,
                    color = TnTTheme.colors.neutralColors.Neutral400,
                    style = TnTTheme.typography.label1Bold,
                )
                Text(
                    text = time,
                    color = TnTTheme.colors.neutralColors.Neutral400,
                    style = TnTTheme.typography.label1Medium,
                )
            }
            Text(
                text = contents,
                color = TnTTheme.colors.neutralColors.Neutral900,
                style = TnTTheme.typography.body2Medium,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview
@Composable
private fun TnTNotificationPreview() {
    TnTTheme {
        TnTNotification(
            type = NotificationIcon.LINK,
            title = "알림 문구",
            contents = "알림 상세 문구",
            time = "2분전",
            isChecked = false,
        )
    }
}
