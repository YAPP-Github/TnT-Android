package co.kr.tnt.designsystem.component.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.component.image.model.ProfileType
import co.kr.tnt.designsystem.theme.TnTTheme
import coil.compose.rememberAsyncImagePainter

@Composable
fun TnTProfileImage(
    type: ProfileType,
    modifier: Modifier = Modifier,
    image: Painter? = null,
    imageSize: Dp = 132.dp,
    showEditButton: Boolean = true,
    onEditClick: () -> Unit = {},
) {
    val defaultImage = painterResource(type.defaultImage)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize()
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = image ?: defaultImage,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(imageSize)
                .clip(CircleShape),
        )
        // TODO 버튼 클릭 시 권한 확인 후 사진 선택
        if (showEditButton) {
            IconButton(
                onClick = { onEditClick() },
                modifier = Modifier
                    .size(28.dp)
                    .align(Alignment.BottomEnd),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = null,
                    tint = Color.Unspecified,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TnTProfileImageDefaultImagePreview() {
    TnTTheme {
        TnTProfileImage(
            type = ProfileType.Trainer,
            modifier = Modifier.fillMaxWidth(),
            onEditClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TnTProfileImageCustomImagePreview() {
    TnTTheme {
        TnTProfileImage(
            type = ProfileType.Trainer,
            image = rememberAsyncImagePainter("https://buly.kr/7FQeS5M"),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
