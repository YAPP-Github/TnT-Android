package co.kr.tnt.trainer.main

import co.kr.tnt.navigation.Route

internal enum class TrainerMainTab(
    internal val contentDescription: String,
    val route: Route.TrainerMainTab,
) {
    HOME(
        contentDescription = "홈",
        Route.TrainerMainTab.Home,
    ),
    FEEDBACK(
        contentDescription = "피드백",
        Route.TrainerMainTab.Feedback,
    ),
    MEMBERS(
        contentDescription = "회원 목록",
        Route.TrainerMainTab.Members,
    ),
    MY_PAGE(
        contentDescription = "내 정보",
        Route.TrainerMainTab.MyPage,
    ),
}
