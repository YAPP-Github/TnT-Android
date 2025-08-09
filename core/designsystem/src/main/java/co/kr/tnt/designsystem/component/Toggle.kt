package co.kr.tnt.designsystem.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.theme.TnTTheme

@Composable
fun TnTCheckToggle(
    isChecked: Boolean = false,
    onClickCheck: () -> Unit,
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
            .clickable(onClick = onClickCheck),
    )
}

@Composable
fun TnTSwitch(
    checked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val trackColor = if (checked) {
        TnTTheme.colors.redColors.Red500
    } else {
        TnTTheme.colors.neutralColors.Neutral300
    }

    val thumbPosition by animateFloatAsState(
        targetValue = if (checked) 20f else 0f,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "thumbPosition",
    )

    Box(
        modifier = modifier
            .width(44.dp)
            .height(24.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(trackColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 2.dp),
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .offset(x = thumbPosition.dp)
                .align(Alignment.CenterStart)
                .background(Color.White, CircleShape),
        )
    }
}

@Preview
@Composable
private fun TnTCheckTogglePreview() {
    TnTTheme {
        var isChecked by remember { mutableStateOf(false) }

        TnTCheckToggle(
            isChecked = isChecked,
            onClickCheck = { isChecked = !isChecked },
        )
    }
}

@Preview
@Composable
private fun TnTSwitchPreview() {
    TnTTheme {
        var checked by remember { mutableStateOf(true) }

        TnTSwitch(
            checked = checked,
            onClick = { checked = !checked },
        )
    }
}
