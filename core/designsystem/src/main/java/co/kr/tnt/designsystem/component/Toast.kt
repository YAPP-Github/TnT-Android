package co.kr.tnt.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.theme.TnTTheme
import kotlinx.coroutines.delay

@Composable
fun TnTToast(
    message: String,
    modifier: Modifier = Modifier,
    resourceIcon: Int? = R.drawable.ic_warning,
    duration: Long = 3000L,
    onDismiss: () -> Unit,
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
        delay(duration)
        visible = false
        delay(300)
        onDismiss()
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = TnTTheme.colors.neutralColors.Neutral900.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(size = 16.dp),
                )
                .padding(vertical = 16.dp, horizontal = 20.dp),
        ) {
            if (resourceIcon != null) {
                Icon(
                    painter = painterResource(resourceIcon),
                    contentDescription = null,
                    tint = TnTTheme.colors.neutralColors.Neutral50,
                    modifier = Modifier.height(24.dp),
                )
            }
            Text(
                text = message,
                style = TnTTheme.typography.label1Medium,
                color = TnTTheme.colors.neutralColors.Neutral50,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TnTToastPreview() {
    TnTTheme {
        TnTToast(
            message = "코드가 복사되었어요!",
            onDismiss = { },
        )
    }
}
