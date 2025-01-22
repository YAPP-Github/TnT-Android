package co.kr.tnt.trainee.signup

import android.app.DatePickerDialog
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
import co.kr.tnt.designsystem.component.TnTLabeledTextField
import co.kr.tnt.designsystem.component.TnTTopBarWithBackButton
import co.kr.tnt.designsystem.component.button.TnTBottomButton
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.feature.trainee.signup.R
import co.kr.tnt.trainee.signup.component.ProgressSteps
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

// 000.0 (점 포함 5자리)
private const val MAX_LENGTH = 5

@Composable
fun TraineeBasicInfoScreen() {
    // TODO 상태 관리 따로 빼기
    val today = LocalDate.now()
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf<LocalDate?>(null) }

    val isHeightValid by remember { derivedStateOf { height.isNotEmpty() && validateInput(height) } }
    val isWeightValid by remember { derivedStateOf { weight.isNotEmpty() && validateInput(weight) } }

    val isFormValid by remember { derivedStateOf { isHeightValid && isWeightValid } }

    Scaffold(
        // TODO 버튼 클릭 시 트레이니 이름 입력 화면으로 이동
        topBar = { TnTTopBarWithBackButton(onBackClick = {}) },
        containerColor = TnTTheme.colors.commonColors.Common0,
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .verticalScroll(rememberScrollState()),
            ) {
                ProgressSteps(
                    currentStep = 2,
                    totalSteps = 4,
                    title = stringResource(R.string.enter_your_basic_info),
                    subTitle = stringResource(R.string.basic_info_for_pt),
                )
                Spacer(Modifier.padding(top = 48.dp))
                Text(
                    text = stringResource(R.string.birthday_placeholder),
                    color = TnTTheme.colors.neutralColors.Neutral900,
                    style = TnTTheme.typography.body1Bold,
                    modifier = Modifier.padding(start = 20.dp, bottom = 8.dp),
                )
                BirthdayPicker(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    today = today,
                    selectedDate = birthday,
                    onDateSelected = { birthday = it },
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = TnTTheme.colors.neutralColors.Neutral200,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
                Spacer(Modifier.padding(top = 48.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                ) {
                    TnTLabeledTextField(
                        title = stringResource(R.string.height_label),
                        value = height,
                        placeholder = "0",
                        isSingleLine = true,
                        isRequired = true,
                        keyboardType = KeyboardType.Number,
                        trailingComponent = {
                            UnitLabel(R.string.height_unit)
                        },
                        onValueChange = { newHeight ->
                            if (validateInput(newHeight)) {
                                height = newHeight
                            }
                        },
                        modifier = Modifier.weight(1f),
                    )
                    TnTLabeledTextField(
                        title = stringResource(R.string.weight_label),
                        value = weight,
                        placeholder = "00.0",
                        isSingleLine = true,
                        isRequired = true,
                        keyboardType = KeyboardType.Number,
                        trailingComponent = {
                            UnitLabel(R.string.weight_unit)
                        },
                        onValueChange = { newWeight ->
                            if (validateInput(newWeight)) {
                                weight = newWeight
                            }
                        },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
            // TODO 트레이니 PT 목적 화면으로 이동
            TnTBottomButton(
                text = stringResource(R.string.next),
                modifier = Modifier.align(Alignment.BottomCenter),
                enabled = isFormValid,
                onClick = { },
            )
        }
    }
}

@Composable
private fun BirthdayPicker(
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

/**
 * 유효한 입력값인지 검사 (정수 또는 실수 형식 확인)
 * 형식: 5자 이하의 정수 또는 실수
 */
private fun validateInput(input: String): Boolean {
    return input.isEmpty() || (input.toDoubleOrNull() != null && !input.startsWith("0") && input.length <= MAX_LENGTH)
}

@Preview(showBackground = true)
@Composable
private fun TraineeBasicInfoScreenPreview() {
    TnTTheme {
        TraineeBasicInfoScreen()
    }
}
