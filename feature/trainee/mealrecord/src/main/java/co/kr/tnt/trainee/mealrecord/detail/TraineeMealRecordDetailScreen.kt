package co.kr.tnt.trainee.mealrecord.detail

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.component.TnTDivider
import co.kr.tnt.designsystem.component.TnTTopBarWithBackButton
import co.kr.tnt.designsystem.component.chip.TnTChip
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.domain.IMAGE_MAX_SIZE
import co.kr.tnt.domain.model.RecordType.MealType
import co.kr.tnt.domain.utils.DateFormatter
import co.kr.tnt.trainee.mealrecord.detail.TraineeMealRecordDetailContract.TraineeMealRecordDetailSideEffect
import co.kr.tnt.trainee.mealrecord.detail.TraineeMealRecordDetailContract.TraineeMealRecordDetailUiEvent
import co.kr.tnt.trainee.mealrecord.detail.TraineeMealRecordDetailContract.TraineeMealRecordDetailUiState
import co.kr.tnt.ui.coil.ResizeTransformation
import co.kr.tnt.ui.model.RecordChip
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
internal fun TraineeMealRecordDetailRoute(
    mealId: Long,
    navigateToPrevious: () -> Unit,
    viewModel: TraineeMealRecordDetailViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val dateFormatter = remember { DateFormatter() }

    TraineeMealRecordDetailScreen(
        state = state,
        context = context,
        dateFormatter = dateFormatter,
        onClickMore = { viewModel.setEvent(TraineeMealRecordDetailUiEvent.OnClickMore) },
        onClickBack = navigateToPrevious,
    )

    LaunchedEffect(mealId) {
        viewModel.setEvent(TraineeMealRecordDetailUiEvent.LoadRecordDetail(mealId))
    }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                TraineeMealRecordDetailSideEffect.NavigateToHome -> navigateToPrevious()
                is TraineeMealRecordDetailSideEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@Composable
private fun TraineeMealRecordDetailScreen(
    state: TraineeMealRecordDetailUiState,
    context: Context,
    dateFormatter: DateFormatter,
    onClickMore: () -> Unit,
    onClickBack: () -> Unit,
) {
    val chip = RecordChip.create(state.mealType)
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(state.image)
            .placeholder(R.drawable.img_default)
            .transformations(ResizeTransformation(IMAGE_MAX_SIZE))
            .build(),
    )

    Scaffold(
        topBar = {
            TnTTopBarWithBackButton(
                title = dateFormatter.format(state.date.toLocalDate(), "M월 d일"),
                onBackClick = onClickBack,
                showStoke = true,
                trailingComponent = {
                    Icon(
                        painter = painterResource(R.drawable.ic_more),
                        contentDescription = null,
                        modifier = Modifier.clickable(onClick = onClickMore),
                    )
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(TnTTheme.colors.commonColors.Common0)
                .verticalScroll(rememberScrollState()),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            ) {
                if (state.image.isNullOrEmpty().not()) {
                    Image(
                        painter = painter,
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .height(350.dp)
                            .padding(vertical = 20.dp)
                            .clip(RoundedCornerShape(20.dp)),
                    )
                    Spacer(Modifier.height(8.dp))
                } else {
                    Spacer(Modifier.height(32.dp))
                }
                TnTChip(
                    text = chip.title,
                    chipStyle = chip.chipStyle,
                    leadingEmoji = chip.emoji,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                ) {
                    Text(
                        text = dateFormatter.format(state.date, "yyyy/MM/dd"),
                        color = TnTTheme.colors.neutralColors.Neutral600,
                        style = TnTTheme.typography.body2Medium,
                    )
                    Text(
                        text = dateFormatter.format(state.date, "a hh:mm"),
                        color = TnTTheme.colors.neutralColors.Neutral600,
                        style = TnTTheme.typography.body2Medium,
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    TnTDivider()
                    Text(
                        text = state.memo,
                        color = TnTTheme.colors.neutralColors.Neutral800,
                        style = TnTTheme.typography.body1Medium,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TraineeMealRecordDetailPreview() {
    TnTTheme {
        TraineeMealRecordDetailScreen(
            state = TraineeMealRecordDetailUiState(
                image = "image",
                mealType = MealType.BREAKFAST,
                memo = "오늘은 계란을 먹었습니다.",
            ),
            context = LocalContext.current,
            dateFormatter = DateFormatter(),
            onClickMore = { },
            onClickBack = { },
        )
    }
}
