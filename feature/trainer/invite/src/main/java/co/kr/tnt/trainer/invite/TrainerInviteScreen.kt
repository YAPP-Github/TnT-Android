package co.kr.tnt.trainer.invite

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.designsystem.component.TnTTopBar
import co.kr.tnt.designsystem.component.TnTTopBarWithBackButton
import co.kr.tnt.designsystem.component.button.TnTTextButton
import co.kr.tnt.designsystem.component.button.model.ButtonSize
import co.kr.tnt.designsystem.component.button.model.ButtonType
import co.kr.tnt.designsystem.snackbar.LocalSnackbar
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.feature.trainer.invite.R
import co.kr.tnt.navigation.model.ScreenMode
import co.kr.tnt.trainer.invite.TrainerInviteContract.TrainerInviteSideEffect
import co.kr.tnt.trainer.invite.TrainerInviteContract.TrainerInviteUiEvent
import co.kr.tnt.trainer.invite.TrainerInviteContract.TrainerInviteUiState
import kotlinx.coroutines.flow.collectLatest
import co.kr.tnt.core.ui.R as coreR

@Composable
internal fun TrainerInviteRoute(
    screenMode: ScreenMode,
    navigateToPrevious: () -> Unit,
    navigateToHome: (Boolean) -> Unit,
    viewModel: TrainerInviteViewModel = hiltViewModel(),
) {
    val snackbar = LocalSnackbar.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val clipboardManager = LocalClipboardManager.current

    TrainerInviteScreen(
        state = state,
        screenMode = screenMode,
        onRegenerateClick = { viewModel.setEvent(TrainerInviteUiEvent.OnRegenerateClick) },
        onCodeClick = { code -> viewModel.setEvent(TrainerInviteUiEvent.OnCodeClick(code)) },
        onBackClick = { viewModel.setEvent(TrainerInviteUiEvent.OnBackClick) },
        onSkipClick = { viewModel.setEvent(TrainerInviteUiEvent.OnSkipClick) },
    )

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                TrainerInviteSideEffect.NavigateToBack -> navigateToPrevious()
                TrainerInviteSideEffect.NavigateToHome -> navigateToHome(true)
                is TrainerInviteSideEffect.ShowToast -> snackbar.show(effect.message)
                is TrainerInviteSideEffect.CopyToClipBoard ->
                    clipboardManager.setText(AnnotatedString(effect.value))
            }
        }
    }
}

@Composable
internal fun TrainerInviteScreen(
    state: TrainerInviteUiState,
    screenMode: ScreenMode,
    onCodeClick: (code: String) -> Unit,
    onRegenerateClick: () -> Unit,
    onBackClick: () -> Unit,
    onSkipClick: () -> Unit,
) {
    BackHandler {
        when (screenMode) {
            ScreenMode.BACK -> onBackClick()
            ScreenMode.SKIP -> onSkipClick()
            ScreenMode.CLOSE -> onBackClick()
        }
    }

    Scaffold(
        topBar = {
            when (screenMode) {
                ScreenMode.BACK -> {
                    TnTTopBarWithBackButton(
                        title = stringResource(R.string.add_member),
                        onBackClick = onBackClick,
                    )
                }

                ScreenMode.SKIP -> {
                    TnTTopBar(
                        title = stringResource(coreR.string.connect),
                        trailingComponent = {
                            Text(
                                text = stringResource(coreR.string.skip),
                                color = TnTTheme.colors.neutralColors.Neutral400,
                                style = TnTTheme.typography.body2Medium,
                                modifier = Modifier.clickable {
                                    onSkipClick()
                                },
                            )
                        },
                    )
                }

                ScreenMode.CLOSE -> {
                    TnTTopBar(
                        title = stringResource(R.string.add_member),
                        trailingComponent = {
                            IconButton(
                                onClick = onBackClick,
                            ) {
                                Icon(
                                    painter = painterResource(co.kr.tnt.core.designsystem.R.drawable.ic_delete),
                                    contentDescription = null,
                                )
                            }
                        },
                    )
                }
            }
        },
        containerColor = TnTTheme.colors.commonColors.Common0,
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(Modifier.padding(top = 24.dp))
                Text(
                    text = stringResource(R.string.invite_trainee_with_code),
                    color = TnTTheme.colors.neutralColors.Neutral950,
                    style = TnTTheme.typography.h2,
                    modifier = Modifier.padding(horizontal = 24.dp),
                )
                Spacer(Modifier.padding(top = 48.dp))
                Column(Modifier.padding(horizontal = 20.dp)) {
                    Row {
                        Text(
                            text = stringResource(R.string.my_invite_code),
                            style = TnTTheme.typography.body1Bold,
                            color = TnTTheme.colors.neutralColors.Neutral900,
                        )
                        Text(
                            text = "*",
                            style = TnTTheme.typography.body1Bold,
                            color = TnTTheme.colors.redColors.Red500,
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = state.inviteCode,
                                style = TnTTheme.typography.body1Medium,
                                color = TnTTheme.colors.neutralColors.Neutral600,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clickable { onCodeClick(state.inviteCode) },
                            )
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = TnTTheme.colors.neutralColors.Neutral300,
                                modifier = Modifier.padding(top = 4.dp),
                            )
                        }
                        Box(
                            modifier = Modifier
                                .wrapContentSize(Alignment.CenterEnd)
                                .align(Alignment.CenterVertically)
                                .padding(vertical = 4.dp),
                            content = {
                                TnTTextButton(
                                    text = stringResource(R.string.code_reissue),
                                    size = ButtonSize.Small,
                                    type = ButtonType.Gray,
                                    onClick = { onRegenerateClick() },
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CodeGenerationPagePreview() {
    TnTTheme {
        TrainerInviteScreen(
            state = TrainerInviteUiState(),
            onCodeClick = {},
            onBackClick = {},
            onSkipClick = {},
            onRegenerateClick = {},
            screenMode = ScreenMode.SKIP,
        )
    }
}
