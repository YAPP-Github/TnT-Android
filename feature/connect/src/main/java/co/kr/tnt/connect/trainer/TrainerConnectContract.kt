import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState

internal class TrainerConnectContract {
    data class TrainerConnectUiState(
        val page: TrainerConnectPage = TrainerConnectPage.CodeGeneration,
        val inviteCode: String? = "",
    ) : UiState

    sealed interface TrainerConnectUiEvent : UiEvent {
        data object OnRegenerateClick : TrainerConnectUiEvent
        data object OnSkipClick : TrainerConnectUiEvent
    }

    sealed interface TrainerConnectSideEffect : UiSideEffect {
        data object NavigateToBack : TrainerConnectSideEffect
        data object NavigateToHome : TrainerConnectSideEffect
    }

    enum class TrainerConnectPage {
        CodeGeneration,
    }
}
