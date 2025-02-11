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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import co.kr.tnt.designsystem.component.TnTOutlinedTextField
import co.kr.tnt.designsystem.component.TnTSelectableTextField
import co.kr.tnt.designsystem.component.TnTTopBarWithBackButton
import co.kr.tnt.designsystem.component.button.TnTTextButton
import co.kr.tnt.designsystem.component.button.model.ButtonSize
import co.kr.tnt.designsystem.component.button.model.ButtonType
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.domain.IMAGE_MAX_SIZE
import co.kr.tnt.domain.model.RecordType.MealType
import co.kr.tnt.domain.utils.DateFormatter
import co.kr.tnt.trainee.mealrecord.TraineeMealRecordContract.TraineeMealRecordUiEvent
import co.kr.tnt.trainee.mealrecord.TraineeMealRecordContract.TraineeMealRecordUiState
import co.kr.tnt.ui.coil.ResizeTransformation
import co.kr.tnt.ui.model.RecordChip
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
        onClickDate = { /* TODO: 달력 바텀시트 */ },
        onClickTime = { /* TODO: 타임 피커 */ },
        onSelectMealType = { type ->
            viewModel.setEvent(TraineeMealRecordUiEvent.OnSelectMealType(type))
        },
        onChangeMemo = { memo ->
            viewModel.setEvent(TraineeMealRecordUiEvent.OnChangeMemo(memo))
        },
        onClickBack = navigateToPrevious,
        onClickSaveButton = { viewModel.setEvent(TraineeMealRecordUiEvent.OnClickSave) },
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
    onImageSelect: (url: Uri) -> Unit,
    onClickDeleteImage: () -> Unit,
    onClickDate: () -> Unit,
    onClickTime: () -> Unit,
    onSelectMealType: (type: String) -> Unit,
    onChangeMemo: (memo: String) -> Unit,
    onClickSaveButton: () -> Unit,
    onClickBack: () -> Unit,
) {
    val dateFormatter = remember { DateFormatter() }

    val pickMediaLauncher = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            onImageSelect(uri)
        }
    }

    Scaffold(
        containerColor = TnTTheme.colors.commonColors.Common0,
        topBar = {
            TnTTopBarWithBackButton(
                title = "식단 기록",
                onBackClick = onClickBack,
                showShadow = true,
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = TnTTheme.colors.neutralColors.Neutral200,
            )
        },
        bottomBar = {
            TnTTextButton(
                text = "저장",
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                size = ButtonSize.XLarge,
                type = ButtonType.Primary,
                enabled = false,
                onClick = onClickSaveButton,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .imePadding()
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
                Column(
                    verticalArrangement = Arrangement.spacedBy(48.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    MealDate(
                        date = state.date,
                        dateFormatter = dateFormatter,
                        onClick = onClickDate,
                    )
                    MealTime(
                        time = state.time,
                        dateFormatter = dateFormatter,
                        onClick = onClickTime,
                    )
                    MealTypes(
                        selectedType = state.mealType,
                        onClick = onSelectMealType,
                    )
                    MealMemo(
                        state = state,
                        onValueChange = onChangeMemo,
                    )
                    Spacer(Modifier.height(64.dp))
                }
            }
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

@Composable
private fun MealDate(
    date: LocalDate,
    dateFormatter: DateFormatter,
    onClick: () -> Unit,
) {
    TnTSelectableTextField(
        title = "식사 날짜",
        value = dateFormatter.format(date, "yyyy/MM/dd"),
        onValueChange = { },
        isRequired = true,
        onClick = onClick,
    )
}

@Composable
private fun MealTime(
    time: LocalTime,
    dateFormatter: DateFormatter,
    onClick: () -> Unit,
) {
    TnTSelectableTextField(
        title = "식사 시간",
        value = dateFormatter.format(time, "HH:mm"),
        onValueChange = { },
        isRequired = true,
        onClick = onClick,
    )
}

@Composable
private fun MealTypes(
    selectedType: String?,
    onClick: (String) -> Unit,
) {
    val typeList = listOf(
        MealType.BREAKFAST,
        MealType.LUNCH,
        MealType.DINNER,
        MealType.SNACK,
    )
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "분류",
                style = TnTTheme.typography.body1Bold,
                color = TnTTheme.colors.neutralColors.Neutral900,
            )
            Text(
                text = "*",
                style = TnTTheme.typography.body1Bold,
                color = TnTTheme.colors.redColors.Red500,
            )
        }
        Spacer(Modifier.height(10.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            typeList.forEach { type ->
                val title = RecordChip.create(type).title
                val isSelected = selectedType.equals(type.name, ignoreCase = true)
                TnTTextButton(
                    text = title,
                    modifier = Modifier.weight(1f),
                    size = ButtonSize.Medium,
                    type = if (isSelected) ButtonType.RedOutline else ButtonType.GrayOutline,
                    onClick = { onClick(type.name) },
                )
            }
        }
    }
}

@Composable
private fun MealMemo(
    state: TraineeMealRecordUiState,
    onValueChange: (String) -> Unit,
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "메모하기",
                style = TnTTheme.typography.body1Bold,
                color = TnTTheme.colors.neutralColors.Neutral900,
            )
            Text(
                text = "*",
                style = TnTTheme.typography.body1Bold,
                color = TnTTheme.colors.redColors.Red500,
            )
        }
        Spacer(Modifier.height(10.dp))
        TnTOutlinedTextField(
            value = state.memo,
            onValueChange = { newValue ->
                if (newValue.length <= 100) {
                    onValueChange(newValue)
                }
            },
            placeholder = "식단에 대한 정보를 입력해주세요!",
            maxLength = 100,
            isError = state.showWarning,
            warningMessage = "100자 미만으로 입력해주세요",
        )
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
            onClickDate = { },
            onClickTime = { },
            onSelectMealType = { },
            onChangeMemo = { },
            onClickBack = { },
            onClickSaveButton = { },
        )
    }
}
