package co.kr.tnt.signup.common.component

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.theme.TnTTheme

@Composable
fun ProfileImageSection(
    defaultImage: Int,
    modifier: Modifier = Modifier,
    @Suppress("UnusedParameter")
    onImageSelected: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize()
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        // TODO 프로필 이미지 가져오기
        Image(
            painter = painterResource(defaultImage),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(131.dp)
                .clip(CircleShape),
        )
        // TODO 버튼 클릭 시 권한 확인 후 사진 선택
        IconButton(
            onClick = { },
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

@Preview(showBackground = true)
@Composable
private fun ProfileImageSectionPreview() {
    TnTTheme {
        ProfileImageSection(
            defaultImage = R.drawable.img_default_profile_trainer,
            modifier = Modifier.fillMaxWidth(),
            onImageSelected = {},
        )
    }
}
