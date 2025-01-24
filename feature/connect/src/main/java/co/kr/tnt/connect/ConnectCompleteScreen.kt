package co.kr.tnt.connect

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.connect.model.TraineeProfile
import co.kr.tnt.connect.model.TrainerProfile
import co.kr.tnt.connect.model.UserProfile
import co.kr.tnt.designsystem.component.button.TnTBottomButton
import co.kr.tnt.designsystem.component.image.TnTProfileImage
import co.kr.tnt.designsystem.component.image.model.ProfileType
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.domain.model.UserType
import co.kr.tnt.feature.connect.R
import coil.compose.rememberAsyncImagePainter

@Composable
fun ConnectCompleteScreen(
    userType: UserType,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    BackHandler { onBackClick() }

    // TODO 전달 받기
    val trainerName = "김헬짱"
    val traineeName = "김회원"
    val trainerImage = null
    val traineeImage = "https://buly.kr/3j7VVqN"

    val trainerInfo = TrainerProfile(
        name = trainerName,
        image = trainerImage,
    )
    val traineeInfo = TraineeProfile(
        name = traineeName,
        image = traineeImage,
    )

    Scaffold { innerPadding ->
        Image(
            painter = painterResource(R.drawable.img_connection_complete_background_3x),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Column(Modifier.fillMaxSize()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(36.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = if (userType == UserType.Trainer) {
                            stringResource(R.string.connected_with_trainee, traineeName)
                        } else {
                            stringResource(R.string.connected_with_trainer, trainerName)
                        },
                        color = TnTTheme.colors.commonColors.Common0,
                        style = TnTTheme.typography.h1,
                        modifier = Modifier.padding(horizontal = 24.dp),
                        textAlign = TextAlign.Center,
                    )
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        val (leftProfile, rightProfile) = if (userType == UserType.Trainer) {
                            Pair(traineeInfo, trainerInfo)
                        } else {
                            Pair(trainerInfo, traineeInfo)
                        }
                        ProfileSection(
                            profile = leftProfile,
                            modifier = Modifier.padding(end = 16.dp),
                        )
                        ProfileSection(
                            profile = rightProfile,
                        )
                    }
                    Image(
                        painter = painterResource(R.drawable.img_boom_3x),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.width(320.dp),
                    )
                    Spacer(Modifier.weight(1f))
                }
            }
            TnTBottomButton(
                text = stringResource(R.string.next),
                onClick = onNextClick,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
private fun ProfileSection(
    profile: UserProfile,
    modifier: Modifier = Modifier,
) {
    val painter = profile.image?.let { rememberAsyncImagePainter(it) }
    val userType = if (profile is TrainerProfile) {
        ProfileType.Trainer
    } else {
        ProfileType.Trainee
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        TnTProfileImage(
            type = userType,
            image = painter,
            imageSize = 100.dp,
            showEditButton = false,
        )
        Text(
            text = profile.name,
            color = TnTTheme.colors.neutralColors.Neutral300,
            style = TnTTheme.typography.body2Medium,
            modifier = Modifier.padding(horizontal = 24.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ConnectCompleteScreenPreview() {
    TnTTheme {
        ConnectCompleteScreen(
            userType = UserType.Trainer,
            onNextClick = {},
            onBackClick = {},
        )
    }
}
