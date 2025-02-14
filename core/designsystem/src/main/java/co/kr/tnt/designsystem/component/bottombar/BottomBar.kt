package co.kr.tnt.designsystem.component.bottombar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.theme.TnTTheme

@Composable
fun <Tab : BottomTab> TnTBottomBar(
    bottomTabs: List<Tab>,
    currentTab: Tab?,
    isVisible: Boolean,
    onClickTab: (tab: Tab) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(700)) + slideIn { IntOffset(0, it.height) },
        exit = fadeOut(animationSpec = tween(700)) + slideOut { IntOffset(0, it.height) },
    ) {
        Row(modifier = modifier.background(TnTTheme.colors.commonColors.Common0)) {
            bottomTabs.forEach { tab ->
                Button(
                    onClick = { onClickTab(tab) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonColors(
                        containerColor = TnTTheme.colors.commonColors.Common0,
                        contentColor = TnTTheme.colors.commonColors.Common0,
                        disabledContainerColor = TnTTheme.colors.commonColors.Common0,
                        disabledContentColor = TnTTheme.colors.commonColors.Common0,
                    ),
                ) {
                    TnTBottomTab(
                        bottomTab = tab,
                        isSelected = currentTab == tab,
                    )
                }
            }
        }
    }
}

@Composable
private fun TnTBottomTab(
    isSelected: Boolean,
    bottomTab: BottomTab,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painterResource(bottomTab.icon),
            contentDescription = null,
            tint = if (isSelected) {
                TnTTheme.colors.neutralColors.Neutral800
            } else {
                TnTTheme.colors.neutralColors.Neutral300
            },
        )
        Text(
            text = bottomTab.contentDescription,
            style = TnTTheme.typography.label2Medium,
            fontSize = 12.sp,
            color = if (isSelected) {
                TnTTheme.colors.neutralColors.Neutral800
            } else {
                TnTTheme.colors.neutralColors.Neutral400
            },
            modifier = Modifier.padding(top = 6.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TnTBottomBarPreview() {
    val home = object : BottomTab {
        override val icon: Int
            get() = R.drawable.ic_navbar_home
        override val contentDescription: String
            get() = "홈"
    }

    val myPage = object : BottomTab {
        override val icon: Int
            get() = R.drawable.ic_navbar_mypage
        override val contentDescription: String
            get() = "내 정보"
    }

    val tabs = listOf(home, myPage)

    TnTTheme {
        TnTBottomBar(
            bottomTabs = tabs,
            currentTab = home,
            isVisible = true,
            onClickTab = { },
        )
    }
}
