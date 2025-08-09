package co.kr.tnt.trainee.connect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.designsystem.snackbar.LocalSnackbar
import co.kr.tnt.navigation.model.ScreenMode
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectPage
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectSideEffect
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectUiEvent
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectUiState
import java.time.LocalDate

@Composable
internal fun TraineeConnectRoute(
    screenMode: ScreenMode,
    navigateToPrevious: () -> Unit,
    navigateToHome: (Boolean) -> Unit,
    viewModel: TraineeConnectViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val snackbar = LocalSnackbar.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    TraineeConnectScreen(
        state = state,
        screenMode = screenMode,
        onClickBack = { viewModel.setEvent(TraineeConnectUiEvent.OnChangeDialogState) },
        onClickNext = { viewModel.setEvent(TraineeConnectUiEvent.OnClickNext) },
        onClickSkip = { viewModel.setEvent(TraineeConnectUiEvent.OnClickSkip) },
        onChangeInviteCode = { code ->
            viewModel.setEvent(TraineeConnectUiEvent.OnChangeInviteCode(code))
        },
        onClickValidateCode = { code ->
            viewModel.setEvent(TraineeConnectUiEvent.OnClickValidateCode(code))
        },
        onClickCancelConnection = { viewModel.setEvent(TraineeConnectUiEvent.OnClickBack) },
        onDismissDialog = { viewModel.setEvent(TraineeConnectUiEvent.OnChangeDialogState) },
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
                is TraineeConnectSideEffect.ShowToast -> snackbar.show(effect.message.asString(context))
            }
        }
    }
}

@Composable
private fun TraineeConnectScreen(
    state: TraineeConnectUiState,
    screenMode: ScreenMode,
    onChangeInviteCode: (code: String) -> Unit,
    onClickValidateCode: (code: String) -> Unit,
    onClickCancelConnection: () -> Unit,
    onDismissDialog: () -> Unit,
    onChangeSessionStartDate: (date: LocalDate) -> Unit,
    onChangeCompletedSessionCount: (count: String) -> Unit,
    onChangeTotalSessionCount: (count: String) -> Unit,
    onClickNext: () -> Unit,
    onClickBack: () -> Unit,
    onClickSkip: () -> Unit,
) {
    when (state.page) {
        TraineeConnectPage.CodeEntry -> CodeEntryPage(
            showDialog = state.showDialog,
            inputState = state.inviteCodeInputState,
            inviteCode = state.inviteCode,
            screenMode = screenMode,
            onClickNext = onClickNext,
            onClickBack = onClickBack,
            onClickSkip = onClickSkip,
            onChangeInviteCode = onChangeInviteCode,
            onClickValidate = onClickValidateCode,
            onClickCancel = onClickCancelConnection,
            onDismissDialog = onDismissDialog,
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
            onClickNext = onClickNext,
            onClickBack = onClickBack,
        )

        TraineeConnectPage.TraineeConnectComplete -> TraineeConnectCompletePage(
            trainerName = state.trainerName,
            trainerImage = state.trainerImage,
            traineeName = state.traineeName,
            traineeImage = state.traineeImage,
            onClickNext = onClickNext,
            onClickBack = onClickBack,
        )
    }
}
