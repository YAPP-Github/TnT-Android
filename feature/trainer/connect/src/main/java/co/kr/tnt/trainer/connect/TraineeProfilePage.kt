package co.kr.tnt.trainer.connect

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import co.kr.tnt.domain.UserProfilePolicy
import co.kr.tnt.domain.model.User
import co.kr.tnt.feature.trainer.connect.R
import co.kr.tnt.trainer.connect.TrainerConnectContract.TrainerConnectUiState
import co.kr.tnt.ui.coil.ResizeTransformation
import co.kr.tnt.ui.model.DefaultUserProfile
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import co.kr.tnt.core.ui.R as uiResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun TraineeProfilePage(
    state: TrainerConnectUiState,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    BackHandler { onBackClick() }

    val context = LocalContext.current
    val trainee = state.traineeState

    Scaffold { innerPadding ->
        Image(
            painter = painterResource(uiResource.drawable.img_connection_complete_background_3x),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 66.dp)
                    .padding(horizontal = 40.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                Spacer(Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.trainee_who_will_be_with),
                    style = TnTTheme.typography.h4,
                    color = TnTTheme.colors.commonColors.Common0,
                )
                Spacer(Modifier.height(12.dp))
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .clip(RoundedCornerShape(20.dp))
                        .background(TnTTheme.colors.commonColors.Common0)
                        .padding(horizontal = 20.dp, vertical = 32.dp),
                ) {
                    val painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(context)
                            .data(trainee.image)
                            .placeholder(DefaultUserProfile.Trainee.image)
                            .error(DefaultUserProfile.Trainee.image)
                            .transformations(ResizeTransformation(UserProfilePolicy.USER_IMAGE_MAX_SIZE))
                            .build(),
                    )
                    val defaultImage = painterResource(DefaultUserProfile.Trainee.image)
                    TnTProfileImage(
                        defaultImage = defaultImage,
                        image = painter,
                        imageSize = 128.dp,
                        showEditButton = false,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = trainee.name,
                        style = TnTTheme.typography.h2,
                        color = TnTTheme.colors.neutralColors.Neutral950,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(32.dp))
                    val traineeInfo = listOfNotNull(
                        trainee.age?.let {
                            stringResource(uiResource.string.age_label) to
                                it.toString() + stringResource(uiResource.string.age_unit)
                        },
                        trainee.height?.let {
                            stringResource(uiResource.string.height_label) to
                                it.toString() + stringResource(uiResource.string.height_unit)
                        },
                        trainee.weight?.let {
                            stringResource(uiResource.string.weight_label) to
                                it.toString()
                                    .removeSuffix(".0") + stringResource(uiResource.string.weight_unit)
                        },
                    )

                    if (traineeInfo.isNotEmpty()) {
                        FlowRow(
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            traineeInfo.forEachIndexed { index, (label, text) ->
                                TextWithLabel(label = label, text = text)
                                Spacer(Modifier.weight(1f))
                            }
                        }
                        Spacer(Modifier.height(32.dp))
                    }
                    TextWithBackground(
                        label = stringResource(R.string.purpose_of_pt),
                        text = trainee.ptPurpose?.joinToString(", ") ?: "",
                    )
                    Spacer(Modifier.height(32.dp))
                    TextWithBackground(
                        label = stringResource(R.string.caution),
                        text = trainee.caution ?: "",
                        modifier = Modifier.height(128.dp),
                    )
                }
                Spacer(Modifier.height(24.dp))
            }
            TnTBottomButton(
                text = stringResource(uiResource.string.start),
                onClick = onNextClick,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
private fun TextWithLabel(
    label: String,
    text: String,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = label,
            style = TnTTheme.typography.body1Bold,
            color = TnTTheme.colors.neutralColors.Neutral950,
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = text,
            style = TnTTheme.typography.body2Medium,
            color = TnTTheme.colors.neutralColors.Neutral500,
        )
    }
}

@Composable
private fun TextWithBackground(
    label: String,
    text: String,
    modifier: Modifier = Modifier,
) {
    Column {
        Text(
            text = label,
            style = TnTTheme.typography.body1Bold,
            color = TnTTheme.colors.neutralColors.Neutral900,
        )
        Spacer(Modifier.height(7.dp))
        Box(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(TnTTheme.colors.neutralColors.Neutral100)
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Text(
                text = text.ifEmpty { "미입력" },
                style = TnTTheme.typography.label1Medium,
                color =
                    if (text.isEmpty()) {
                        TnTTheme.colors.neutralColors.Neutral400
                    } else {
                        TnTTheme.colors.neutralColors.Neutral800
                    },
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun TraineeProfilePagePreview() {
    TnTTheme {
        TraineeProfilePage(
            state = TrainerConnectUiState(
                traineeState = User.Trainee(
                    id = "",
                    name = "김회원",
                    image = null,
                    birthday = null,
                    age = 25,
                    weight = 70.0,
                    height = 170,
                    ptPurpose = listOf("체중 감량", "자세 교정"),
                    caution = "손목이 안 좋습니다.",
                    isConnected = true,
                ),
            ),
            onNextClick = {},
            onBackClick = {},
        )
    }
}
