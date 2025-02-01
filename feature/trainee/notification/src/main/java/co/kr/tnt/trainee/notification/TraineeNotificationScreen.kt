package co.kr.tnt.trainee.notification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import co.kr.tnt.designsystem.component.TnTTopBarWithBackButton
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.core.ui.R as uiResource

@Composable
internal fun TraineeNotificationRoute(
    navigateToPrevious: () -> Unit,
) {
    TraineeNotificationScreen(
        navigateToPrevious = navigateToPrevious,
    )
}

@Composable
fun TraineeNotificationScreen(
    navigateToPrevious: () -> Unit,
) {
    Scaffold(
        topBar = {
            TnTTopBarWithBackButton(
                title = stringResource(uiResource.string.notification),
                onBackClick = navigateToPrevious,
            )
        },
        contentColor = TnTTheme.colors.commonColors.Common0,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Text(text = "text")
        }
    }
}

@Preview
@Composable
private fun TraineeNotificationScreenPreview() {
    TnTTheme {
        TraineeNotificationScreen(
            navigateToPrevious = {},
        )
    }
}
