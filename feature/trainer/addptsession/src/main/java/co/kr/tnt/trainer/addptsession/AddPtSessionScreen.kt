package co.kr.tnt.trainer.addptsession

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.core.designsystem.R.drawable.ic_arrow_down
import co.kr.tnt.core.designsystem.R.drawable.ic_check_true
import co.kr.tnt.core.designsystem.R.drawable.ic_delete
import co.kr.tnt.core.designsystem.R.drawable.ic_red_clock
import co.kr.tnt.core.ui.R.string.core_cancel
import co.kr.tnt.core.ui.R.string.core_length_warning
import co.kr.tnt.core.ui.R.string.core_ok
import co.kr.tnt.designsystem.component.TnTBottomSheetDialog
import co.kr.tnt.designsystem.component.TnTDivider
import co.kr.tnt.designsystem.component.TnTIconPopupDialog
import co.kr.tnt.designsystem.component.TnTSingleButtonPopupDialog
import co.kr.tnt.designsystem.component.TnTTopBarWithBackButton
import co.kr.tnt.designsystem.component.TnTWheelTimePicker
import co.kr.tnt.designsystem.component.button.TnTBottomButton
import co.kr.tnt.designsystem.component.button.TnTTextButton
import co.kr.tnt.designsystem.component.button.model.ButtonSize
import co.kr.tnt.designsystem.component.button.model.ButtonType
import co.kr.tnt.designsystem.component.calendar.TnTCalendarSelector
import co.kr.tnt.designsystem.component.calendar.TnTMonthCalendar
import co.kr.tnt.designsystem.component.calendar.model.DayState
import co.kr.tnt.designsystem.component.calendar.utils.rememberMostVisibleMonth
import co.kr.tnt.designsystem.component.textfield.TnTLabeledTextField
import co.kr.tnt.designsystem.component.textfield.TnTSelectableLabeledTextField
import co.kr.tnt.designsystem.component.textfield.model.TnTTextFieldSize
import co.kr.tnt.designsystem.snackbar.LocalSnackbar
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.domain.model.MemberInfo
import co.kr.tnt.domain.utils.DateFormatter
import co.kr.tnt.feature.trainer.addptsession.R
import co.kr.tnt.trainer.addptsession.AddPtSessionContract.AddPtSessionSideEffect
import co.kr.tnt.trainer.addptsession.AddPtSessionContract.AddPtSessionUiEvent
import co.kr.tnt.trainer.addptsession.AddPtSessionContract.AddPtSessionUiState
import co.kr.tnt.trainer.addptsession.AddPtSessionContract.AddPtSessionUiState.DialogState
import co.kr.tnt.ui.component.TnTLoadingScreen
import co.kr.tnt.ui.extensions.clearFocusOnTap
import co.kr.tnt.ui.utils.throttled
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.yearMonth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@Composable
internal fun AddPtSessionRoute(
    selectedDate: String,
    viewModel: AddPtSessionViewModel = hiltViewModel(),
    navigateToPrevious: () -> Unit,
) {
    val context = LocalContext.current
    val snackbar = LocalSnackbar.current

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val dateFormatter = remember { DateFormatter() }

    BackHandler { viewModel.setEvent(AddPtSessionUiEvent.OnClickBack) }

    LaunchedEffect(selectedDate) {
        viewModel.setEvent(AddPtSessionUiEvent.OnSelectDate(dateFormatter.parse(selectedDate)))
    }

    AddPtSessionScreen(
        state = state,
        onClickBack = { viewModel.setEvent(AddPtSessionUiEvent.OnClickBack) },
        onClickMember = { viewModel.setEvent(AddPtSessionUiEvent.OnClickMember) },
        onClickDate = { viewModel.setEvent(AddPtSessionUiEvent.OnClickDate) },
        onClickStartTime = { viewModel.setEvent(AddPtSessionUiEvent.OnClickStartTime) },
        onClickEndTime = { viewModel.setEvent(AddPtSessionUiEvent.OnClickEndTime) },
        onClickMinuteChip = { minute -> viewModel.setEvent(AddPtSessionUiEvent.OnClickMinuteChip(minute)) },
        onChangeMemo = { memo -> viewModel.setEvent(AddPtSessionUiEvent.OnChangeMemo(memo)) },
        onClickComplete = { viewModel.setEvent(AddPtSessionUiEvent.OnClickComplete) },
    )

    if (showBottomSheet) {
        TnTBottomSheetDialog(
            onDismissRequest = { showBottomSheet = false },
            content = {
                when (state.sheetType) {
                    AddPtSessionUiState.BottomSheetType.NONE -> Unit
                    AddPtSessionUiState.BottomSheetType.SELECT_MEMBER -> MembersBottomSheetContent(
                        members = state.members,
                        selectedMember = state.selectedMember,
                        onSelectMember = { member ->
                            viewModel.setEvent(AddPtSessionUiEvent.OnSelectMember(member))
                        },
                        onDismissRequest = { showBottomSheet = false },
                    )

                    AddPtSessionUiState.BottomSheetType.SELECT_DATE -> CalendarBottomSheetContent(
                        selectedDate = state.selectedDate,
                        onDismissRequest = { showBottomSheet = false },
                        onClickConfirm = { selectedDate ->
                            viewModel.setEvent(AddPtSessionUiEvent.OnSelectDate(selectedDate))
                        },
                    )

                    AddPtSessionUiState.BottomSheetType.SELECT_START_TIME -> TimePickerBottomSheetContent(
                        title = stringResource(R.string.select_start_time),
                        selectedTime = state.selectedStartTime,
                        onDismissRequest = { showBottomSheet = false },
                        onClickConfirm = { selectTime ->
                            viewModel.setEvent(AddPtSessionUiEvent.OnSelectStartTime(selectTime))
                        },
                    )

                    AddPtSessionUiState.BottomSheetType.SELECT_END_TIME -> TimePickerBottomSheetContent(
                        title = stringResource(R.string.select_end_time),
                        selectedTime = state.selectedEndTime,
                        onDismissRequest = { showBottomSheet = false },
                        onClickConfirm = { selectTime ->
                            viewModel.setEvent(AddPtSessionUiEvent.OnSelectEndTime(selectTime))
                        },
                    )
                }
            },
        )
    }

    Dialog(
        dialogState = state.dialogState,
        onClickConfirm = { viewModel.setEvent(AddPtSessionUiEvent.OnClickDialogConfirm) },
        onDismissDialog = { viewModel.setEvent(AddPtSessionUiEvent.OnDismissDialog) },
    )

    if (state.isLoading) {
        TnTLoadingScreen()
    }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                AddPtSessionSideEffect.ShowBottomSheet -> {
                    showBottomSheet = true
                }

                AddPtSessionSideEffect.HideBottomSheet -> {
                    showBottomSheet = false
                }

                is AddPtSessionSideEffect.ShowToast -> snackbar.show(effect.message.asString(context))
                AddPtSessionSideEffect.NavigateToPrevious -> navigateToPrevious()
            }
        }
    }
}

