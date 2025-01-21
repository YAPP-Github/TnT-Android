package co.kr.tnt.signup.trainee.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.designsystem.theme.TnTTheme

@Composable
fun ProgressSteps(
    currentStep: Int,
    totalSteps: Int,
    title: String,
    modifier: Modifier = Modifier,
    subTitle: String? = null,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
    ) {
        StepIndicator(
            currentStep = currentStep,
            totalSteps = totalSteps,
        )
        Text(
            text = title,
            color = TnTTheme.colors.neutralColors.Neutral950,
            style = TnTTheme.typography.h2,
            modifier = Modifier.defaultMinSize(minHeight = 36.dp),
        )
        if (subTitle != null) {
            Text(
                text = subTitle,
                color = TnTTheme.colors.neutralColors.Neutral500,
                style = TnTTheme.typography.body1Medium,
            )
        }
    }
}

@Composable
private fun StepIndicator(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier,
) {
    val defaultBackgroundColor = TnTTheme.colors.neutralColors.Neutral200
    val defaultTextColor = TnTTheme.colors.neutralColors.Neutral400
    val activeBackgroundColor = TnTTheme.colors.neutralColors.Neutral800
    val activeTextColor = TnTTheme.colors.commonColors.Common0

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        for (step in 1..totalSteps) {
            val isActive = step == currentStep

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(22.dp)
                    .background(
                        color = if (isActive) activeBackgroundColor else defaultBackgroundColor,
                        shape = CircleShape,
                    ),
            ) {
                Text(
                    text = step.toString(),
                    color = if (isActive) activeTextColor else defaultTextColor,
                    style = TnTTheme.typography.label1Medium,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StepProgressHeaderPreview() {
    TnTTheme {
        ProgressSteps(
            currentStep = 3,
            totalSteps = 4,
            title = "PT를 받는 목적에 대해\n알려주세요!",
            subTitle = "다중 선택이 가능해요.",
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
