package co.kr.tnt.trainer.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.ui.component.TnTCountTopBar

@Composable
@Suppress("UnusedParameter")
internal fun TrainerFeedbackRoute(
    viewModel: TrainerFeedbackViewModel = hiltViewModel(),
) {
    TrainerFeedbackScreen()
}

@Composable
private fun TrainerFeedbackScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TnTTheme.colors.neutralColors.Neutral100),
    ) {
        TnTCountTopBar(
            title = "피드백",
            count = 0,
        )
        EmptyFeedback()
    }
}

@Composable
private fun EmptyFeedback() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "아직 등록된 기록이 없어요",
            color = TnTTheme.colors.neutralColors.Neutral600,
            style = TnTTheme.typography.body2Bold,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "트레이니가 기록을 전송하면 여기에 표시돼요!",
            color = TnTTheme.colors.neutralColors.Neutral400,
            style = TnTTheme.typography.label1Medium,
        )
    }
}
