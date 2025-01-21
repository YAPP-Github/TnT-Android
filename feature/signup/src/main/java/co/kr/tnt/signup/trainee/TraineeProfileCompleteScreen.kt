package co.kr.tnt.signup.trainee

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.component.button.TnTBottomButton
import co.kr.tnt.designsystem.theme.TnTTheme

@Composable
fun TraineeProfileCompleteScreen(
    modifier: Modifier = Modifier,
) {
    // TODO 이름 불러오기
    val name = "김헬짱"

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TnTTheme.colors.commonColors.Common0),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 66.dp),
        ) {
            Text(
                text = stringResource(R.string.signup_complete_trainee_title, name),
                color = TnTTheme.colors.neutralColors.Neutral950,
                style = TnTTheme.typography.h1,
                textAlign = Center,
                modifier = Modifier.padding(horizontal = 24.dp),
            )
            Spacer(Modifier.padding(top = 10.dp))
            Text(
                text = stringResource(R.string.signup_complete_trainee_subtitle),
                color = TnTTheme.colors.neutralColors.Neutral500,
                style = TnTTheme.typography.body1Medium,
                textAlign = Center,
            )
            Spacer(Modifier.padding(top = 28.dp))
            // TODO 프로필 이미지 가져오기
            Image(
                painter = painterResource(R.drawable.img_default_profile_trainee),
                contentDescription = null,
                modifier = Modifier.size(200.dp),
            )
        }
        // TODO 연결코드 입력 화면으로 이동
        TnTBottomButton(
            text = stringResource(R.string.start),
            onClick = { },
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TraineeProfileCompleteScreenPreview() {
    TnTTheme {
        TraineeProfileCompleteScreen()
    }
}
