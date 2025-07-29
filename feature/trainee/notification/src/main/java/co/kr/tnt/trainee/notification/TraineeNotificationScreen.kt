package co.kr.tnt.trainee.notification

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.designsystem.component.TnTTopBarWithBackButton
import co.kr.tnt.designsystem.component.notification.TnTNotification
import co.kr.tnt.designsystem.component.notification.model.NotificationIcon
import co.kr.tnt.designsystem.snackbar.LocalSnackbar
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.trainee.notification.TraineeNotificationContract.TraineeNotificationUiEvent
import co.kr.tnt.trainee.notification.TraineeNotificationContract.TraineeNotificationUiState
import co.kr.tnt.ui.model.NotificationState
import co.kr.tnt.core.ui.R as uiResource

@Composable
internal fun TraineeNotificationRoute(
    navigateToPrevious: () -> Unit,
    viewModel: TraineeNotificationViewModel = hiltViewModel(),
) {
    val snackbar = LocalSnackbar.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TraineeNotificationScreen(
        state = uiState,
        onClickBack = { viewModel.setEvent(TraineeNotificationUiEvent.OnClickBack) },
    )

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                TraineeNotificationContract.TraineeNotificationEffect.NavigateToPrevious -> navigateToPrevious()
                is TraineeNotificationContract.TraineeNotificationEffect.ShowToast -> snackbar.show(effect.message)
            }
        }
    }
}

@Composable
private fun TraineeNotificationScreen(
    state: TraineeNotificationUiState,
    onClickBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TnTTopBarWithBackButton(
                title = stringResource(uiResource.string.notification),
                onBackClick = onClickBack,
                showStoke = true,
            )
        },
        containerColor = TnTTheme.colors.commonColors.Common0,
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            if (state.notifications.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(uiResource.string.no_recent_notifications),
                        style = TnTTheme.typography.label1Medium,
                        color = TnTTheme.colors.neutralColors.Neutral400,
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(state.notifications) { notification ->
                        TnTNotification(
                            type = notification.type,
                            title = notification.title,
                            contents = notification.contents,
                            time = notification.time,
                            isChecked = notification.isChecked,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TraineeNotificationScreenPreview() {
    TnTTheme {
        val sampleNotifications = listOf(
            NotificationState(
                type = NotificationIcon.LINK,
                title = "트레이너 연결 해제",
                contents = "박헬린 트레이너가 연결을 끊었어요",
                time = "3분 전",
                isChecked = false,
            ),
            NotificationState(
                type = NotificationIcon.LINK,
                title = "트레이너 연결 해제",
                contents = "김헬스 트레이너가 연결을 끊었어요",
                time = "12시간 전",
                isChecked = false,
            ),
            NotificationState(
                type = NotificationIcon.LINK,
                title = "트레이너 연결 해제",
                contents = "김피티 트레이너가 연결을 끊었어요",
                time = "2025/01/01",
                isChecked = true,
            ),
        )
        TraineeNotificationScreen(
            onClickBack = {},
            state = TraineeNotificationUiState(notifications = sampleNotifications),
        )
    }
}
