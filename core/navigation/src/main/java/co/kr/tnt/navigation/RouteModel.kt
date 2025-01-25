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
    data object TrainerConnect : Route

    @Serializable
    data object TraineeConnect : Route
}
