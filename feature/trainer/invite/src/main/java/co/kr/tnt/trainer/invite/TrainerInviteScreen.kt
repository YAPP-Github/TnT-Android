package co.kr.tnt.trainer.invite

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.core.ui.R.string.core_connect
import co.kr.tnt.core.ui.R.string.core_skip
import co.kr.tnt.designsystem.component.TnTTopBar
import co.kr.tnt.designsystem.component.TnTTopBarWithBackButton
import co.kr.tnt.designsystem.component.button.model.ButtonSize
import co.kr.tnt.designsystem.component.button.model.ButtonType
import co.kr.tnt.designsystem.component.textfield.TnTSelectableLabeledTextFieldWithTextButton
import co.kr.tnt.designsystem.snackbar.LocalSnackbar
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.feature.trainer.invite.R
import co.kr.tnt.navigation.model.ScreenMode
import co.kr.tnt.trainer.invite.TrainerInviteContract.TrainerInviteSideEffect
import co.kr.tnt.trainer.invite.TrainerInviteContract.TrainerInviteUiEvent
import co.kr.tnt.trainer.invite.TrainerInviteContract.TrainerInviteUiState
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun TrainerInviteRoute(
    screenMode: ScreenMode,
    navigateToPrevious: () -> Unit,
    navigateToHome: (Boolean) -> Unit,
    viewModel: TrainerInviteViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val snackbar = LocalSnackbar.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val clipboardManager = LocalClipboardManager.current

    TrainerInviteScreen(
        state = state,
        screenMode = screenMode,
        onClickRegenerate = { viewModel.setEvent(TrainerInviteUiEvent.OnClickRegenerate) },
        onClickCode = { code -> viewModel.setEvent(TrainerInviteUiEvent.OnClickCode(code)) },
        onClickBack = { viewModel.setEvent(TrainerInviteUiEvent.OnClickBack) },
        onClickSkip = { viewModel.setEvent(TrainerInviteUiEvent.OnClickSkip) },
    )

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                TrainerInviteSideEffect.NavigateToBack -> navigateToPrevious()
                TrainerInviteSideEffect.NavigateToHome -> navigateToHome(true)
                is TrainerInviteSideEffect.ShowToast -> snackbar.show(
                    message = effect.message.asString(context),
                    icon = effect.type.iconRes,
                )
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
    onClickCode: (code: String) -> Unit,
    onClickRegenerate: () -> Unit,
    onClickBack: () -> Unit,
    onClickSkip: () -> Unit,
) {
    BackHandler {
        when (screenMode) {
            ScreenMode.BACK -> onClickBack()
            ScreenMode.SKIP -> onClickSkip()
            ScreenMode.CLOSE -> onClickBack()
        }
    }

    Scaffold(
        topBar = {
            when (screenMode) {
                ScreenMode.BACK -> {
                    TnTTopBarWithBackButton(
                        title = stringResource(R.string.add_member),
                        onBackClick = onClickBack,
                    )
                }

                ScreenMode.SKIP -> {
                    TnTTopBar(
                        title = stringResource(core_connect),
                        trailingComponent = {
                            Text(
                                text = stringResource(core_skip),
                                color = TnTTheme.colors.neutralColors.Neutral400,
                                style = TnTTheme.typography.body2Medium,
                                modifier = Modifier.clickable {
                                    onClickSkip()
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
                                onClick = onClickBack,
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
                TnTSelectableLabeledTextFieldWithTextButton(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    title = stringResource(R.string.my_invite_code),
                    showRequiredTitleBadge = true,
                    value = state.inviteCode,
                    trailingButtonTitle = stringResource(R.string.code_reissue),
                    trailingButtonSize = ButtonSize.Small,
                    trailingButtonType = ButtonType.Gray,
                    onClickTextField = { onClickCode(state.inviteCode) },
                    onClickTrailingButton = { onClickRegenerate() },
                )
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
            onClickCode = {},
            onClickBack = {},
            onClickSkip = {},
            onClickRegenerate = {},
            screenMode = ScreenMode.SKIP,
        )
    }
}
