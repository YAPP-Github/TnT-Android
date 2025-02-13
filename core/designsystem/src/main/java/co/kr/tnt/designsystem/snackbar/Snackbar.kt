package co.kr.tnt.designsystem.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.theme.TnTTheme

private val toastBottomPadding = 90.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TnTSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .imePadding()
            .systemBarsPadding()
            .padding(
                start = 20.dp,
                end = 20.dp,
                bottom = if (WindowInsets.isImeVisible) 24.dp else toastBottomPadding,
            )
            .clip(RoundedCornerShape(8.dp))
            .background(
                color = TnTTheme.colors.neutralColors.Neutral900.copy(alpha = 0.8f),
                shape = RoundedCornerShape(size = 16.dp),
            )
            .padding(
                vertical = 16.dp,
                horizontal = 20.dp,
            ),
    ) {
        snackbarData.icon?.let { icon ->
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = TnTTheme.colors.neutralColors.Neutral50,
                modifier = Modifier.height(24.dp),
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = snackbarData.message,
            style = TnTTheme.typography.label1Medium,
            color = TnTTheme.colors.neutralColors.Neutral50,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TnTToastPreview() {
    TnTTheme {
        TnTSnackbar(
            snackbarData = object : SnackbarData {
                override val message: String = "코드가 복사되었어요!"
                override val icon: Int = R.drawable.ic_warning
                override val duration: Long = 500L
                override fun dismiss() = Unit
            },
        )
    }
}
