package co.kr.tnt.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.kr.tnt.designsystem.theme.TnTTheme

@Composable
fun TnTCountTopBar(
    title: String,
    count: Int,
    modifier: Modifier = Modifier,
    trailingComponent: (@Composable () -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 20.dp) // Text 를 기준으로 상하 패딩 부여
                .weight(1f),
        ) {
            Text(
                text = title,
                color = TnTTheme.colors.neutralColors.Neutral900,
                style = TnTTheme.typography.h2,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.weight(
                    weight = 1f,
                    fill = false,
                ),
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = count.toString(),
                color = TnTTheme.colors.redColors.Red500,
                style = TnTTheme.typography.h2,
            )
        }
        trailingComponent?.let { component ->
            Spacer(Modifier.width(16.dp))
            component()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TnTCountTopBarPreview() {
    TnTTheme {
        TnTCountTopBar(
            title = "연결된 회원 수를 확인해보세요",
            count = 1,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TnTCountTopBarWithComponentPreview() {
    TnTTheme {
        TnTCountTopBar(
            title = "연결된 회원 수를 확인해보세요",
            count = 1,
        ) {
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                    containerColor = TnTTheme.colors.neutralColors.Neutral200,
                    contentColor = TnTTheme.colors.neutralColors.Neutral50,
                    disabledContainerColor = TnTTheme.colors.neutralColors.Neutral200,
                    disabledContentColor = TnTTheme.colors.neutralColors.Neutral50,
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 7.dp),
                modifier = Modifier.defaultMinSize(
                    minWidth = Dp.Hairline,
                    minHeight = 32.dp,
                ),
            ) {
                Text(
                    text = "회원 초대하기",
                    color = TnTTheme.colors.neutralColors.Neutral600,
                    style = TnTTheme.typography.label2Medium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            }
        }
    }
}
