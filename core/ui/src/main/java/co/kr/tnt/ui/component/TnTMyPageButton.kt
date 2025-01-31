package co.kr.tnt.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.kr.tnt.designsystem.theme.TnTTheme

@Composable
fun TnTMyPageButton(
    text: String,
    verticalPadding: Dp,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 39.dp) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonColors(
                containerColor = TnTTheme.colors.commonColors.Common0,
                contentColor = TnTTheme.colors.neutralColors.Neutral700,
                disabledContainerColor = TnTTheme.colors.commonColors.Common0,
                disabledContentColor = TnTTheme.colors.neutralColors.Neutral700,
            ),
            elevation = null,
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = verticalPadding),
            modifier = modifier
                .fillMaxWidth()
                .defaultMinSize(Dp.Hairline),
        ) {
            Text(
                text = text,
                style = TnTTheme.typography.body2Medium,
                modifier = Modifier.fillMaxWidth(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
    }
}

@Preview
@Preview(fontScale = 1.5f)
@Composable
private fun TnTMyPageButtonPreview() {
    TnTTheme {
        TnTMyPageButton(
            text = "트레이너와 연결하기",
            onClick = {},
            verticalPadding = 8.dp,
        )
    }
}
