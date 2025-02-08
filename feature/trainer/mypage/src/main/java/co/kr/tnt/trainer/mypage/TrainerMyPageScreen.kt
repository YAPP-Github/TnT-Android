package co.kr.tnt.trainer.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.component.TnTProfileImage
import co.kr.tnt.designsystem.component.TnTSwitch
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.ui.component.TnTMyPageButton
import co.kr.tnt.ui.model.DefaultUserProfile
import co.kr.tnt.core.ui.R as coreR

@Composable
@Suppress("UnusedParameter")
internal fun TrainerMyPageRoute(
    navigateToLogin: () -> Unit,
    navigateToWebView: (String) -> Unit,
    viewModel: TrainerMyPageViewModel = hiltViewModel(),
) {
    TrainerMyPageScreen(
        onTogglePushNotification = { },
        onClickServiceTerm = { },
        onClickPrivacy = { },
        onClickOpenSourceLicense = { },
        onClickLogout = { },
        onClickDeleteAccount = { },
    )
}

@Composable
private fun TrainerMyPageScreen(
    onTogglePushNotification: () -> Unit,
    onClickServiceTerm: () -> Unit,
    onClickPrivacy: () -> Unit,
    onClickOpenSourceLicense: () -> Unit,
    onClickLogout: () -> Unit,
    onClickDeleteAccount: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(TnTTheme.colors.neutralColors.Neutral50)
            .verticalScroll(rememberScrollState()),
    ) {
        TnTProfileImage(
            defaultImage = painterResource(DefaultUserProfile.Trainee.image),
            imageSize = 132.dp,
            showEditButton = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
        )
        Text(
            text = "회원명",
            style = TnTTheme.typography.h2,
            color = TnTTheme.colors.neutralColors.Neutral950,
        )
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            ManagementMemberCount(
                title = "관리 중인 회원",
                count = 20,
            )
            Spacer(modifier = Modifier.width(8.dp))
            ManagementMemberCount(
                title = "함께 했던 회원",
                count = 24,
            )
        }
        Spacer(Modifier.height(16.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
        ) {
            TnTMyPageButton(
                text = stringResource(coreR.string.app_push_notification),
                verticalPadding = 12.dp,
                enabled = false,
                onClick = { },
                trailingComponent = {
                    TnTSwitch(
                        checked = true,
                        onClick = onTogglePushNotification,
                    )
                },
            )
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(TnTTheme.colors.commonColors.Common0)
                    .padding(vertical = 12.dp),
            ) {
                TnTMyPageButton(
                    text = stringResource(coreR.string.terms_of_service),
                    onClick = onClickServiceTerm,
                    verticalPadding = 8.dp,
                )
                TnTMyPageButton(
                    text = stringResource(coreR.string.privacy_policy),
                    onClick = onClickPrivacy,
                    verticalPadding = 8.dp,
                )
                TnTMyPageButton(
                    text = stringResource(coreR.string.app_push_notification),
                    verticalPadding = 12.dp,
                    enabled = false,
                    onClick = onTogglePushNotification,
                    trailingComponent = {
                        Text(
                            text = "1.0.0",
                            style = TnTTheme.typography.body2Medium,
                            color = TnTTheme.colors.neutralColors.Neutral400,
                        )
                    },
                )
                TnTMyPageButton(
                    text = stringResource(coreR.string.open_source_license),
                    onClick = onClickOpenSourceLicense,
                    verticalPadding = 8.dp,
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(TnTTheme.colors.commonColors.Common0)
                    .padding(vertical = 12.dp),
            ) {
                TnTMyPageButton(
                    text = stringResource(coreR.string.logout),
                    onClick = onClickLogout,
                    verticalPadding = 8.dp,
                )
                TnTMyPageButton(
                    text = stringResource(coreR.string.delete_account),
                    onClick = onClickDeleteAccount,
                    verticalPadding = 8.dp,
                )
            }
        }
    }
}

@Composable
private fun ManagementMemberCount(
    title: String,
    count: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(TnTTheme.colors.neutralColors.Neutral100)
            .padding(
                vertical = 16.dp,
                horizontal = 40.dp,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            title,
            style = TnTTheme.typography.label1Bold,
            color = TnTTheme.colors.neutralColors.Neutral500,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(28.dp),
                painter = painterResource(R.drawable.ic_bomb),
                contentDescription = null,
                tint = Color.Unspecified,
            )
            Text(
                count.toString(),
                style = TnTTheme.typography.body1Medium,
                color = TnTTheme.colors.neutralColors.Neutral950,
            )
        }
    }
}

@Preview
@Composable
private fun TrainerMyPageScreenPreview() {
    TnTTheme {
        TrainerMyPageScreen(
            onTogglePushNotification = { },
            onClickServiceTerm = { },
            onClickPrivacy = { },
            onClickOpenSourceLicense = { },
            onClickLogout = { },
            onClickDeleteAccount = { },
        )
    }
}
