package co.kr.tnt.trainer.main

import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.component.bottombar.BottomTab
import co.kr.tnt.navigation.Route

internal enum class TrainerMainTab(
    override val contentDescription: String,
    override val icon: Int,
    val baseRoute: Route.TrainerMainTab,
    val route: Route,
) : BottomTab {
    HOME(
        contentDescription = "홈",
        icon = R.drawable.ic_navbar_home,
        baseRoute = Route.TrainerMainTab.Home,
        route = Route.TrainerHome,
    ),
    FEEDBACK(
        contentDescription = "피드백",
        icon = R.drawable.ic_navbar_feedback,
        baseRoute = Route.TrainerMainTab.Feedback,
        route = Route.TrainerFeedback,
    ),
    MEMBERS(
        contentDescription = "회원목록",
        icon = R.drawable.ic_navbar_members,
        baseRoute = Route.TrainerMainTab.Members,
        route = Route.TrainerMembers,
    ),
    MY_PAGE(
        contentDescription = "내 정보",
        icon = R.drawable.ic_navbar_mypage,
        baseRoute = Route.TrainerMainTab.MyPage,
        route = Route.TrainerMyPage,
    ),
}
