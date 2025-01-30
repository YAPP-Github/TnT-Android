package co.kr.tnt.trainee.mypage

import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState

internal class TraineeMyPageContract {
    data class TraineeMyPageUiState(
        val image: String? = "",
        val name: String = "",
        val isConnected: Boolean = false,
        val isPushEnabled: Boolean = false,
        val appVersion: String = "",
    ) : UiState

    sealed interface TraineeMyPageUiEvent : UiEvent {
        data object OnConnectClick : TraineeMyPageUiEvent
        data object ToggleNotification : TraineeMyPageUiEvent
    }

    sealed interface TraineeMyPageEffect : UiSideEffect {
        data class ShowToast(val message: String) : TraineeMyPageEffect
        data object NavigateToConnect : TraineeMyPageEffect
    }
}