@Composable
private fun AddPtSessionScreen(
    state: AddPtSessionUiState,
    onClickBack: () -> Unit,
    onClickMember: () -> Unit,
    onClickDate: () -> Unit,
    onClickStartTime: () -> Unit,
    onClickEndTime: () -> Unit,
    onClickMinuteChip: (minute: Int) -> Unit,
    onChangeMemo: (memo: String) -> Unit,
    onClickComplete: () -> Unit,
) {
    val dateFormatter = remember { DateFormatter() }

    Scaffold(
        topBar = {
            TnTTopBarWithBackButton(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.add_pt_session),
                onBackClick = onClickBack,
                showStoke = true,
            )
        },
        containerColor = TnTTheme.colors.commonColors.Common0,
        modifier = Modifier.clearFocusOnTap(),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .clearFocusOnTap()
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(innerPadding)
                    .imePadding()
                    .verticalScroll(state = rememberScrollState()),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Description()
                    Spacer(modifier = Modifier.height(48.dp))
                    Selector(
                        title = stringResource(R.string.select_member),
                        value = state.selectedMember?.traineeName ?: "",
                        placeholder = stringResource(R.string.insert_memeber),
                        onClick = onClickMember,
                    )
                    Spacer(modifier = Modifier.height(48.dp))
                    Selector(
                        title = stringResource(R.string.pt_date),
                        value = state.selectedDate?.let { selectedDate ->
                            dateFormatter.format(selectedDate, "yyyy/MM/dd")
                        } ?: "",
                        placeholder = stringResource(R.string.insert_date),
                        onClick = onClickDate,
                    )
                    Spacer(modifier = Modifier.height(48.dp))
                    TimeSelector(
                        startTime = state.selectedStartTime,
                        endTime = state.selectedEndTime,
                        dateFormatter = dateFormatter,
                        onClickStartTime = onClickStartTime,
                        onClickEndTime = onClickEndTime,
                        isWarning = state.isErrorTime,
                    )
                    if (state.selectedStartTime != null && state.selectedEndTime == null) {
                        Spacer(modifier = Modifier.height(48.dp))
                        MinuteChips(
                            selectedMinute = state.totalSessionMinute ?: 0,
                            onClickChip = onClickMinuteChip,
                        )
                    }
                    state.totalSessionMinute?.let { totalSessionMinute ->
                        Spacer(modifier = Modifier.height(20.dp))
                        TotalSessionMinute(
                            minute = totalSessionMinute,
                        )
                    }
                    Spacer(modifier = Modifier.height(48.dp))
                    Memo(
                        value = state.memo,
                        isWarning = state.isErrorMemo,
                        onValueChanged = onChangeMemo,
                    )
                    Spacer(modifier = Modifier.height(70.dp))
                }
            }

            TnTBottomButton(
                text = "완료",
                enabled = state.isEnableComplete,
                modifier = Modifier.align(Alignment.BottomCenter),
                onClick = throttled { onClickComplete() },
            )
        }
    }
}

