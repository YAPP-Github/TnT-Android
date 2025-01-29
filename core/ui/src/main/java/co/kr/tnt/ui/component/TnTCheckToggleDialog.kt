package co.kr.tnt.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import co.kr.tnt.designsystem.component.TnTCheckToggle
import co.kr.tnt.designsystem.component.button.TnTTextButton
import co.kr.tnt.designsystem.component.button.model.ButtonSize
import co.kr.tnt.designsystem.component.button.model.ButtonType
import co.kr.tnt.designsystem.theme.TnTTheme

@Composable
fun TnTCheckToggleDialog(
    title: String,
    content: String,
    isChecked: Boolean,
    checkToggleText: String,
    leftButtonText: String,
    rightButtonText: String,
    modifier: Modifier = Modifier,
    onLeftButtonClick: () -> Unit,
    onRightButtonClick: () -> Unit,
    onCheckClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = cardColors().copy(TnTTheme.colors.commonColors.Common0),
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 20.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(20.dp),
            ) {
                Text(
                    text = title,
                    style = TnTTheme.typography.h3.copy(
                        textAlign = TextAlign.Center,
                    ),
                    color = TnTTheme.colors.neutralColors.Neutral900,
                    modifier = Modifier.padding(top = 20.dp),
                )
                Text(
                    text = content,
                    style = TnTTheme.typography.body2Medium.copy(
                        textAlign = TextAlign.Center,
                    ),
                    color = TnTTheme.colors.neutralColors.Neutral500,
                    modifier = Modifier.padding(top = 10.dp),
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp, vertical = 20.dp),
                ) {
                    TnTCheckToggle(
                        isChecked = isChecked,
                        onCheckClick = onCheckClick,
                    )
                    Text(
                        text = checkToggleText,
                        style = TnTTheme.typography.body2Medium,
                        color = TnTTheme.colors.neutralColors.Neutral500,
                        modifier = Modifier.padding(start = 4.dp),
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    TnTTextButton(
                        size = ButtonSize.Medium,
                        type = ButtonType.Gray,
                        text = leftButtonText,
                        onClick = onLeftButtonClick,
                        modifier = Modifier.weight(1f),
                    )
                    TnTTextButton(
                        size = ButtonSize.Medium,
                        type = ButtonType.Primary,
                        text = rightButtonText,
                        onClick = onRightButtonClick,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TnTCheckToggleDialogPreview() {
    TnTTheme {
        var isChecked by remember { mutableStateOf(false) }

        TnTCheckToggleDialog(
            title = "트레이너를 연결해 주세요",
            content = "연결하지 않을 경우 일부 기능이 제한돼요\n초대 코드를 입력해 연결해주시겠어요?",
            checkToggleText = "3일 동안 보지 않기",
            isChecked = isChecked,
            onLeftButtonClick = {},
            onRightButtonClick = {},
            onCheckClick = { isChecked = !isChecked },
            leftButtonText = "다음에",
            rightButtonText = "연결하기",
            onDismiss = {},
        )
    }
}
