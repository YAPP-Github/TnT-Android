package co.kr.tnt.trainer.members

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import co.kr.tnt.designsystem.theme.TnTTheme

@Composable
@Suppress("UnusedParameter")
internal fun TrainerMembersRoute(
    viewModel: TrainerMembersViewModel = hiltViewModel(),
    navigateToInvite: (Boolean) -> Unit,
) {
    TrainerMembersScreen()
}

@Composable
private fun TrainerMembersScreen() {
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
            }
        }
    }
}

@Preview
@Composable
private fun TrainerMembersScreenPreview() {
    TnTTheme {
        TrainerMembersScreen()
    }
}
