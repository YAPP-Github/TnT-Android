package co.kr.tnt.designsystem.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.designsystem.theme.TnTTheme

@Composable
fun TnTDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier.padding(vertical = 8.dp),
        thickness = 2.dp,
        color = TnTTheme.colors.neutralColors.Neutral100,
    )
}

@Preview(showBackground = true)
@Composable
private fun TnTDividerPreview() {
    TnTTheme {
        TnTDivider()
    }
}
