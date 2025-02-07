package co.kr.tnt.trainee.main

import co.kr.tnt.core.designsystem.R
import co.kr.tnt.navigation.Route

internal enum class TraineeMainTab(
    internal val contentDescription: String,
    internal val icon: Int,
    val route: Route.TraineeMainTab,
) {
    HOME(
        contentDescription = "홈",
        icon = R.drawable.ic_navbar_home,
        route = Route.TraineeMainTab.Home,
    ),
    MY_PAGE(
        contentDescription = "내 정보",
        icon = R.drawable.ic_navbar_mypage,
        route = Route.TraineeMainTab.MyPage,
    ),
}
