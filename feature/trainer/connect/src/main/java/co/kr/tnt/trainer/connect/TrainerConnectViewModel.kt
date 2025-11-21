package co.kr.tnt.trainer.connect

import androidx.lifecycle.viewModelScope
import co.kr.tnt.core.ui.R.string.core_failed_to_server_request
import co.kr.tnt.domain.model.User
import co.kr.tnt.domain.model.trainer.TrainerManagementMemberCount
import co.kr.tnt.domain.repository.ConnectRepository
import co.kr.tnt.trainer.connect.TrainerConnectContract.TrainerConnectPage
import co.kr.tnt.trainer.connect.TrainerConnectContract.TrainerConnectSideEffect
import co.kr.tnt.trainer.connect.TrainerConnectContract.TrainerConnectUiEvent
import co.kr.tnt.trainer.connect.TrainerConnectContract.TrainerConnectUiState
import co.kr.tnt.ui.base.BaseViewModel
import co.kr.tnt.ui.resource.DisplayText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TrainerConnectViewModel @Inject constructor(
    private val connectRepository: ConnectRepository,
) : BaseViewModel<TrainerConnectUiState, TrainerConnectUiEvent, TrainerConnectSideEffect>(
        TrainerConnectUiState(),
    ) {
    override suspend fun handleEvent(event: TrainerConnectUiEvent) {
        when (event) {
            is TrainerConnectUiEvent.OnFetchInitialData -> initProfile(
                event.trainerId,
                event.traineeId,
            )

            TrainerConnectUiEvent.OnClickNext -> navigateToNext()
            TrainerConnectUiEvent.OnClickBack -> navigateToBack()
        }
    }

    private fun initProfile(trainerId: String, traineeId: String) {
        viewModelScope.launch {
            runCatching {
                connectRepository.getConnectedTraineeInfo(
                    trainerId = trainerId,
                    traineeId = traineeId,
                )
            }.onSuccess { result ->
                updateState {
                    copy(
                        trainerState = User.Trainer(
                            id = "",
                            name = result.trainerName,
                            image = result.trainerImage,
                            memberCounts = TrainerManagementMemberCount.ZERO,
                        ),
                        traineeState = User.Trainee(
                            id = "",
                            name = result.traineeName,
                            image = result.traineeImage,
                            birthday = null,
                            age = result.age,
                            weight = result.weight,
                            height = result.height?.toInt(),
                            ptPurpose = result.ptGoals,
                            caution = result.cautionNote,
                            isConnected = true,
                        ),
                    )
                }
            }.onFailure {
                sendEffect(TrainerConnectSideEffect.ShowToast(DisplayText.Resource(core_failed_to_server_request)))
            }
        }
    }

    private fun navigateToNext() {
        if (currentState.page == TrainerConnectPage.lastPage) {
            sendEffect(TrainerConnectSideEffect.NavigateToHome)
            return
        }
        updateState { copy(page = TrainerConnectPage.getNextPage(currentState.page)) }
    }

    private fun navigateToBack() {
        if (currentState.page == TrainerConnectPage.firstPage) {
            sendEffect(TrainerConnectSideEffect.NavigateToBack)
            return
        }

        updateState { copy(page = TrainerConnectPage.getPreviousPage(currentState.page)) }
    }
}
