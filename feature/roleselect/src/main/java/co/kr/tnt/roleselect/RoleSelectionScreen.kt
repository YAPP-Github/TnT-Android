package co.kr.tnt.roleselect

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import co.kr.tnt.designsystem.component.button.TnTBottomButton
import co.kr.tnt.designsystem.component.button.TnTTextButton
import co.kr.tnt.designsystem.component.button.model.ButtonSize
import co.kr.tnt.designsystem.component.button.model.ButtonType
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.domain.model.UserType
import co.kr.tnt.feature.roleselect.R
import co.kr.tnt.roleselect.RoleSelectionContract.RoleSelectionEffect
import co.kr.tnt.roleselect.RoleSelectionContract.RoleSelectionUiEvent
import co.kr.tnt.roleselect.model.RoleState
import co.kr.tnt.core.ui.R as uiResource

@Composable
internal fun RoleSelectionRoute(
    viewModel: RoleSelectionViewModel = hiltViewModel(),
    navigateToTraineeSignUp: () -> Unit,
    navigateToTrainerSignUp: () -> Unit,
) {
    RoleSelectionScreen(
        onNextClick = { viewModel.setEvent(RoleSelectionUiEvent.OnNextClick(it)) },
    )

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                RoleSelectionEffect.NavigateToTraineeSignUp -> navigateToTraineeSignUp()
                RoleSelectionEffect.NavigateToTrainerSignUp -> navigateToTrainerSignUp()
            }
        }
    }
}

@Composable
fun RoleSelectionScreen(
    onNextClick: (RoleState) -> Unit = {},
) {
    var selectedRole by remember { mutableStateOf(RoleState.fromDomain(UserType.TRAINER)) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = TnTTheme.colors.commonColors.Common0,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.padding(start = 24.dp, top = 60.dp)) {
                Text(
                    text = stringResource(R.string.select_role),
                    color = TnTTheme.colors.neutralColors.Neutral950,
                    style = TnTTheme.typography.h2,
                )
                Spacer(modifier = Modifier.padding(top = 12.dp))
                Text(
                    text = stringResource(R.string.select_between_trainer_and_trainee),
                    color = TnTTheme.colors.neutralColors.Neutral500,
                    style = TnTTheme.typography.body1Medium,
                )
            }
            Image(
                painter = painterResource(selectedRole.imageResId),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 20.dp),
            ) {
                TnTTextButton(
                    text = stringResource(RoleState.Trainer.textResId),
                    modifier = Modifier.weight(1f),
                    size = ButtonSize.Large,
                    type = if (selectedRole == RoleState.Trainer) ButtonType.RedOutline else ButtonType.GrayOutline,
                    onClick = {
                        selectedRole = RoleState.Trainer
                    },
                )
                TnTTextButton(
                    text = stringResource(RoleState.Trainee.textResId),
                    modifier = Modifier.weight(1f),
                    size = ButtonSize.Large,
                    type = if (selectedRole == RoleState.Trainee) ButtonType.RedOutline else ButtonType.GrayOutline,
                    onClick = {
                        selectedRole = RoleState.Trainee
                    },
                )
            }
            TnTBottomButton(
                text = stringResource(uiResource.string.next),
                enabled = true,
                onClick = { onNextClick(selectedRole) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RoleScreenPreview() {
    TnTTheme {
        RoleSelectionScreen(
            onNextClick = {},
        )
    }
}
