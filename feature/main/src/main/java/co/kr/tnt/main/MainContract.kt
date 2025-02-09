package co.kr.tnt.main

import co.kr.tnt.navigation.Route
import co.kr.tnt.ui.base.UiEvent
import co.kr.tnt.ui.base.UiSideEffect
import co.kr.tnt.ui.base.UiState

internal class MainContract {
    data class MainUiState(
        val showSplash: Boolean = true,
        val startDestination: Route = Route.Login,
    ) : UiState

    sealed class MainUiEvent : UiEvent {
        data object OnNotificationPermissionRevoked : MainUiEvent()
    }

    data object MainSideEffect : UiSideEffect
}