@Composable
private fun Description() {
    Text(
        text = stringResource(R.string.when_to_have_pt),
        style = TnTTheme.typography.h2,
        color = TnTTheme.colors.neutralColors.Neutral950,
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = stringResource(R.string.schedule_registered_for_member),
        style = TnTTheme.typography.body2Medium,
        color = TnTTheme.colors.neutralColors.Neutral500,
    )
}

@Composable
private fun Selector(
    title: String,
    value: String,
    placeholder: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isWarning: Boolean = false,
) {
    TnTSelectableLabeledTextField(
        title = title,
        showRequiredTitleBadge = true,
        value = value,
        isWarning = isWarning,
        placeholder = placeholder,
        modifier = modifier,
        trailing = {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 8.dp),
                painter = painterResource(ic_arrow_down),
                contentDescription = null,
                tint = TnTTheme.colors.neutralColors.Neutral400,
            )
        },
        onClickTextField = onClick,
    )
}

@Composable
private fun TimeSelector(
    startTime: LocalTime?,
    endTime: LocalTime?,
    dateFormatter: DateFormatter,
    onClickStartTime: () -> Unit,
    onClickEndTime: () -> Unit,
    modifier: Modifier = Modifier,
    isWarning: Boolean = false,
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Selector(
            title = stringResource(R.string.start_time),
            value = startTime?.let {
                dateFormatter.format(it, "HH:mm")
            } ?: "",
            onClick = onClickStartTime,
            placeholder = "09:00",
            isWarning = isWarning,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = "~",
            style = TnTTheme.typography.body1Medium,
            color = TnTTheme.colors.neutralColors.Neutral500,
            modifier = Modifier
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    bottom = 8.dp,
                )
                .align(Alignment.Bottom),
        )
        Selector(
            title = stringResource(R.string.end_time),
            value = endTime?.let {
                dateFormatter.format(it, "HH:mm")
            } ?: "",
            onClick = onClickEndTime,
            placeholder = "10:00",
            isWarning = isWarning,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun MinuteChips(
    selectedMinute: Int,
    onClickChip: (minute: Int) -> Unit,
) {
    Text(
        text = stringResource(R.string.pt_time),
        style = TnTTheme.typography.body1Bold,
        color = TnTTheme.colors.neutralColors.Neutral900,
    )
    Spacer(modifier = Modifier.height(16.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        for (minute in 40..60 step (10)) {
            MinuteChip(
                minute = minute,
                isSelected = selectedMinute == minute,
                modifier = Modifier.weight(1f),
                onClick = onClickChip,
            )
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        for (minute in 70..90 step (10)) {
            MinuteChip(
                minute = minute,
                isSelected = selectedMinute == minute,
                modifier = Modifier.weight(1f),
                onClick = onClickChip,
            )
        }
    }
}

@Composable
private fun MinuteChip(
    minute: Int,
    isSelected: Boolean,
    modifier: Modifier,
    onClick: (minute: Int) -> Unit,
) {
    TnTTextButton(
        stringResource(R.string.minute, minute),
        modifier = modifier,
        type = if (isSelected) ButtonType.RedOutline else ButtonType.GrayOutline,
        onClick = { onClick(minute) },
    )
}

@Composable
private fun TotalSessionMinute(
    minute: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(color = TnTTheme.colors.neutralColors.Neutral100)
            .padding(
                vertical = 16.dp,
                horizontal = 20.dp,
            ),
    ) {
        Icon(
            painter = painterResource(ic_red_clock),
            contentDescription = null,
            tint = Color.Unspecified,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = buildAnnotatedString {
                append(stringResource(R.string.total_prefix))
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("${minute}분")
                }
                append(stringResource(R.string.minute_suffix))
            },
            style = TnTTheme.typography.body2Medium,
            color = TnTTheme.colors.neutralColors.Neutral900,
        )
    }
}

@Composable
private fun Memo(
    value: String,
    isWarning: Boolean,
    onValueChanged: (String) -> Unit,
) {
    TnTLabeledTextField(
        title = stringResource(R.string.write_memo),
        value = value,
        onValueChange = onValueChanged,
        modifier = Modifier.fillMaxWidth(),
        size = TnTTextFieldSize.LARGE,
        maxLength = 30,
        isWarning = isWarning,
        warningMessage = stringResource(core_length_warning, 30),
    )
}

@Composable
private fun MembersBottomSheetContent(
    members: List<MemberInfo>,
    selectedMember: MemberInfo?,
    onSelectMember: (member: MemberInfo) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.heightIn(max = 708.dp),
    ) {
        SheetTopBar(
            title = stringResource(R.string.select_member),
            onDismissRequest = onDismissRequest,
        )
        LazyColumn {
            items(members.size) { index ->
                members[index].let { member ->
                    MemberItem(
                        member = member,
                        isSelected = member == selectedMember,
                        onClick = onSelectMember,
                    )
                }
            }
        }
    }
}

