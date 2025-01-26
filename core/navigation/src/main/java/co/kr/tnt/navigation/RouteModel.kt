package co.kr.tnt.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object HomeBase : Route

    @Serializable
    data class Home(val isTrainer: Boolean) : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object TrainerSignUp : Route

    @Serializable
    data object TraineeSignUp : Route

    @Serializable
    data class TrainerConnect(val isFromMyPage: Boolean) : Route

    @Serializable
    data class TraineeConnect(val isFromMyPage: Boolean) : Route

    @Serializable
    data class RoleSelection(
        val authId: String,
        val authType: String,
        val email: String,
    ) : Route
}
