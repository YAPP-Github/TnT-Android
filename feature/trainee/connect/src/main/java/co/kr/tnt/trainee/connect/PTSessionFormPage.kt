package co.kr.tnt.trainee.connect

import android.app.DatePickerDialog
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.designsystem.component.TnTLabeledTextField
import co.kr.tnt.designsystem.component.TnTTopBarWithBackButton
import co.kr.tnt.designsystem.component.button.TnTBottomButton
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.feature.trainee.connect.R
import co.kr.tnt.ui.component.TnTLoadingScreen
import co.kr.tnt.ui.utils.throttled
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import co.kr.tnt.core.ui.R as uiResource

private const val MAX_COUNT = 99

@Composable
internal fun PTSessionFormPage(
    trainerName: String,
    sessionStartDate: LocalDate?,
    completedSessionCount: String,
    totalSessionCount: String,
    isLoading: Boolean,
    onChangeSessionStartDate: (date: LocalDate) -> Unit,
    onChangeCompletedSessionCount: (count: String) -> Unit,
    onChangeTotalSessionCount: (count: String) -> Unit,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    BackHandler { onBackClick() }

    val today = LocalDate.now()

    /**
     * 모든 입력 값의 유효성을 확인
     * 유효성 조건:
     * 1. 모든 항목(완료된 회차, 총 등록 회차, 시작일)이 입력되어야 한다
     * 2. 완료된 회차가 총 등록 회차보다 크지 않아야 한다
     */
    val isFormValid = sessionStartDate != null &&
        completedSessionCount.isNotBlank() &&
        totalSessionCount.isNotBlank() &&
        isInvalidInput(completedSessionCount, allowZero = true).not() &&
        isInvalidInput(totalSessionCount, allowZero = false).not() &&
        isCompletedSessionInvalid(completedSessionCount, totalSessionCount).not()

    val showTotalSessionWarning = totalSessionCount.isNotBlank() &&
        isInvalidInput(totalSessionCount, allowZero = false)

    val showCompletedSessionWarning = completedSessionCount.isNotBlank() &&
        (
            isInvalidInput(completedSessionCount, allowZero = true) ||
                isCompletedSessionInvalid(completedSessionCount, totalSessionCount)
        )

    Scaffold(
        topBar = {
            TnTTopBarWithBackButton(
                title = stringResource(R.string.add_pt_info),
                onBackClick = onBackClick,
            )
        },
        containerColor = TnTTheme.colors.commonColors.Common0,
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .verticalScroll(rememberScrollState()),
            ) {
                Spacer(Modifier.padding(top = 24.dp))
                Text(
                    text = stringResource(
                        R.string.since_when_with_trainer,
                        trainerName,
                    ),
                    color = TnTTheme.colors.neutralColors.Neutral950,
                    style = TnTTheme.typography.h2,
                    modifier = Modifier.padding(horizontal = 24.dp),
                )
                Spacer(Modifier.padding(top = 48.dp))
                Row {
                    Text(
                        text = stringResource(R.string.pt_start_day),
                        color = TnTTheme.colors.neutralColors.Neutral900,
                        style = TnTTheme.typography.body1Bold,
                        modifier = Modifier.padding(start = 20.dp, bottom = 8.dp),
                    )
                    Text(
                        text = "*",
                        color = TnTTheme.colors.redColors.Red500,
                        style = TnTTheme.typography.body1Bold,
                    )
                }
                DatePicker(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    today = today,
                    selectedDate = sessionStartDate,
                    onDateSelected = onChangeSessionStartDate,
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = TnTTheme.colors.neutralColors.Neutral200,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
                Spacer(Modifier.padding(top = 48.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        TnTLabeledTextField(
                            title = stringResource(R.string.completed_session_until_now),
                            value = completedSessionCount,
                            placeholder = "0",
                            isSingleLine = true,
                            isRequired = true,
                            keyboardType = KeyboardType.Number,
                            showWarning = showCompletedSessionWarning || showTotalSessionWarning,
                            trailingComponent = {
                                UnitLabel(R.string.count_unit)
                            },
                            onValueChange = { newValue ->
                                onChangeCompletedSessionCount(newValue)
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Text(
                            text = if (showCompletedSessionWarning || showTotalSessionWarning) {
                                stringResource(uiResource.string.entered_wrong_text)
                            } else {
                                ""
                            },
                            style = TnTTheme.typography.body2Medium,
                            color = TnTTheme.colors.redColors.Red500,
                            modifier = Modifier.padding(top = 4.dp),
                        )
                    }
                    Text(
                        text = "/",
                        color = TnTTheme.colors.neutralColors.Neutral600,
                        style = TnTTheme.typography.body1Medium,
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.CenterVertically),
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        TnTLabeledTextField(
                            title = stringResource(R.string.total_register_session),
                            value = totalSessionCount,
                            placeholder = "0",
                            isSingleLine = true,
                            isRequired = true,
                            keyboardType = KeyboardType.Number,
                            showWarning = showTotalSessionWarning || showCompletedSessionWarning,
                            trailingComponent = {
                                UnitLabel(R.string.count_unit)
                            },
                            onValueChange = { newValue ->
                                onChangeTotalSessionCount(newValue)
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Text(
                            text = if (showTotalSessionWarning || showCompletedSessionWarning) {
                                stringResource(uiResource.string.entered_wrong_text)
                            } else {
                                ""
                            },
                            style = TnTTheme.typography.body2Medium,
                            color = TnTTheme.colors.redColors.Red500,
                            modifier = Modifier.padding(top = 4.dp),
                        )
                    }
                }
            }
            TnTBottomButton(
                text = stringResource(uiResource.string.next),
                modifier = Modifier.align(Alignment.BottomCenter),
                enabled = isFormValid,
                onClick = throttled { onNextClick() },
            )
        }
    }

    if (isLoading) {
        TnTLoadingScreen()
    }
}

/**
 * 입력이 유효하지 않은 경우 true 리턴
 * @param input 입력 값
 * @param allowZero `true`일 경우 0 입력 가능 (현재 완료된 회차)
 * @return `true`면 경고 필요, `false`면 정상 입력
 */
private fun isInvalidInput(input: String, allowZero: Boolean = false): Boolean {
    val num = input.toIntOrNull() ?: return false

    return if (allowZero) {
        (num !in 0..MAX_COUNT) || (input.length > 1 && input.startsWith("0"))
    } else {
        (num !in 1..MAX_COUNT) || (input.length > 1 && input.startsWith("0"))
    }
}

private fun isCompletedSessionInvalid(completedSession: String, totalSession: String): Boolean {
    if (completedSession.isEmpty() || totalSession.isEmpty()) return false
    return (completedSession.toIntOrNull() ?: 0) >= (totalSession.toIntOrNull() ?: 0)
}

@Composable
private fun DatePicker(
    modifier: Modifier = Modifier,
    today: LocalDate,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
) {
    val context = LocalContext.current
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    val date = selectedDate ?: today

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                DatePickerDialog(
                    context,
                    { _, selectedYear, selectedMonth, selectedDay ->
                        val newDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
                        onDateSelected(newDate)
                    },
                    date.year,
                    date.monthValue - 1,
                    date.dayOfMonth,
                )
                    .apply {
                        // 오늘 이후는 선택 불가능
                        datePicker.maxDate =
                            today
                                .atStartOfDay(ZoneId.systemDefault())
                                .toInstant()
                                .toEpochMilli()
                    }
                    .show()
            },
    ) {
        Text(
            text = selectedDate?.format(dateFormatter)
                ?: stringResource(R.string.birthday_placeholder),
            color = if (selectedDate == null) {
                TnTTheme.colors.neutralColors.Neutral400
            } else {
                TnTTheme.colors.neutralColors.Neutral600
            },
            style = TnTTheme.typography.body1Medium,
            textAlign = TextAlign.Start,
        )
    }
}

@Composable
private fun UnitLabel(stringResId: Int) {
    Text(
        text = stringResource(stringResId),
        style = TnTTheme.typography.body1Medium,
        color = TnTTheme.colors.neutralColors.Neutral400,
    )
}

@Preview
@Composable
private fun PTSessionFormPagePreview() {
    TnTTheme {
        PTSessionFormPage(
            trainerName = "김헬짱",
            sessionStartDate = LocalDate.now(),
            completedSessionCount = "15",
            totalSessionCount = "10",
            isLoading = false,
            onNextClick = { },
            onBackClick = { },
            onChangeSessionStartDate = { },
            onChangeCompletedSessionCount = { },
            onChangeTotalSessionCount = { },
        )
    }
}
