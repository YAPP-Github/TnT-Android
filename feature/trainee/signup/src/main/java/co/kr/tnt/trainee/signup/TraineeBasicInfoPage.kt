package co.kr.tnt.trainee.signup

import android.app.DatePickerDialog
import android.content.Context
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
import co.kr.tnt.feature.trainee.signup.R
import co.kr.tnt.trainee.signup.TraineeSignUpContract.TraineeSignUpUiState
import co.kr.tnt.trainee.signup.component.ProgressSteps
import co.kr.tnt.ui.extensions.clearFocusOnTap
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import co.kr.tnt.core.ui.R as uiResource

@Composable
internal fun TraineeBasicInfoPage(
    state: TraineeSignUpUiState,
    onHeightChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
    onBirthdayChange: (LocalDate) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    BackHandler { onBackClick() }

    val context = LocalContext.current
    val today = LocalDate.now()

    Scaffold(
        topBar = { TnTTopBarWithBackButton(onBackClick = onBackClick) },
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
                ProgressSteps(
                    currentStep = 2,
                    totalSteps = 4,
                    title = stringResource(R.string.enter_your_basic_info),
                    subTitle = stringResource(R.string.basic_info_for_trainer),
                )
                Spacer(Modifier.padding(top = 48.dp))
                Text(
                    text = stringResource(R.string.birthday_label),
                    color = TnTTheme.colors.neutralColors.Neutral900,
                    style = TnTTheme.typography.body1Bold,
                    modifier = Modifier.padding(start = 20.dp, bottom = 8.dp),
                )
                BirthdayPicker(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    context = context,
                    today = today,
                    selectedDate = state.birthday,
                    onDateSelected = onBirthdayChange,
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
                        title = stringResource(uiResource.string.height_label),
                        value = state.height ?: "",
                        placeholder = "0",
                        isSingleLine = true,
                        showWarning = state.isHeightValid.not(),
                        warningMessage = stringResource(uiResource.string.entered_wrong_text),
                        keyboardType = KeyboardType.Number,
                        trailingComponent = {
                            UnitLabel(uiResource.string.height_unit)
                        },
                        onValueChange = onHeightChange,
                        modifier = Modifier.weight(1f),
                    )
                    TnTLabeledTextField(
                        title = stringResource(uiResource.string.weight_label),
                        value = state.weight ?: "",
                        placeholder = "00.0",
                        isSingleLine = true,
                        showWarning = state.isWeightValid.not(),
                        warningMessage = stringResource(uiResource.string.entered_wrong_text),
                        keyboardType = KeyboardType.Number,
                        trailingComponent = {
                            UnitLabel(uiResource.string.weight_unit)
                        },
                        onValueChange = onWeightChange,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
            TnTBottomButton(
                text = stringResource(uiResource.string.next),
                modifier = Modifier.align(Alignment.BottomCenter),
                enabled = state.isBasicInfoValid,
                onClick = onNextClick,
            )
        }
    }
}

@Composable
private fun BirthdayPicker(
    modifier: Modifier = Modifier,
    context: Context,
    today: LocalDate,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
) {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    val date = selectedDate ?: LocalDate.of(2001, 1, 1)

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
                        val todayMillis = today
                            .atStartOfDay(ZoneId.systemDefault())
                            .toInstant()
                            .toEpochMilli()

                        datePicker.maxDate = todayMillis - 1
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

@Preview(showBackground = true)
@Composable
private fun TraineeBasicInfoPagePreview() {
    TnTTheme {
        TraineeBasicInfoPage(
            state = TraineeSignUpUiState(),
            onBackClick = {},
            onNextClick = {},
            onHeightChange = {},
            onWeightChange = {},
            onBirthdayChange = {},
        )
    }
}
