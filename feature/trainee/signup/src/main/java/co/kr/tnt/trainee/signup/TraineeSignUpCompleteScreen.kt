package co.kr.tnt.trainee.signup

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.designsystem.component.button.TnTBottomButton
import co.kr.tnt.designsystem.component.image.TnTProfileImage
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.feature.trainee.signup.R
import co.kr.tnt.ui.model.DefaultUserProfile
import coil.compose.rememberAsyncImagePainter

@Composable
fun TraineeSignUpCompleteScreen(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    BackHandler { onBackClick() }

    // TODO 이름, 프로필 사진 불러오기
    val name = "김헬짱"
    val profileImage = "https://buly.kr/7FQeS5M"

    Scaffold(
        containerColor = TnTTheme.colors.commonColors.Common0,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
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
                    text = stringResource(R.string.nice_to_meet_you_trainee, name),
                    color = TnTTheme.colors.neutralColors.Neutral950,
                    style = TnTTheme.typography.h1,
                    textAlign = Center,
                    modifier = Modifier.padding(horizontal = 24.dp),
                )
                Spacer(Modifier.padding(top = 10.dp))
                Text(
                    text = stringResource(R.string.chemistry_boom_with_trainer),
                    color = TnTTheme.colors.neutralColors.Neutral500,
                    style = TnTTheme.typography.body1Medium,
                    textAlign = Center,
                )
                Spacer(Modifier.padding(top = 28.dp))
                TnTProfileImage(
                    defaultImage = painterResource(DefaultUserProfile.Trainee.image),
                    image = rememberAsyncImagePainter(profileImage),
                    imageSize = 200.dp,
                    showEditButton = false,
                )
            }
            TnTBottomButton(
                text = stringResource(R.string.start),
                onClick = { onNextClick() },
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TraineeSignUpCompleteScreenPreview() {
    TnTTheme {
        TraineeSignUpCompleteScreen(
            onBackClick = {},
            onNextClick = {},
        )
    }
}
