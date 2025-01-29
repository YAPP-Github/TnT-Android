package co.kr.tnt.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.theme.TnTTheme

@Composable
fun TnTCheckToggle(
    isChecked: Boolean = false,
    onCheckClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val icon = if (isChecked) {
        R.drawable.ic_check_true
    } else {
        R.drawable.ic_check_false
    }

    Image(
        painter = painterResource(icon),
        contentDescription = null,
        modifier = modifier
            .size(24.dp)
            .clip(CircleShape)
            .clickable(onClick = onCheckClick),
    )
}

@Preview
@Composable
private fun TnTCheckTogglePreview() {
    TnTTheme {
        var isChecked by remember { mutableStateOf(false) }

        TnTCheckToggle(
            isChecked = isChecked,
            onCheckClick = { isChecked = !isChecked },
        )
    }
}
