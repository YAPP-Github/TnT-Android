package co.kr.tnt.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.kr.tnt.designsystem.theme.TnTTheme

@Composable
fun TnTMyPageButton(
    text: String,
    height: Dp,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonColors(
            containerColor = TnTTheme.colors.commonColors.Common0,
            contentColor = TnTTheme.colors.neutralColors.Neutral700,
            disabledContainerColor = TnTTheme.colors.commonColors.Common0,
            disabledContentColor = TnTTheme.colors.neutralColors.Neutral700,
        ),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .defaultMinSize(minWidth = Dp.Hairline),
    ) {
        Text(
            text = text,
            style = TnTTheme.typography.body2Medium,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun TnTMyPageButtonPreview() {
    TnTTheme {
        TnTMyPageButton(
            text = "트레이너와 연결하기",
            height = 47.dp,
            onClick = {},
        )
    }
}
