package co.kr.tnt.trainee.mealrecord.record

import android.content.Context
import android.net.Uri
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.core.ui.R.string.core_ok
import co.kr.tnt.designsystem.component.TnTBottomSheetDialog
import co.kr.tnt.designsystem.component.TnTDivider
import co.kr.tnt.designsystem.component.TnTIconPopupDialog
import co.kr.tnt.designsystem.component.TnTOutlinedTextField
import co.kr.tnt.designsystem.component.TnTSelectableTextField
import co.kr.tnt.designsystem.component.TnTSingleButtonPopupDialog
import co.kr.tnt.designsystem.component.TnTTopBarWithBackButton
import co.kr.tnt.designsystem.component.TnTWheelTimePicker
import co.kr.tnt.designsystem.component.button.TnTTextButton
import co.kr.tnt.designsystem.component.button.model.ButtonSize
import co.kr.tnt.designsystem.component.button.model.ButtonType
import co.kr.tnt.designsystem.component.calendar.TnTCalendarSelector
import co.kr.tnt.designsystem.component.calendar.TnTMonthCalendar
import co.kr.tnt.designsystem.component.calendar.model.DayState
import co.kr.tnt.designsystem.component.calendar.utils.rememberMostVisibleMonth
import co.kr.tnt.designsystem.snackbar.LocalSnackbar
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.domain.IMAGE_MAX_SIZE
import co.kr.tnt.domain.model.RecordType.MealType
import co.kr.tnt.domain.utils.DateFormatter
import co.kr.tnt.trainee.mealrecord.record.TraineeMealRecordContract.TraineeMealRecordUiEvent
import co.kr.tnt.trainee.mealrecord.record.TraineeMealRecordContract.TraineeMealRecordUiState
import co.kr.tnt.trainee.mealrecord.record.TraineeMealRecordContract.TraineeMealRecordUiState.DialogState
import co.kr.tnt.ui.coil.ResizeTransformation
import co.kr.tnt.ui.component.TnTLoadingScreen
import co.kr.tnt.ui.extensions.clearFocusOnTap
import co.kr.tnt.ui.model.RecordChip
import co.kr.tnt.ui.utils.throttled
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.kizitonwose.calendar.compose.rememberCalendarState
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth

