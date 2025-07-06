package co.kr.tnt.trainer.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import co.kr.tnt.core.ui.R.string.core_no_records_yet
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.feature.trainer.feedback.R
import co.kr.tnt.ui.component.TnTCountTopBar

@Composable
@Suppress("UnusedParameter")
internal fun TrainerFeedbackRoute(
    padding: PaddingValues,
    viewModel: TrainerFeedbackViewModel = hiltViewModel(),
) {
    TrainerFeedbackScreen(padding)
}

@Composable
private fun TrainerFeedbackScreen(
    padding: PaddingValues,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
    ) {
        TnTCountTopBar(
            title = stringResource(R.string.feedback),
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
            text = stringResource(core_no_records_yet),
            color = TnTTheme.colors.neutralColors.Neutral600,
            style = TnTTheme.typography.body2Bold,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.records_shown_when_trainee_sends),
            color = TnTTheme.colors.neutralColors.Neutral400,
            style = TnTTheme.typography.label1Medium,
        )
    }
}
