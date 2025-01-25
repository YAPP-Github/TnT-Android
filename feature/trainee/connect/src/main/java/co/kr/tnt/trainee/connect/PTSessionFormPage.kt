package co.kr.tnt.connect.trainee

import TraineeConnectContract.TraineeConnectUiState
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.connect.model.PTSessionFormData
import co.kr.tnt.designsystem.component.TnTLabeledTextField
import co.kr.tnt.designsystem.component.TnTTopBarWithBackButton
import co.kr.tnt.designsystem.component.button.TnTBottomButton
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.feature.trainee.connect.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

private const val MAX_COUNT = 99

@Composable
internal fun PTSessionFormPage(
    state: TraineeConnectUiState,
    onNextClick: (PTSessionFormData) -> Unit,
    onBackClick: () -> Unit,
) {
    BackHandler { onBackClick() }

    var completedSession by remember { mutableStateOf("") }
    var totalSession by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf<LocalDate?>(null) }

    /**
     * 모든 입력 값의 유효성을 확인
     * 유효성 조건:
     * 1. 모든 항목(완료된 회차, 총 등록 회차, 시작일)이 입력되어야 한다
     * 2. 완료된 회차가 총 등록 회차보다 크지 않아야 한다
     */
    val isFormValid by remember {
        derivedStateOf {
            completedSession.isNotEmpty() && totalSession.isNotEmpty() && startDate != null &&
                (completedSession.toIntOrNull() ?: 0) < (totalSession.toIntOrNull() ?: 0)
        }
    }

    Scaffold(
        // TODO 버튼 클릭 시 코드 입력 화면으로 이동
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
                    text = stringResource(R.string.since_when_with_trainer, state.trainerState.name),
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
                    selectedDate = startDate,
                    onDateSelected = { startDate = it },
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
                    TnTLabeledTextField(
                        title = stringResource(R.string.completed_session_until_now),
                        value = completedSession,
                        placeholder = "0",
                        isSingleLine = true,
                        isRequired = true,
                        keyboardType = KeyboardType.Number,
                        trailingComponent = {
                            UnitLabel(R.string.count_unit)
                        },
                        onValueChange = { newValue ->
                            if (validateInput(newValue)) {
                                completedSession = newValue
                            }
                        },
                        modifier = Modifier.weight(1f),
                    )
                    Text(
                        text = "/",
                        color = TnTTheme.colors.neutralColors.Neutral600,
                        style = TnTTheme.typography.body1Medium,
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.Bottom),
                    )
                    TnTLabeledTextField(
                        title = stringResource(R.string.total_register_session),
                        value = totalSession,
                        placeholder = "0",
                        isSingleLine = true,
                        isRequired = true,
                        keyboardType = KeyboardType.Number,
                        trailingComponent = {
                            UnitLabel(R.string.count_unit)
                        },
                        onValueChange = { newValue ->
                            if (validateInput(newValue)) {
                                totalSession = newValue
                            }
                        },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
            TnTBottomButton(
                text = stringResource(R.string.next),
                modifier = Modifier.align(Alignment.BottomCenter),
                enabled = isFormValid,
                onClick = {
                    val formData = PTSessionFormData(
                        completedSession = completedSession.toInt(),
                        totalSession = totalSession.toInt(),
                        startDate = startDate,
                    )
                    onNextClick(formData)
                },
            )
        }
    }
}

@Composable
private fun DatePicker(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
) {
    val context = LocalContext.current
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                val today = LocalDate.now()

                DatePickerDialog(
                    context,
                    { _, selectedYear, selectedMonth, selectedDay ->
                        val newDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
                        onDateSelected(newDate)
                    },
                    today.year,
                    today.monthValue - 1,
                    today.dayOfMonth,
                )
                    .apply {
                        // 오늘 이후는 선택 불가능
                        datePicker.maxDate = Calendar.getInstance().timeInMillis
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

/**
 * 유효한 입력 값인지 검사
 * 형식: 99 이하의 정수
 */
private fun validateInput(input: String): Boolean {
    return input.isEmpty() || (input.toIntOrNull() != null && !input.startsWith("0") && input.toInt() <= MAX_COUNT)
}

@Preview(showBackground = true)
@Composable
private fun PTSessionFormPagePreview() {
    TnTTheme {
        PTSessionFormPage(
            onNextClick = {},
            onBackClick = {},
            state = TODO(),
        )
    }
}