@Composable
internal fun TraineeMealRecordRoute(
    selectedDate: String,
    navigateToPrevious: () -> Unit,
    viewModel: TraineeMealRecordViewModel = hiltViewModel(),
) {
    BackHandler { viewModel.setEvent(TraineeMealRecordUiEvent.OnClickBack) }

    val context = LocalContext.current
    val snackbar = LocalSnackbar.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val dateFormatter = remember { DateFormatter() }

    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(selectedDate) {
        viewModel.setEvent(
            TraineeMealRecordUiEvent.OnSelectMealDate(
                dateFormatter.parse(
                    selectedDate,
                ),
            ),
        )
    }

    TraineeMealRecordScreen(
        state = state,
        context = context,
        onImageSelect = { uri ->
            viewModel.setEvent(TraineeMealRecordUiEvent.OnSelectImage(imageUri = uri))
        },
        onClickDeleteImage = { viewModel.setEvent(TraineeMealRecordUiEvent.OnClickDeleteImage) },
        onClickDateSection = {
            viewModel.setEvent(TraineeMealRecordUiEvent.OnClickMealDate)
            showBottomSheet = true
        },
        onClickTimeSection = {
            viewModel.setEvent(TraineeMealRecordUiEvent.OnClickMealTime)
            showBottomSheet = true
        },
        onSelectMealType = { type ->
            viewModel.setEvent(TraineeMealRecordUiEvent.OnSelectMealType(type))
        },
        onChangeMemo = { memo ->
            viewModel.setEvent(TraineeMealRecordUiEvent.OnChangeMemo(memo))
        },
        onClickBack = {
            viewModel.setEvent(TraineeMealRecordUiEvent.OnClickBack)
        },
        onClickSaveButton = { viewModel.setEvent(TraineeMealRecordUiEvent.OnClickSave(context)) },
    )

    if (showBottomSheet) {
        TnTBottomSheetDialog(
            onDismissRequest = {
                viewModel.setEvent(TraineeMealRecordUiEvent.OnClickCloseBottomSheet)
                showBottomSheet = false
            },
            content = {
                if (state.isDateFieldFocused) {
                    CalendarBottomSheetContent(
                        date = state.date,
                        onClickClose = {
                            viewModel.setEvent(TraineeMealRecordUiEvent.OnClickCloseBottomSheet)
                            showBottomSheet = false
                        },
                        onClickConfirm = { newDate ->
                            viewModel.setEvent(TraineeMealRecordUiEvent.OnSelectMealDate(newDate))
                            showBottomSheet = false
                        },
                    )
                } else {
                    TimePickerBottomSheetContent(
                        time = state.time,
                        onClickClose = {
                            viewModel.setEvent(TraineeMealRecordUiEvent.OnClickCloseBottomSheet)
                            showBottomSheet = false
                        },
                        onClickConfirm = { newTime ->
                            viewModel.setEvent(
                                TraineeMealRecordUiEvent.OnSelectMealTime(newTime),
                            )
                            showBottomSheet = false
                        },
                    )
                }
            },
        )
    }

    Dialog(
        state = state,
        onClickExit = { viewModel.setEvent(TraineeMealRecordUiEvent.OnClickBack) },
        onClickConfirm = { viewModel.setEvent(TraineeMealRecordUiEvent.OnClickDialogConfirm) },
        onDismissDialog = { viewModel.setEvent(TraineeMealRecordUiEvent.OnDismissDialog) },
    )

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                TraineeMealRecordContract.TraineeMealRecordSideEffect.NavigateToHome -> navigateToPrevious()
                is TraineeMealRecordContract.TraineeMealRecordSideEffect.ShowToast -> snackbar.show(
                    effect.message,
                )
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
    onClickDateSection: () -> Unit,
    onClickTimeSection: () -> Unit,
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
                showStoke = true,
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
                enabled = state.isMealRecordValid,
                onClick = throttled { onClickSaveButton() },
            )
        },
        modifier = Modifier.clearFocusOnTap(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .consumeWindowInsets(innerPadding)
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
                        focusState = state.isDateFieldFocused,
                        dateFormatter = dateFormatter,
                        onClick = onClickDateSection,
                    )
                    MealTime(
                        time = state.time,
                        focusState = state.isTimeFieldFocused,
                        dateFormatter = dateFormatter,
                        onClick = onClickTimeSection,
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

    if (state.isLoading) {
        TnTLoadingScreen()
    }
}

@Composable
private fun Dialog(
    state: TraineeMealRecordUiState,
    onClickExit: () -> Unit,
    onClickConfirm: () -> Unit,
    onDismissDialog: () -> Unit,
) {
    when (state.dialogState) {
        DialogState.NONE -> Unit
        DialogState.COMPLETED -> {
            TnTSingleButtonPopupDialog(
                title = "식단을 기록했어요!",
                content = "내일도 기록해 주실 거죠?",
                buttonText = stringResource(core_ok),
                cancelable = false,
                onButtonClick = onClickConfirm,
                onDismiss = onDismissDialog,
            )
        }

        DialogState.EXIT -> {
            TnTIconPopupDialog(
                title = "식단 기록을 종료할까요?",
                content = "기록이 저장되지 않아요!",
                leftButtonText = "취소",
                rightButtonText = "확인",
                onLeftButtonClick = onClickExit,
                onRightButtonClick = onDismissDialog,
                onDismiss = onDismissDialog,
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
                    painter = painterResource(R.drawable.ic_image),
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
                            painter = painterResource(R.drawable.ic_overlay_close),
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
    focusState: Boolean,
    dateFormatter: DateFormatter,
    onClick: () -> Unit,
) {
    TnTSelectableTextField(
        title = "식사 날짜",
        value = dateFormatter.format(date, "yyyy/MM/dd"),
        onValueChange = { },
        isRequired = true,
        shouldClearFocus = focusState.not(),
        onClick = onClick,
    )
}

@Composable
private fun MealTime(
    time: LocalTime?,
    focusState: Boolean,
    dateFormatter: DateFormatter,
    onClick: () -> Unit,
) {
    val now = LocalTime.now()
    TnTSelectableTextField(
        title = "식사 시간",
        value = time?.let { dateFormatter.format(it, "HH:mm") } ?: "",
        onValueChange = { },
        isRequired = true,
        placeholder = dateFormatter.format(now, "HH:mm"),
        shouldClearFocus = focusState.not(),
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
                onValueChange(newValue)
            },
            placeholder = "식단에 대한 정보를 입력해주세요!",
            maxLength = 100,
            isError = state.showWarning,
            warningMessage = "100자 미만으로 입력해주세요",
        )
    }
}

@Composable
private fun CalendarBottomSheetContent(
    date: LocalDate,
    onClickClose: () -> Unit,
    onClickConfirm: (day: LocalDate) -> Unit,
) {
    var selectedDate by rememberSaveable { mutableStateOf(date) }

    Column(
        verticalArrangement = Arrangement.Center,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "식단 날짜 선택하기",
                color = TnTTheme.colors.neutralColors.Neutral900,
                style = TnTTheme.typography.h3,
                modifier = Modifier.weight(1f),
            )
            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = null,
                modifier = Modifier.clickable(onClick = onClickClose),
            )
        }
        TnTDivider()
        CalenderItem(
            selectedDay = selectedDate,
            onClickDay = { newDate -> selectedDate = newDate },
        )
        TnTTextButton(
            text = stringResource(core_ok),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            size = ButtonSize.Large,
            type = ButtonType.Primary,
            onClick = { onClickConfirm(selectedDate) },
        )
        Spacer(Modifier.height(54.dp))
    }
}

