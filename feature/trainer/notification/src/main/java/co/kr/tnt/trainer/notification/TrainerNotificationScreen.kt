package co.kr.tnt.trainer.notification

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.designsystem.component.TnTTopBarWithBackButton
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.trainer.notification.TrainerNotificationContract.TrainerNotificationEffect
import co.kr.tnt.trainer.notification.TrainerNotificationContract.TrainerNotificationUiEvent
import co.kr.tnt.trainer.notification.TrainerNotificationContract.TrainerNotificationUiState
import co.kr.tnt.core.ui.R as uiResource

@Composable
internal fun TrainerNotificationRoute(
    navigateToPrevious: () -> Unit,
    navigateToConnect: () -> Unit,
    viewModel: TrainerNotificationViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TrainerNotificationScreen(
        state = uiState,
        onBackClick = { viewModel.setEvent(TrainerNotificationUiEvent.OnBackClick) },
        onLinkNotificationClick = { viewModel.setEvent(TrainerNotificationUiEvent.OnLinkNotificationClick) },
    )

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                TrainerNotificationEffect.NavigateToPrevious -> navigateToPrevious()
                TrainerNotificationEffect.NavigateToConnect -> navigateToConnect()
                is TrainerNotificationEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@Composable
private fun TrainerNotificationScreen(
    state: TrainerNotificationUiState,
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
            state = TrainerNotificationUiState(),
            onBackClick = {},
            onLinkNotificationClick = {},
        )
    }
}