@Composable
private fun MemberItem(
    member: MemberInfo,
    isSelected: Boolean,
    onClick: (MemberInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(member) }
            .padding(
                vertical = 16.dp,
                horizontal = 20.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = member.traineeName,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = TnTTheme.typography.body1SemiBold,
            color = TnTTheme.colors.neutralColors.Neutral600,
        )
        if (isSelected) {
            Icon(
                painter = painterResource(ic_check_true),
                contentDescription = null,
                tint = Color.Unspecified,
            )
        }
    }
}

@Composable
private fun CalendarBottomSheetContent(
    onDismissRequest: () -> Unit,
    onClickConfirm: (day: LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    selectedDate: LocalDate? = null,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) {
    val now = remember { LocalDate.now() }
    val initSelectedDate = selectedDate ?: now
    var selectedDay by rememberSaveable { mutableStateOf(initSelectedDate) }
    val calendarState = rememberCalendarState(
        firstVisibleMonth = initSelectedDate.yearMonth,
        firstDayOfWeek = DayOfWeek.SUNDAY,
        startMonth = initSelectedDate.minusYears(10).yearMonth,
        endMonth = initSelectedDate.plusYears(10).yearMonth,
    )
    val visibleMonth = rememberMostVisibleMonth(calendarState)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
    ) {
        SheetTopBar(
            title = stringResource(R.string.select_pt_date),
            onDismissRequest = onDismissRequest,
        )
        Column(
            modifier = Modifier.height(446.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
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
            Spacer(Modifier.height(20.dp))
            TnTMonthCalendar(
                state = calendarState,
                onClickDay = { selectedDay = it },
                dayState = { day -> DayState(isSelected = day == selectedDay) },
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
        SheetConfirm { onClickConfirm(selectedDay) }
        Spacer(Modifier.height(54.dp))
    }
}

@Composable
private fun TimePickerBottomSheetContent(
    title: String,
    onDismissRequest: () -> Unit,
    onClickConfirm: (time: LocalTime) -> Unit,
    modifier: Modifier = Modifier,
    selectedTime: LocalTime? = null,
) {
    val now = remember { LocalTime.now() }
    var selectTime by rememberSaveable { mutableStateOf(selectedTime ?: now) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
    ) {
        SheetTopBar(
            title = title,
            onDismissRequest = onDismissRequest,
        )
        Spacer(Modifier.height(24.dp))
        TnTWheelTimePicker(
            modifier = Modifier
                .padding(
                    vertical = 20.dp,
                    horizontal = 24.dp,
                ),
            initialTime = selectTime ?: now,
            onTimeSelected = { time ->
                selectTime = time
            },
        )
        Spacer(Modifier.height(40.dp))
        SheetConfirm { onClickConfirm(selectTime) }
        Spacer(Modifier.height(54.dp))
    }
}

@Composable
private fun SheetTopBar(
    title: String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = title,
                style = TnTTheme.typography.h3,
                color = TnTTheme.colors.neutralColors.Neutral900,
            )
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .clickable(onClick = onDismissRequest)
                    .size(32.dp),
            ) {
                Icon(
                    painter = painterResource(ic_delete),
                    contentDescription = null,
                )
            }
        }
        TnTDivider()
    }
}

