package co.kr.tnt.trainee.signup

import android.app.DatePickerDialog
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.core.ui.R.string.core_entered_wrong_text
import co.kr.tnt.core.ui.R.string.core_height_label
import co.kr.tnt.core.ui.R.string.core_height_unit
import co.kr.tnt.core.ui.R.string.core_next
import co.kr.tnt.core.ui.R.string.core_weight_label
import co.kr.tnt.core.ui.R.string.core_weight_unit
import co.kr.tnt.designsystem.component.TnTTopBarWithBackButton
import co.kr.tnt.designsystem.component.button.TnTBottomButton
import co.kr.tnt.designsystem.component.textfield.TnTLabeledTextField2
import co.kr.tnt.designsystem.component.textfield.TnTSelectableLabeledTextField2
import co.kr.tnt.designsystem.component.textfield.model.TnTTextFieldSize
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.feature.trainee.signup.R
import co.kr.tnt.trainee.signup.TraineeSignUpContract.TraineeSignUpUiState
import co.kr.tnt.trainee.signup.component.ProgressSteps
import co.kr.tnt.ui.extensions.clearFocusOnTap
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
internal fun TraineeBasicInfoPage(
    state: TraineeSignUpUiState,
    onChangeHeight: (String) -> Unit,
    onChangeWeight: (String) -> Unit,
    onChangeBirthday: (LocalDate) -> Unit,
    onClickBack: () -> Unit,
    onClickNext: () -> Unit,
) {
    BackHandler { onClickBack() }

    val context = LocalContext.current
    val today = LocalDate.now()

    Scaffold(
        topBar = { TnTTopBarWithBackButton(onBackClick = onClickBack) },
        containerColor = TnTTheme.colors.commonColors.Common0,
        modifier = Modifier.clearFocusOnTap(),
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(innerPadding)
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
                TnTSelectableLabeledTextField2(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    value = state.birthday?.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                        ?: stringResource(R.string.birthday_placeholder),
                    onClickTextField = {
                        DatePickerDialog(
                            context,
                            { _, selectedYear, selectedMonth, selectedDay ->
                                val newDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
                                onChangeBirthday(newDate)
                            },
                            state.birthday?.year ?: today.year,
                            (state.birthday?.monthValue?.minus(1)) ?: (today.monthValue - 1),
                            state.birthday?.dayOfMonth ?: today.dayOfMonth,
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
                    title = stringResource(R.string.birthday_label),
                    size = TnTTextFieldSize.SMALL,
                )
                Spacer(Modifier.padding(top = 48.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                ) {
                    TnTLabeledTextField2(
                        title = stringResource(core_height_label),
                        value = state.height ?: "",
                        placeholder = "0",
                        isWarning = state.isHeightValid.not(),
                        warningMessage = stringResource(core_entered_wrong_text),
                        keyboardType = KeyboardType.Number,
                        trailing = {
                            UnitLabel(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                stringResId = core_height_unit,
                            )
                        },
                        onValueChange = onChangeHeight,
                        modifier = Modifier.weight(1f),
                    )
                    TnTLabeledTextField2(
                        title = stringResource(core_weight_label),
                        value = state.weight ?: "",
                        placeholder = "00.0",
                        isWarning = state.isWeightValid.not(),
                        warningMessage = stringResource(core_entered_wrong_text),
                        keyboardType = KeyboardType.Number,
                        trailing = {
                            UnitLabel(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                stringResId = core_weight_unit,
                            )
                        },
                        onValueChange = onChangeWeight,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
            TnTBottomButton(
                text = stringResource(core_next),
                modifier = Modifier.align(Alignment.BottomCenter),
                enabled = state.isBasicInfoValid,
                onClick = onClickNext,
            )
        }
    }
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

@Preview(showBackground = true)
@Composable
private fun TraineeBasicInfoPagePreview() {
    TnTTheme {
        TraineeBasicInfoPage(
            state = TraineeSignUpUiState(),
            onClickBack = {},
            onClickNext = {},
            onChangeHeight = {},
            onChangeWeight = {},
            onChangeBirthday = {},
        )
    }
}
