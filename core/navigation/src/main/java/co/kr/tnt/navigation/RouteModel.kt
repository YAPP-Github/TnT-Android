package co.kr.tnt.navigation

import kotlinx.serialization.Serializable

// TODO Route 정리
sealed interface Route {
    @Serializable
    data object HomeBase : Route

    @Serializable
    data object Home : Route

    @Serializable
    data object Login : Route

    @Serializable
    data class RoleSelection(
        val authId: String,
        val authType: String,
        val email: String,
    ) : Route

    @Serializable
    data class TrainerSignUp(
        val authId: String,
        val authType: String,
        val email: String,
    ) : Route

    @Serializable
    data class TraineeSignUp(
        val authId: String,
        val authType: String,
        val email: String,
    ) : Route

    @Serializable
    data class TrainerInvite(val isSkippable: Boolean) : Route

    @Serializable
    data class TrainerConnect(
        val trainerId: String,
        val traineeId: String,
    ) : Route

    @Serializable
    data class TraineeConnect(val isSkippable: Boolean) : Route

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
    data object TraineeMain : Route

    sealed interface TraineeMainTab : Route {
        @Serializable
        data object Home : TraineeMainTab

        @Serializable
        data object MyPage : TraineeMainTab
    }

    @Serializable
    data object TraineeHome : Route

    @Serializable
    data object TraineeMyPage : Route

    @Serializable
    data object TraineeNotification : Route

    @Serializable
    data object TrainerNotification : Route

    @Serializable
    data object AddPtSession : Route

    @Serializable
    data object TraineeMealRecord : Route

    @Serializable
    data class TraineeMealRecordDetail(val id: Long) : Route

    @Serializable
    data class WebView(val url: String) : Route
}
