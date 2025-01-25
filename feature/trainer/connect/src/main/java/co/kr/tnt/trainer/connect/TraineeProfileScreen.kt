package co.kr.tnt.connect.trainer

import TrainerConnectContract.TrainerConnectUiState
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.designsystem.component.button.TnTBottomButton
import co.kr.tnt.designsystem.component.image.TnTProfileImage
import co.kr.tnt.designsystem.component.image.model.ProfileType
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.feature.connect.R
import coil.compose.rememberAsyncImagePainter

@Composable
internal fun TraineeProfileScreen(
    state: TrainerConnectUiState,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    BackHandler { onBackClick() }

    val trainee = state.traineeState

    Scaffold { innerPadding ->
        Image(
            painter = painterResource(R.drawable.img_connection_complete_background_3x),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 24.dp),
            ) {
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
                        .padding(horizontal = 20.dp, vertical = 32.dp)
                        .width(260.dp),
                ) {
                    val painter = trainee.image?.let { rememberAsyncImagePainter(it) }
                    TnTProfileImage(
                        type = ProfileType.Trainee,
                        image = painter,
                        imageSize = 128.dp,
                        showEditButton = false,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = trainee.name,
                            style = TnTTheme.typography.h2,
                            color = TnTTheme.colors.neutralColors.Neutral950,
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.trainee),
                            style = TnTTheme.typography.h4,
                            color = TnTTheme.colors.neutralColors.Neutral950,
                        )
                    }
                    Spacer(Modifier.height(32.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        TextWithLabel(
                            label = stringResource(R.string.age_label),
                            text = trainee.age.toString() + stringResource(R.string.age_unit),
                            modifier = Modifier.weight(1f),
                        )
                        TextWithLabel(
                            label = stringResource(R.string.height_label),
                            text = trainee.height.toString() + stringResource(R.string.height_unit),
                            modifier = Modifier.weight(1f),
                        )
                        TextWithLabel(
                            label = stringResource(R.string.weight_label),
                            text = trainee.weight.toString() + stringResource(R.string.weight_unit),
                            modifier = Modifier.weight(1f),
                        )
                    }
                    Spacer(Modifier.height(32.dp))
                    TextWithBackground(
                        label = stringResource(R.string.purpose_of_pt),
                        text = trainee.ptPurpose,
                    )
                    Spacer(Modifier.height(32.dp))
                    TextWithBackground(
                        label = stringResource(R.string.caution),
                        text = trainee.caution,
                        modifier = Modifier.height(128.dp),
                    )
                }
            }
            TnTBottomButton(
                text = stringResource(R.string.start),
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
    modifier: Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Text(
            text = label,
            style = TnTTheme.typography.body1Bold,
            color = TnTTheme.colors.neutralColors.Neutral950,
        )
        Spacer(Modifier.width(12.dp))
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
                text = text,
                style = TnTTheme.typography.label1Medium,
                color = TnTTheme.colors.neutralColors.Neutral800,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TraineeProfileScreenPreview() {
    TnTTheme {
        TraineeProfileScreen(
            state = TODO(),
            onNextClick = {},
            onBackClick = {},
        )
    }
}