@Composable
private fun CalenderItem(
    selectedDay: LocalDate,
    onClickDay: (day: LocalDate) -> Unit,
) {
    val selectedYearMonth = remember(selectedDay) { YearMonth.from(selectedDay) }
    val calendarState = rememberCalendarState(
        firstVisibleMonth = selectedYearMonth,
        firstDayOfWeek = DayOfWeek.SUNDAY,
        startMonth = selectedYearMonth.minusYears(10),
        endMonth = selectedYearMonth.plusYears(10),
    )
    val coroutineScope = rememberCoroutineScope()
    val visibleMonth = rememberMostVisibleMonth(calendarState)

    Column(
        modifier = Modifier.height(446.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TnTCalendarSelector(
            yearMonth = visibleMonth,
            onClickPrevious = {
                coroutineScope.launch {
                    calendarState.animateScrollToMonth(visibleMonth.minusMonths(1))
                }
            },
            onClickNext = {
                coroutineScope.launch {
                    calendarState.animateScrollToMonth(visibleMonth.plusMonths(1))
                }
            },
        )
        Spacer(Modifier.height(12.dp))
        TnTMonthCalendar(
            state = calendarState,
            onClickDay = onClickDay,
            dayState = { day -> DayState(isSelected = day == selectedDay) },
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(Modifier.height(40.dp))
    }
}

@Composable
private fun TimePickerBottomSheetContent(
    time: LocalTime?,
    onClickClose: () -> Unit,
    onClickConfirm: (time: LocalTime) -> Unit,
) {
    val now = LocalTime.now()
    var selectedTime by rememberSaveable { mutableStateOf(time ?: now) }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.wrapContentSize(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(20.dp),
        ) {
            Text(
                text = "식단 시간 선택하기",
                color = TnTTheme.colors.neutralColors.Neutral900,
                style = TnTTheme.typography.h3,
                modifier = Modifier.weight(1f),
            )
            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = null,
                modifier = Modifier.clickable(onClick = onClickClose),
            )
        }
        TnTDivider()
        Spacer(Modifier.height(24.dp))
        TnTWheelTimePicker(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp),
            initialTime = time ?: LocalTime.now(),
            minuteInterval = 1,
            onTimeSelected = { newTime ->
                selectedTime = newTime
            },
        )
        Spacer(Modifier.height(40.dp))
        TnTTextButton(
            text = stringResource(core_ok),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            size = ButtonSize.Large,
            type = ButtonType.Primary,
            onClick = { onClickConfirm(selectedTime) },
        )
        Spacer(Modifier.height(54.dp))
    }
}

@Preview
@Composable
private fun TraineeMealRecordScreenPreview() {
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
            onClickDateSection = { },
            onClickTimeSection = { },
            onSelectMealType = { },
            onChangeMemo = { },
            onClickBack = { },
            onClickSaveButton = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarBottomSheetPreview() {
    TnTTheme {
        CalendarBottomSheetContent(
            date = LocalDate.now(),
            onClickClose = { },
            onClickConfirm = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TimePickerBottomSheetPreview() {
    TnTTheme {
        TimePickerBottomSheetContent(
            time = LocalTime.now(),
            onClickClose = { },
            onClickConfirm = { },
        )
    }
}
