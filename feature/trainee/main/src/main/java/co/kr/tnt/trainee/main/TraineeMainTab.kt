package co.kr.tnt.trainee.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.component.bottombar.BottomTab
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.navigation.Route

internal enum class TraineeMainTab(
    override val contentDescription: String,
    override val icon: Int,
    val containerColor: @Composable () -> Color,
    val baseRoute: Route.TraineeMainTab,
    val route: Route,
) : BottomTab {
    HOME(
        contentDescription = "홈",
        icon = R.drawable.ic_navbar_home,
        containerColor = { TnTTheme.colors.commonColors.Common0 },
        baseRoute = Route.TraineeMainTab.Home,
        route = Route.TraineeHome,
    ),
    MY_PAGE(
        contentDescription = "내 정보",
        icon = R.drawable.ic_navbar_mypage,
        containerColor = { TnTTheme.colors.neutralColors.Neutral50 },
        baseRoute = Route.TraineeMainTab.MyPage,
        route = Route.TraineeMyPage,
    ),
}
