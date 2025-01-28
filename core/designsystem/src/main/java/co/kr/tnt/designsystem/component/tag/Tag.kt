package co.kr.tnt.designsystem.component.tag

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.designsystem.component.tag.model.TagType
import co.kr.tnt.designsystem.theme.TnTTheme

@Composable
fun TnTTag(
    text: String,
    type: TagType,
    modifier: Modifier = Modifier,
    leadingEmoji: String? = null,
) {
    val backgroundColor: Color
    val textColor: Color

    when (type) {
        TagType.EXERCISE -> {
            backgroundColor = TnTTheme.colors.blueColors.Blue100
            textColor = TnTTheme.colors.blueColors.Blue800
        }

        TagType.DIET -> {
            backgroundColor = TnTTheme.colors.pinkColors.Pink100
            textColor = TnTTheme.colors.pinkColors.Pink800
        }
    }

    Row(
        modifier = modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .height(28.dp)
            .padding(horizontal = 8.dp, vertical = 6.dp),
    ) {
        leadingEmoji?.let {
            Text(
                text = it,
                style = TnTTheme.typography.label1Bold,
            )
            Spacer(modifier = Modifier.width(2.dp))
        }
        Text(
            text = text,
            color = textColor,
            style = TnTTheme.typography.label1Bold,
        )
    }
}

@Preview(showBackground = true, widthDp = 100, heightDp = 40)
@Composable
private fun TnTExerciseTagPreview() {
    TnTTheme {
        TnTTag(
            text = "8회차 수업",
            type = TagType.EXERCISE,
            leadingEmoji = "💪",
        )
    }
}

@Preview(showBackground = true, widthDp = 100, heightDp = 40)
@Composable
private fun TnTTagWithoutEmojiPreview() {
    TnTTheme {
        TnTTag(
            text = "등 운동",
            type = TagType.EXERCISE,
        )
    }
}

@Preview(showBackground = true, widthDp = 100, heightDp = 40)
@Composable
private fun TnTDietTagPreview() {
    TnTTheme {
        TnTTag(
            text = "아침",
            type = TagType.DIET,
            leadingEmoji = "\uD83C\uDF1E",
        )
    }
}
