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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.component.TnTTopBarWithBackButton
import co.kr.tnt.designsystem.component.chip.TnTChip
import co.kr.tnt.designsystem.component.chip.model.ChipStyle
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.domain.IMAGE_MAX_SIZE
import co.kr.tnt.domain.model.RecordType
import co.kr.tnt.trainee.mealrecord.detail.TraineeMealRecordDetailContract.TraineeMealRecordDetailSideEffect
import co.kr.tnt.trainee.mealrecord.detail.TraineeMealRecordDetailContract.TraineeMealRecordDetailUiEvent
import co.kr.tnt.trainee.mealrecord.detail.TraineeMealRecordDetailContract.TraineeMealRecordDetailUiState
import co.kr.tnt.ui.coil.ResizeTransformation
import co.kr.tnt.ui.model.RecordChip
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
internal fun TraineeMealRecordDetailRoute(
    mealId: Int,
    navigateToPrevious: () -> Unit,
    viewModel: TraineeMealRecordDetailViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    TraineeMealRecordDetailScreen(
        state = state,
        context = context,
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
    onClickMore: () -> Unit,
    onClickBack: () -> Unit,
) {
    val chip = getMealChipStyle(state.mealType)
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(state.image)
            .transformations(ResizeTransformation(IMAGE_MAX_SIZE))
            .build(),
    )

    Scaffold(
        topBar = {
            TnTTopBarWithBackButton(
                title = state.titleDate,
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
                if (state.image != null) {
                    Image(
                        painter = state.image.let { painter },
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 20.dp),
                    )
                    Spacer(Modifier.height(8.dp))
                } else {
                    Spacer(Modifier.height(32.dp))
                }
                TnTChip(
                    text = chip?.title ?: "",
                    chipStyle = chip?.chipStyle ?: ChipStyle.PINK,
                    leadingEmoji = chip?.emoji,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                ) {
                    Text(
                        text = state.contentDate,
                        color = TnTTheme.colors.neutralColors.Neutral600,
                        style = TnTTheme.typography.body2Medium,
                    )
                    Text(
                        text = state.contentTime,
                        color = TnTTheme.colors.neutralColors.Neutral600,
                        style = TnTTheme.typography.body2Medium,
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = 2.dp,
                        color = TnTTheme.colors.neutralColors.Neutral100,
                    )
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

@Composable
fun getMealChipStyle(mealType: String): RecordChip? {
    val normalizedType = mealType.uppercase()

    val recordType = when (normalizedType) {
        "BREAKFAST" -> RecordType.MealType.BREAKFAST
        "LUNCH" -> RecordType.MealType.LUNCH
        "DINNER" -> RecordType.MealType.DINNER
        "SNACK" -> RecordType.MealType.SNACK
        else -> null
    }
    return recordType?.let { RecordChip.create(it) }
}

@Preview
@Composable
private fun TraineeMealRecordDetailPreview() {
    TnTTheme {
        TraineeMealRecordDetailScreen(
            state = TraineeMealRecordDetailUiState(
                mealType = "breakfast",
                memo = "오늘은 계란을 먹었습니다.",
            ),
            context = LocalContext.current,
            onClickMore = { },
            onClickBack = { },
        )
    }
}
