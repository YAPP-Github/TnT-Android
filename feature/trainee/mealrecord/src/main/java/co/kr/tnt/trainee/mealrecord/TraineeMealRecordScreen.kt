package co.kr.tnt.trainee.mealrecord

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.designsystem.component.TnTTopBarWithBackButton
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.domain.IMAGE_MAX_SIZE
import co.kr.tnt.trainee.mealrecord.TraineeMealRecordContract.TraineeMealRecordUiEvent
import co.kr.tnt.trainee.mealrecord.TraineeMealRecordContract.TraineeMealRecordUiState
import co.kr.tnt.ui.coil.ResizeTransformation
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import java.time.LocalDate
import java.time.LocalTime
import co.kr.tnt.core.designsystem.R as uiResource

@Composable
internal fun TraineeMealRecordRoute(
    navigateToPrevious: () -> Unit,
    viewModel: TraineeMealRecordViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TraineeMealRecordScreen(
        state = uiState,
        context = context,
        onImageSelect = { uri ->
            viewModel.setEvent(TraineeMealRecordUiEvent.OnSelectImage(imageUri = uri))
        },
        onClickDeleteImage = { viewModel.setEvent(TraineeMealRecordUiEvent.OnClickDeleteImage) },
        onClickBack = navigateToPrevious,
    )

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                TraineeMealRecordContract.TraineeMealRecordSideEffect.NavigateToHome -> navigateToPrevious()
                is TraineeMealRecordContract.TraineeMealRecordSideEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@Composable
private fun TraineeMealRecordScreen(
    state: TraineeMealRecordUiState,
    context: Context,
    onImageSelect: (Uri) -> Unit,
    onClickDeleteImage: () -> Unit,
    onClickBack: () -> Unit,
) {
    val pickMediaLauncher = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            onImageSelect(uri)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(TnTTheme.colors.commonColors.Common0),
    ) {
        TnTTopBarWithBackButton(
            title = "식단 기록",
            onBackClick = onClickBack,
            showShadow = true,
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = TnTTheme.colors.neutralColors.Neutral200,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            MealImageSelector(
                imageUri = state.image,
                context = context,
                onImageSelect = {
                    pickMediaLauncher.launch(
                        PickVisualMediaRequest(
                            mediaType = PickVisualMedia.ImageOnly,
                        ),
                    )
                },
                onClickDeleteImage = onClickDeleteImage,
            )
        }
    }
}

@Composable
private fun MealImageSelector(
    imageUri: Uri?,
    context: Context,
    onImageSelect: () -> Unit,
    onClickDeleteImage: () -> Unit,
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(imageUri)
            .transformations(ResizeTransformation(IMAGE_MAX_SIZE))
            .build(),
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
            .height(350.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(TnTTheme.colors.neutralColors.Neutral100),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onImageSelect),
        ) {
            if (imageUri == null) {
                Image(
                    painter = painterResource(uiResource.drawable.ic_image),
                    contentDescription = null,
                )
                Text(
                    text = "오늘 먹은 식단을 추가해보세요",
                    color = TnTTheme.colors.neutralColors.Neutral400,
                    style = TnTTheme.typography.body2Medium,
                )
            } else {
                Box {
                    Image(
                        painter = imageUri.let { painter },
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                    )
                    IconButton(
                        onClick = onClickDeleteImage,
                        modifier = Modifier.align(Alignment.TopEnd),
                    ) {
                        Icon(
                            painter = painterResource(uiResource.drawable.ic_overlay_close),
                            contentDescription = null,
                            tint = Color.Unspecified,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TraineeMEalRecordScreenPreview() {
    TnTTheme {
        TraineeMealRecordScreen(
            state = TraineeMealRecordUiState(
                image = null,
                date = LocalDate.now(),
                time = LocalTime.now(),
                mealType = "breakfast",
                memo = "",
            ),
            context = LocalContext.current,
            onImageSelect = { },
            onClickDeleteImage = { },
            onClickBack = { },
        )
    }
}
