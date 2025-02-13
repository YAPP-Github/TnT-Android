package co.kr.tnt.trainer.members

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.component.card.TnTMemberProfileCard
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.domain.model.MemberInfo
import co.kr.tnt.trainer.members.TrainerMemberContract.TrainerMemberUiState
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
internal fun TrainerMembersRoute(
    viewModel: TrainerMembersViewModel = hiltViewModel(),
    navigateToInvite: (Boolean) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TrainerMembersScreen(
        state = uiState,
        onClickInviteButton = { navigateToInvite(false) },
    )
}

@Composable
private fun TrainerMembersScreen(
    state: TrainerMemberUiState,
    onClickInviteButton: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TnTTheme.colors.neutralColors.Neutral100),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
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
                        text = state.memberList.size.toString(),
                        color = TnTTheme.colors.redColors.Red500,
                        style = TnTTheme.typography.h2,
                    )
                    Spacer(Modifier.weight(1f))
                    MemberInviteButton(onClickInviteButton)
                }
            }
            if (state.memberList.isNotEmpty()) {
                items(state.memberList) { member ->
                    MemberList(member)
                }
            }
        }
        if (state.memberList.isEmpty()) {
            EmptyMemberList(
                modifier = Modifier.align(Alignment.Center),
            )
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

@Composable
private fun EmptyMemberList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "아직 연결된 회원이 없어요",
            color = TnTTheme.colors.neutralColors.Neutral600,
            style = TnTTheme.typography.body2Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "추가 버튼을 눌러 회원을 추가해 보세요",
            color = TnTTheme.colors.neutralColors.Neutral400,
            style = TnTTheme.typography.label1Medium,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun MemberList(member: MemberInfo) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(member.profileUrl)
            .placeholder(R.drawable.img_default)
            .error(R.drawable.img_default)
            .build(),
    )
    // TODO : 회원 상세 페이지 연결 (배포 이후)
    TnTMemberProfileCard(
        name = member.traineeName,
        profileImage = painter,
        purpose = member.ptPurpose,
        memo = member.memo ?: "",
        completedSessions = member.finishedPtCount.toString(),
        totalSessions = member.totalPtCount.toString(),
        modifier = Modifier.padding(horizontal = 16.dp),
        onClick = { },
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@Preview(widthDp = 300)
@Composable
private fun TrainerMembersScreenPreview() {
    TnTTheme {
        TrainerMembersScreen(
            state = TrainerMemberUiState(
                memberList = listOf(
                    MemberInfo(
                        id = 0L,
                        traineeName = "김선정",
                        profileUrl = "https://buly.kr/BpESNP5",
                        ptPurpose = "체중 감량, 근력 향상, 건강 관리, 자세 교정, 유연성 향상",
                        memo = "발목 안 좋고 식단 관리 원함. 하체가 약한 편임. 손목도 약함",
                        finishedPtCount = 1,
                        totalPtCount = 2,
                    ),
                    MemberInfo(
                        id = 0L,
                        traineeName = "김영명",
                        profileUrl = "https://buly.kr/BpESNP5",
                        ptPurpose = "근력 향상",
                        memo = "식단 관리 원함.",
                        finishedPtCount = 1,
                        totalPtCount = 2,
                    ),
                    MemberInfo(
                        id = 0L,
                        traineeName = "김정호",
                        profileUrl = "https://buly.kr/BpESNP5",
                        ptPurpose = "근력 향상, 건강 관리",
                        memo = "직장인. 6시 퇴근이라 이후에 수업 일정 잡아야 함.",
                        finishedPtCount = 1,
                        totalPtCount = 2,
                    ),
                    MemberInfo(
                        id = 0L,
                        traineeName = "박민서",
                        profileUrl = "https://buly.kr/BpESNP5",
                        ptPurpose = "근력 향상, 건강 관리",
                        memo = "수면 부족임. 잠을 잘 잤는지 확인 필요",
                        finishedPtCount = 1,
                        totalPtCount = 2,
                    ),
                    MemberInfo(
                        id = 0L,
                        traineeName = "김혜림",
                        profileUrl = "https://buly.kr/BpESNP5",
                        ptPurpose = "건강 관리",
                        memo = "3시 이후 수업 가능",
                        finishedPtCount = 1,
                        totalPtCount = 2,
                    ),
                ),
            ),
            onClickInviteButton = { },
        )
    }
}
