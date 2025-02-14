package co.kr.tnt.trainer.main

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.component.bottombar.BottomTab
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.navigation.Route

internal enum class TrainerMainTab(
    override val contentDescription: String,
    @DrawableRes override val icon: Int,
    val containerColor: @Composable () -> Color,
    val baseRoute: Route.TrainerMainTab,
    val route: Route,
) : BottomTab {
    HOME(
        contentDescription = "홈",
        icon = R.drawable.ic_navbar_home,
        containerColor = { TnTTheme.colors.commonColors.Common0 },
        baseRoute = Route.TrainerMainTab.Home,
        route = Route.TrainerHome,
    ),
    FEEDBACK(
        contentDescription = "피드백",
        icon = R.drawable.ic_navbar_feedback,
        containerColor = { TnTTheme.colors.neutralColors.Neutral100 },
        baseRoute = Route.TrainerMainTab.Feedback,
        route = Route.TrainerFeedback,
    ),
    MEMBERS(
        contentDescription = "회원목록",
        icon = R.drawable.ic_navbar_members,
        containerColor = { TnTTheme.colors.neutralColors.Neutral100 },
        baseRoute = Route.TrainerMainTab.Members,
        route = Route.TrainerMembers,
    ),
    MY_PAGE(
        contentDescription = "내 정보",
        icon = R.drawable.ic_navbar_mypage,
        containerColor = { TnTTheme.colors.neutralColors.Neutral50 },
        baseRoute = Route.TrainerMainTab.MyPage,
        route = Route.TrainerMyPage,
    ),
}
