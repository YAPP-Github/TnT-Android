package co.kr.tnt.trainee.connect

import android.app.DatePickerDialog
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.core.ui.R.string.core_entered_wrong_text
import co.kr.tnt.core.ui.R.string.core_next
import co.kr.tnt.designsystem.component.TnTTopBarWithBackButton
import co.kr.tnt.designsystem.component.button.TnTBottomButton
import co.kr.tnt.designsystem.component.textfield.TnTSelectableLabeledTextField2
import co.kr.tnt.designsystem.component.textfield.TnTTextField2
import co.kr.tnt.designsystem.component.textfield.model.TnTTextFieldSize
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.domain.utils.DateFormatter
import co.kr.tnt.feature.trainee.connect.R
import co.kr.tnt.ui.component.TnTLoadingScreen
import co.kr.tnt.ui.extensions.clearFocusOnTap
import co.kr.tnt.ui.utils.throttled
import java.time.LocalDate
import java.time.ZoneId

private const val MAX_COUNT = 99
private val DEFAULT_DATE = LocalDate.of(2000, 1, 1)

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
    onClickNext: () -> Unit,
    onClickBack: () -> Unit,
) {
    BackHandler { onClickBack() }

    val today = remember { LocalDate.now() }
    val dateFormatter = remember { DateFormatter() }
    val context = LocalContext.current

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
                onBackClick = onClickBack,
            )
        },
        containerColor = TnTTheme.colors.commonColors.Common0,
        modifier = Modifier.clearFocusOnTap(),
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
                TnTSelectableLabeledTextField2(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    value = sessionStartDate?.let {
                        dateFormatter.format(
                            date = it,
                            pattern = "yyyy/MM/dd",
                        )
                    } ?: "",
                    placeholder = dateFormatter.format(
                        date = DEFAULT_DATE,
                        pattern = "yyyy/MM/dd",
                    ),
                    onClickTextField = {
                        DatePickerDialog(
                            context,
                            { _, selectedYear, selectedMonth, selectedDay ->
                                val newDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
                                onChangeSessionStartDate(newDate)
                            },
                            sessionStartDate?.year ?: DEFAULT_DATE.year,
                            (sessionStartDate?.monthValue?.minus(1)) ?: (DEFAULT_DATE.monthValue - 1),
                            sessionStartDate?.dayOfMonth ?: DEFAULT_DATE.dayOfMonth,
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
                    title = stringResource(R.string.pt_start_day),
                    showRequiredTitleBadge = true,
                    size = TnTTextFieldSize.SMALL,
                )
                Spacer(Modifier.padding(top = 48.dp))
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f),
                        ) {
                            Text(
                                text = stringResource(R.string.completed_session_until_now),
                                style = TnTTheme.typography.body1Bold,
                                color = TnTTheme.colors.neutralColors.Neutral900,
                            )
                            Text(
                                text = "*",
                                style = TnTTheme.typography.body1Bold,
                                color = TnTTheme.colors.redColors.Red500,
                            )
                        }
                        Spacer(modifier = Modifier.width(32.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f),
                        ) {
                            Text(
                                text = stringResource(R.string.total_register_session),
                                style = TnTTheme.typography.body1Bold,
                                color = TnTTheme.colors.neutralColors.Neutral900,
                            )
                            Text(
                                text = "*",
                                style = TnTTheme.typography.body1Bold,
                                color = TnTTheme.colors.redColors.Red500,
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        TnTTextField2(
                            value = completedSessionCount,
                            placeholder = "0",
                            isWarning = showCompletedSessionWarning || showTotalSessionWarning,
                            keyboardType = KeyboardType.Number,
                            trailing = {
                                UnitLabel(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    stringResId = R.string.count_unit,
                                )
                            },
                            onValueChange = { newValue ->
                                onChangeCompletedSessionCount(newValue)
                            },
                            modifier = Modifier.weight(1f),
                        )
                        Text(
                            text = "/",
                            color = TnTTheme.colors.neutralColors.Neutral600,
                            style = TnTTheme.typography.body1Medium,
                        )
                        TnTTextField2(
                            value = totalSessionCount,
                            placeholder = "0",
                            isWarning = showTotalSessionWarning || showCompletedSessionWarning,
                            keyboardType = KeyboardType.Number,
                            trailing = {
                                UnitLabel(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    stringResId = R.string.count_unit,
                                )
                            },
                            onValueChange = { newValue ->
                                onChangeTotalSessionCount(newValue)
                            },
                            modifier = Modifier.weight(1f),
                        )
                    }
                    if (showCompletedSessionWarning || showTotalSessionWarning) {
                        Spacer(Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stringResource(core_entered_wrong_text),
                                    style = TnTTheme.typography.body2Medium,
                                    color = TnTTheme.colors.redColors.Red500,
                                )
                            }
                            Spacer(modifier = Modifier.width(32.dp))
                            Box(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stringResource(core_entered_wrong_text),
                                    style = TnTTheme.typography.body2Medium,
                                    color = TnTTheme.colors.redColors.Red500,
                                )
                            }
                        }
                    }
                }
            }
            TnTBottomButton(
                text = stringResource(core_next),
                modifier = Modifier.align(Alignment.BottomCenter),
                enabled = isFormValid,
                onClick = throttled { onClickNext() },
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
private fun UnitLabel(
    modifier: Modifier = Modifier,
    stringResId: Int,
) {
    Text(
        modifier = modifier.padding(end = 12.dp),
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
            onClickNext = { },
            onClickBack = { },
            onChangeSessionStartDate = { },
            onChangeCompletedSessionCount = { },
            onChangeTotalSessionCount = { },
        )
    }
}
