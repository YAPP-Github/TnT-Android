package co.kr.tnt.trainer.members

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import co.kr.tnt.designsystem.theme.TnTTheme

@Composable
@Suppress("UnusedParameter")
internal fun TrainerMembersRoute(
    viewModel: TrainerMembersViewModel = hiltViewModel(),
    navigateToInvite: (Boolean) -> Unit,
) {
    TrainerMembersScreen(
        onClickInviteButton = { navigateToInvite(false) },
    )
}

@Composable
private fun TrainerMembersScreen(
    onClickInviteButton: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(TnTTheme.colors.neutralColors.Neutral100),
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
            ) {
                Text(
                    text = "내 회원",
                    color = TnTTheme.colors.neutralColors.Neutral900,
                    style = TnTTheme.typography.h2,
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "8",
                    color = TnTTheme.colors.redColors.Red500,
                    style = TnTTheme.typography.h2,
                )
                Spacer(Modifier.weight(1f))
                MemberInviteButton(onClickInviteButton)
            }
        }
    }
}

@Composable
private fun MemberInviteButton(
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = TnTTheme.colors.neutralColors.Neutral200,
            contentColor = TnTTheme.colors.neutralColors.Neutral50,
            disabledContainerColor = TnTTheme.colors.neutralColors.Neutral200,
            disabledContentColor = TnTTheme.colors.neutralColors.Neutral50,
        ),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 7.dp),
        modifier = Modifier.defaultMinSize(
            minWidth = Dp.Hairline,
            minHeight = 32.dp,
        ),
    ) {
        Text(
            text = "회원 초대하기",
            color = TnTTheme.colors.neutralColors.Neutral600,
            style = TnTTheme.typography.label2Medium,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
    }
}

@Preview
@Composable
private fun TrainerMembersScreenPreview() {
    TnTTheme {
        TrainerMembersScreen(
            onClickInviteButton = { },
        )
    }
}
