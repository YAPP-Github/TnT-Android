package co.kr.tnt.trainee.main

import co.kr.tnt.navigation.Route

internal enum class TraineeMainTab(
    internal val contentDescription: String,
    val route: Route.TraineeMainTab,
) {
    HOME(
        contentDescription = "홈",
        Route.TraineeMainTab.Home,
    ),
    MY_PAGE(
        contentDescription = "내 정보",
        Route.TraineeMainTab.MyPage,
    ),
}
