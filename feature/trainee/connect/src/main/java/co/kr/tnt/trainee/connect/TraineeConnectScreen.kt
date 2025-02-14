package co.kr.tnt.trainee.connect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.designsystem.snackbar.LocalSnackbar
import co.kr.tnt.navigation.model.ConnectScreenMode
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectPage
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectSideEffect
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectUiEvent
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectUiState
import java.time.LocalDate

@Composable
internal fun TraineeConnectRoute(
    screenMode: ConnectScreenMode,
    navigateToPrevious: () -> Unit,
    navigateToHome: (Boolean) -> Unit,
    viewModel: TraineeConnectViewModel = hiltViewModel(),
) {
    val snackbar = LocalSnackbar.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    TraineeConnectScreen(
        state = state,
        screenMode = screenMode,
        onBackClick = { viewModel.setEvent(TraineeConnectUiEvent.OnChangeDialogState) },
        onNextClick = { viewModel.setEvent(TraineeConnectUiEvent.OnNextClick) },
        onSkipClick = { viewModel.setEvent(TraineeConnectUiEvent.OnSkipClick) },
        onChangeInviteCode = { code ->
            viewModel.setEvent(TraineeConnectUiEvent.OnChangeInviteCode(code))
        },
        onCodeValidationClick = { code ->
            viewModel.setEvent(TraineeConnectUiEvent.OnCodeValidateClick(code))
        },
        onCancelConnectClick = { viewModel.setEvent(TraineeConnectUiEvent.OnBackClick) },
        onDismissPopUp = { viewModel.setEvent(TraineeConnectUiEvent.OnChangeDialogState) },
        onChangeSessionStartDate = { date ->
            viewModel.setEvent(TraineeConnectUiEvent.OnChangeSessionStartDate(date))
        },
        onChangeCompletedSessionCount = { count ->
            viewModel.setEvent(TraineeConnectUiEvent.OnChangeCompletedSessionCount(count))
        },
        onChangeTotalSessionCount = { count ->
            viewModel.setEvent(TraineeConnectUiEvent.OnChangeTotalSessionCount(count))
        },
    )

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                TraineeConnectSideEffect.NavigateToBack -> navigateToPrevious()
                TraineeConnectSideEffect.NavigateToHome -> navigateToHome(false)
                is TraineeConnectSideEffect.ShowToast -> snackbar.show(effect.message)
            }
        }
    }
}

@Composable
private fun TraineeConnectScreen(
    state: TraineeConnectUiState,
    screenMode: ConnectScreenMode,
    onChangeInviteCode: (code: String) -> Unit,
    onCodeValidationClick: (code: String) -> Unit,
    onCancelConnectClick: () -> Unit,
    onDismissPopUp: () -> Unit,
    onChangeSessionStartDate: (date: LocalDate) -> Unit,
    onChangeCompletedSessionCount: (count: String) -> Unit,
    onChangeTotalSessionCount: (count: String) -> Unit,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    onSkipClick: () -> Unit,
) {
    when (state.page) {
        TraineeConnectPage.CodeEntry -> CodeEntryPage(
            showDialog = state.showDialog,
            inputState = state.inviteCodeInputState,
            inviteCode = state.inviteCode,
            screenMode = screenMode,
            onNextClick = onNextClick,
            onBackClick = onBackClick,
            onSkipClick = onSkipClick,
            onChangeInviteCode = onChangeInviteCode,
            onValidateClick = onCodeValidationClick,
            onCancelClick = onCancelConnectClick,
            onDismissPopup = onDismissPopUp,
        )

        TraineeConnectPage.PTSessionForm -> PTSessionFormPage(
            trainerName = state.trainerName,
            sessionStartDate = state.sessionStartDate,
            completedSessionCount = state.completedSessionCount,
            totalSessionCount = state.totalSessionCount,
            isLoading = state.isLoading,
            onChangeSessionStartDate = onChangeSessionStartDate,
            onChangeCompletedSessionCount = onChangeCompletedSessionCount,
            onChangeTotalSessionCount = onChangeTotalSessionCount,
            onNextClick = onNextClick,
            onBackClick = onBackClick,
        )

        TraineeConnectPage.TraineeConnectComplete -> TraineeConnectCompletePage(
            trainerName = state.trainerName,
            trainerImage = state.trainerImage,
            traineeName = state.traineeName,
            traineeImage = state.traineeImage,
            onNextClick = onNextClick,
            onBackClick = onBackClick,
        )
    }
}