@Composable
private fun SheetConfirm(
    onClick: () -> Unit,
) {
    TnTTextButton(
        text = stringResource(core_ok),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        size = ButtonSize.Large,
        type = ButtonType.Primary,
        onClick = onClick,
    )
}

@Composable
private fun Dialog(
    dialogState: DialogState,
    onClickConfirm: () -> Unit,
    onDismissDialog: () -> Unit,
) {
    when (dialogState) {
        DialogState.NONE -> Unit
        DialogState.CHECK_CANCEL_ADD -> {
            TnTIconPopupDialog(
                title = stringResource(R.string.cancel_pt_registration),
                content = stringResource(R.string.schedule_will_not_be_saved),
                leftButtonText = stringResource(core_cancel),
                rightButtonText = stringResource(core_ok),
                onLeftButtonClick = onDismissDialog,
                onRightButtonClick = onClickConfirm,
                onDismiss = onDismissDialog,
            )
        }

        DialogState.SUCCESS_ADD -> {
            TnTSingleButtonPopupDialog(
                title = stringResource(R.string.added_pt_schedule),
                content = stringResource(R.string.schedule_shown_to_trainee),
                buttonText = stringResource(core_ok),
                cancelable = false,
                onButtonClick = onClickConfirm,
                onDismiss = onDismissDialog,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddPtSessionScreenPreview() {
    TnTTheme {
        AddPtSessionScreen(
            state = AddPtSessionUiState(),
            onClickBack = { },
            onClickMember = { },
            onClickDate = { },
            onClickStartTime = { },
            onClickEndTime = { },
            onClickMinuteChip = { },
            onChangeMemo = { },
            onClickComplete = { },
        )
    }
}
