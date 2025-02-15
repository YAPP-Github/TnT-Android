package co.kr.tnt.trainee.connect

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.designsystem.component.TnTProfileImage
import co.kr.tnt.designsystem.component.button.TnTBottomButton
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.domain.IMAGE_MAX_SIZE
import co.kr.tnt.domain.model.UserType
import co.kr.tnt.feature.trainee.connect.R
import co.kr.tnt.ui.coil.ResizeTransformation
import co.kr.tnt.ui.model.DefaultUserProfile
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import co.kr.tnt.core.ui.R as uiResource

@Composable
internal fun TraineeConnectCompletePage(
    trainerName: String,
    trainerImage: String,
    traineeName: String,
    traineeImage: String,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    BackHandler { onBackClick() }

    val context = LocalContext.current

    Scaffold { innerPadding ->
        Image(
            painter = painterResource(uiResource.drawable.img_connection_complete_background_3x),
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
                        text = stringResource(
                            R.string.connected_with_trainer,
                            trainerName,
                        ),
                        color = TnTTheme.colors.commonColors.Common0,
                        style = TnTTheme.typography.h1,
                        modifier = Modifier.padding(horizontal = 24.dp),
                        textAlign = TextAlign.Center,
                    )
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        ProfileSection(
                            type = UserType.TRAINER,
                            name = trainerName,
                            image = trainerImage,
                            context = context,
                            modifier = Modifier
                                .padding(end = 16.dp),
                        )
                        ProfileSection(
                            type = UserType.TRAINEE,
                            name = traineeName,
                            image = traineeImage,
                            context = context,
                            modifier = Modifier.width(100.dp),
                        )
                    }
                    Image(
                        painter = painterResource(uiResource.drawable.img_boom_3x),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.width(320.dp),
                    )
                    Spacer(Modifier.weight(1f))
                }
            }
            TnTBottomButton(
                text = stringResource(uiResource.string.next),
                onClick = onNextClick,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
private fun ProfileSection(
    type: UserType,
    name: String,
    image: String,
    context: Context,
    modifier: Modifier = Modifier,
) {
    val defaultImage = DefaultUserProfile.fromDomain(type).image
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(image)
            .placeholder(defaultImage)
            .error(DefaultUserProfile.Trainer.image)
            .transformations(ResizeTransformation(IMAGE_MAX_SIZE))
            .build(),
    )
    Column(
        modifier = modifier.width(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        TnTProfileImage(
            defaultImage = painterResource(defaultImage),
            image = painter,
            imageSize = 100.dp,
            showEditButton = false,
        )
        Text(
            text = name,
            color = TnTTheme.colors.neutralColors.Neutral300,
            style = TnTTheme.typography.body2Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TraineeConnectCompletePagePreview() {
    TnTTheme {
        TraineeConnectCompletePage(
            trainerName = "김헬스",
            trainerImage = "",
            traineeName = "김회원",
            traineeImage = "",
            onNextClick = {},
            onBackClick = {},
        )
    }
}
