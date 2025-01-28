package co.kr.tnt.trainee.signup

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.designsystem.component.TnTTopBarWithBackButton
import co.kr.tnt.designsystem.component.button.TnTBottomButton
import co.kr.tnt.designsystem.component.button.TnTTextButton
import co.kr.tnt.designsystem.component.button.model.ButtonSize
import co.kr.tnt.designsystem.component.button.model.ButtonType
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.feature.trainee.signup.R
import co.kr.tnt.trainee.signup.TraineeSignUpContract.TraineeSignUpUiState
import co.kr.tnt.trainee.signup.component.ProgressSteps
import co.kr.tnt.trainee.signup.model.PTPurpose
import co.kr.tnt.core.ui.R as uiResource

private const val ROW_NUM = 3
private const val COLUMNS_NUM = 2

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun TraineePTPurposePage(
    state: TraineeSignUpUiState,
    onPurposeSelected: (String) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    BackHandler { onBackClick() }

    Scaffold(
        topBar = { TnTTopBarWithBackButton(onBackClick = onBackClick) },
        containerColor = TnTTheme.colors.commonColors.Common0,
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(modifier = Modifier.fillMaxSize()) {
                ProgressSteps(
                    currentStep = 3,
                    totalSteps = 4,
                    title = stringResource(R.string.purpose_of_pt),
                    subTitle = stringResource(R.string.multiple_choices_available),
                )
                Spacer(Modifier.padding(top = 32.dp))
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        maxItemsInEachRow = COLUMNS_NUM,
                        maxLines = ROW_NUM,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        PTPurpose.entries.forEach { purpose ->
                            val purposeText = stringResource(purpose.textResId)
                            PurposeButton(
                                text = purposeText,
                                isSelected = purposeText in state.ptPurpose,
                                onClick = { onPurposeSelected(purposeText) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }
            }
            TnTBottomButton(
                text = stringResource(uiResource.string.next),
                onClick = onNextClick,
                enabled = state.ptPurpose.isNotEmpty(),
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
fun PurposeButton(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    TnTTextButton(
        text = text,
        modifier = modifier,
        size = ButtonSize.XLarge,
        type = if (isSelected) ButtonType.RedOutline else ButtonType.GrayOutline,
        onClick = onClick,
    )
}

@Preview(showBackground = true)
@Composable
private fun TraineePTPurposePagePreview() {
    TnTTheme {
        TraineePTPurposePage(
            state = TraineeSignUpUiState(),
            onBackClick = {},
            onNextClick = {},
            onPurposeSelected = {},
        )
    }
}
