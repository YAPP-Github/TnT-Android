package co.kr.tnt.trainer.notification

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
import co.kr.tnt.trainer.notification.TrainerNotificationContract.TrainerNotificationEffect
import co.kr.tnt.trainer.notification.TrainerNotificationContract.TrainerNotificationUiEvent
import co.kr.tnt.trainer.notification.TrainerNotificationContract.TrainerNotificationUiState
import co.kr.tnt.ui.model.NotificationState
import co.kr.tnt.core.ui.R as uiResource

@Composable
internal fun TrainerNotificationRoute(
    navigateToPrevious: () -> Unit,
    navigateToConnect: (trainerId: String, traineeId: String) -> Unit,
    viewModel: TrainerNotificationViewModel = hiltViewModel(),
) {
    val snackbar = LocalSnackbar.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TrainerNotificationScreen(
        state = uiState,
        onClickBack = { viewModel.setEvent(TrainerNotificationUiEvent.OnClickBack) },
        onLinkNotificationClick = { viewModel.setEvent(TrainerNotificationUiEvent.OnLinkNotificationClick) },
    )

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                TrainerNotificationEffect.NavigateToPrevious -> navigateToPrevious()
                TrainerNotificationEffect.NavigateToConnect -> navigateToConnect(
                    uiState.trainerId,
                    uiState.traineeId,
                )

                is TrainerNotificationEffect.ShowToast -> snackbar.show(effect.message)
            }
        }
    }
}

@Composable
private fun TrainerNotificationScreen(
    state: TrainerNotificationUiState,
    onClickBack: () -> Unit,
    onLinkNotificationClick: () -> Unit,
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
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
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
                            onClick = if (notification.isClickable) {
                                onLinkNotificationClick
                            } else {
                                null
                            },
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TrainerNotificationScreenPreview() {
    TnTTheme {
        val sampleNotifications = listOf(
            NotificationState(
                type = NotificationIcon.LINK,
                title = "트레이니 연결 완료",
                contents = "김회원 회원과 연결되었어요",
                time = "방금",
                isChecked = false,
            ),
            NotificationState(
                type = NotificationIcon.LINK,
                title = "트레이니 연결 완료",
                contents = "김돌돌 회원과 연결되었어요",
                time = "4분 전",
                isChecked = false,
            ),
            NotificationState(
                type = NotificationIcon.LINK,
                title = "트레이니 연결 해제",
                contents = "박헬린 회원이 연결을 끊었어요",
                time = "2025/01/01",
                isChecked = true,
            ),
        )
        TrainerNotificationScreen(
            onClickBack = {},
            onLinkNotificationClick = {},
            state = TrainerNotificationUiState(notifications = sampleNotifications),
        )
    }
}
