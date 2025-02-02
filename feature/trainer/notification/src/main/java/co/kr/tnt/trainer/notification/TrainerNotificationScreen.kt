package co.kr.tnt.trainer.notification

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import co.kr.tnt.designsystem.component.TnTTopBarWithBackButton
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.core.ui.R as uiResource

@Composable
internal fun TrainerNotificationRoute(
    navigateToPrevious: () -> Unit,
    navigateToConnect: () -> Unit,
    viewModel: TrainerNotificationViewModel = hiltViewModel(),
) {
    TrainerNotificationScreen(
        onBackClick = navigateToPrevious,
        onLinkNotificationClick = navigateToConnect,
    )
}

@Composable
private fun TrainerNotificationScreen(
    onBackClick: () -> Unit,
    onLinkNotificationClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TnTTopBarWithBackButton(
                title = stringResource(uiResource.string.notification),
                onBackClick = onBackClick,
            )
        },
        containerColor = TnTTheme.colors.commonColors.Common0,
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(uiResource.string.no_recent_notifications),
                    style = TnTTheme.typography.label1Medium,
                    color = TnTTheme.colors.neutralColors.Neutral400,
                    modifier = Modifier.clickable {
                        onLinkNotificationClick()
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun TrainerNotificationScreenPreview() {
    TnTTheme {
        TrainerNotificationScreen(
            onBackClick = {},
            onLinkNotificationClick = {},
        )
    }
}
