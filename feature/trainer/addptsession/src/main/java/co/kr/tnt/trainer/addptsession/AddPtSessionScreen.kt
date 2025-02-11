package co.kr.tnt.trainer.addptsession

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.component.TnTOutlinedTextField
import co.kr.tnt.designsystem.component.TnTTextField
import co.kr.tnt.designsystem.component.TnTTopBarWithBackButton
import co.kr.tnt.designsystem.component.button.TnTBottomButton
import co.kr.tnt.designsystem.component.button.TnTTextButton
import co.kr.tnt.designsystem.component.button.model.ButtonType
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.domain.utils.DateFormatter
import co.kr.tnt.trainer.addptsession.AddPtSessionContract.AddPtSessionSideEffect
import co.kr.tnt.trainer.addptsession.AddPtSessionContract.AddPtSessionUiEvent
import co.kr.tnt.trainer.addptsession.AddPtSessionContract.AddPtSessionUiState
import java.time.LocalDate
import java.time.LocalTime

@Composable
internal fun AddPtSessionRoute(
    viewModel: AddPtSessionViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AddPtSessionScreen(
        state = uiState,
        onClickBack = { viewModel.setEvent(AddPtSessionUiEvent.OnClickBack) },
        onClickMember = { viewModel.setEvent(AddPtSessionUiEvent.OnClickMember) },
        onClickDate = { viewModel.setEvent(AddPtSessionUiEvent.OnClickDate) },
        onClickStartTime = { viewModel.setEvent(AddPtSessionUiEvent.OnClickStartTime) },
        onClickEndTime = { viewModel.setEvent(AddPtSessionUiEvent.OnClickEndTime) },
        onClickMinuteChip = { minute -> viewModel.setEvent(AddPtSessionUiEvent.OnClickMinuteChip(minute)) },
        onChangeMemo = { memo -> viewModel.setEvent(AddPtSessionUiEvent.OnChangeMemo(memo)) },
        onClickComplete = { viewModel.setEvent(AddPtSessionUiEvent.OnClickComplete) },
    )

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                AddPtSessionSideEffect.ShowBottomSheet -> TODO()
                AddPtSessionSideEffect.HideBottomSheet -> TODO()

                is AddPtSessionSideEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }

                AddPtSessionSideEffect.NavigateToPrevious -> TODO()
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
    val today = remember { LocalDate.now() }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(state = rememberScrollState()),
        ) {
            TnTTopBarWithBackButton(
                modifier = Modifier.fillMaxWidth(),
                title = "수업 추가하기",
                windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
                onBackClick = onClickBack,
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Description()
                Spacer(modifier = Modifier.height(48.dp))
                Selector(
                    title = "회원 선택",
                    value = state.selectedMember.name,
                    placeholder = "회원을 입력해주세요",
                    onClick = onClickMember,
                )
                Spacer(modifier = Modifier.height(48.dp))
                Selector(
                    title = "PT 날짜",
                    value = state.selectedDate?.let { selectedDate ->
                        dateFormatter.format(selectedDate, "yyyy/MM/dd")
                    } ?: "",
                    placeholder = dateFormatter.format(today, "yyyy/MM/dd"),
                    onClick = onClickDate,
                )
                Spacer(modifier = Modifier.height(48.dp))
                TimeSelector(
                    startTime = null,
                    endTime = null,
                    dateFormatter = dateFormatter,
                    onClickStartTime = onClickStartTime,
                    onClickEndTime = onClickEndTime,
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
                    onValueChanged = onChangeMemo,
                )
                Spacer(modifier = Modifier.height(70.dp))
            }
        }

        TnTBottomButton(
            text = "완료",
            modifier = Modifier.align(Alignment.BottomCenter),
            onClick = onClickComplete,
        )
    }
}

@Composable
private fun Description() {
    Text(
        text = "언제 수업할까요?",
        style = TnTTheme.typography.h2,
        color = TnTTheme.colors.neutralColors.Neutral950,
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = "일정을 등록하면 회원에게도 일정이 등록돼요",
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
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = title,
                style = TnTTheme.typography.body1Bold,
                color = TnTTheme.colors.neutralColors.Neutral900,
            )
            Text(
                text = "*",
                style = TnTTheme.typography.body1Bold,
                color = TnTTheme.colors.redColors.Red500,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        TnTTextField(
            value = value,
            enabled = false,
            isSingleLine = true,
            placeholder = placeholder,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
            trailingComponent = {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_down),
                    contentDescription = null,
                    tint = TnTTheme.colors.neutralColors.Neutral400,
                )
            },
            onValueChange = { },
        )
    }
}

@Composable
private fun TimeSelector(
    startTime: LocalTime?,
    endTime: LocalTime?,
    dateFormatter: DateFormatter,
    onClickStartTime: () -> Unit,
    onClickEndTime: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Selector(
            title = "시작 시간",
            value = startTime?.let {
                dateFormatter.format(it, "HH:mm")
            } ?: "",
            onClick = onClickStartTime,
            placeholder = "09:00",
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
            title = "종료 시간",
            value = endTime?.let {
                dateFormatter.format(it, "HH:mm")
            } ?: "",
            onClick = onClickEndTime,
            placeholder = "10:00",
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
        text = "수업 시간",
        style = TnTTheme.typography.body1Bold,
        color = TnTTheme.colors.neutralColors.Neutral900,
    )
    Spacer(modifier = Modifier.height(16.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        for (minute in 40..60 step (10))
            MinuteChip(
                minute = minute,
                isSelected = selectedMinute == minute,
                modifier = Modifier.weight(1f),
                onClick = onClickChip,
            )
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
        "+${minute}분",
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
            painter = painterResource(R.drawable.ic_red_clock),
            contentDescription = null,
            tint = Color.Unspecified,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = buildAnnotatedString {
                append("총 ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("${minute}분")
                }
                append(" 수업이에요")
            },
            style = TnTTheme.typography.body2Medium,
            color = TnTTheme.colors.neutralColors.Neutral900,
        )
    }
}

@Composable
private fun Memo(
    value: String,
    onValueChanged: (String) -> Unit,
) {
    Text(
        text = "메모하기",
        style = TnTTheme.typography.body1Bold,
        color = TnTTheme.colors.neutralColors.Neutral900,
    )
    Spacer(modifier = Modifier.height(8.dp))
    TnTOutlinedTextField(
        value = "",
        onValueChange = onValueChanged,
        maxLength = 30,
        isError = value.length >= 30,
        warningMessage = "30자 미만으로 입력해주세요",
        modifier = Modifier.fillMaxWidth(),
    )
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
