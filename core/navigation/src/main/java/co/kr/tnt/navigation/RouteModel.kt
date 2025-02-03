package co.kr.tnt.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object HomeBase : Route

    @Serializable
    data object Home : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object SignUpBaseRoute : Route

    @Serializable
    sealed interface SignUpBase : Route {
        @Serializable
        data class RoleSelection(
            val authId: String,
            val authType: String,
            val email: String,
        ) : SignUpBase {
            companion object {
                val DEFAULT = RoleSelection(
                    authId = "",
                    authType = "",
                    email = "",
                )
            }
        }

        @Serializable
        data class TrainerSignUp(
            val authId: String,
            val authType: String,
            val email: String,
        ) : SignUpBase

        @Serializable
        data class TraineeSignUp(
            val authId: String,
            val authType: String,
            val email: String,
        ) : SignUpBase
    }

    @Serializable
    data class TrainerConnect(val isFromMyPage: Boolean) : Route

    @Serializable
    data class TraineeConnect(val isFromMyPage: Boolean) : Route

    @Serializable
    data object TrainerMain : Route

    sealed interface TrainerMainTab : Route {
        @Serializable
        data object Home : TrainerMainTab

        @Serializable
        data object Members : TrainerMainTab

        @Serializable
        data object Feedback : TrainerMainTab

        @Serializable
        data object MyPage : TrainerMainTab
    }

    @Serializable
    data object TrainerHome : Route

    @Serializable
    data object TrainerMembers : Route

    @Serializable
    data object TrainerFeedback : Route

    @Serializable
    data object TrainerMyPage : Route

    @Serializable
    data object TraineeMyPage : Route

    @Serializable
    data class WebView(val url: String) : Route
}
