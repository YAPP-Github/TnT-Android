package co.kr.tnt.connect.trainer

import TrainerConnectContract.TrainerConnectUiState
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Build
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.designsystem.component.TnTToast
import co.kr.tnt.designsystem.component.TnTTopBar
import co.kr.tnt.designsystem.component.button.TnTTextButton
import co.kr.tnt.designsystem.component.button.model.ButtonSize
import co.kr.tnt.designsystem.component.button.model.ButtonType
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.feature.trainer.connect.R
import co.kr.tnt.core.ui.R as uiResource

@Composable
internal fun CodeGenerationPage(
    state: TrainerConnectUiState,
    onRegenerateClick: () -> Unit,
    onBackClick: () -> Unit,
    onSkipClick: () -> Unit,
) {
    BackHandler { onBackClick() }

    val context = LocalContext.current
    var showToast by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TnTTopBar(
                title = stringResource(uiResource.string.connect),
                trailingComponent = {
                    Text(
                        text = stringResource(uiResource.string.skip),
                        color = TnTTheme.colors.neutralColors.Neutral400,
                        style = TnTTheme.typography.body2Medium,
                        modifier = Modifier.clickable {
                            onSkipClick()
                        },
                    )
                },
            )
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
                                    .clickable {
                                        showToast = true
                                        copyToClipboard(context, state.inviteCode)
                                    },
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
                                // TODO 코드 재발급
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
            if (showToast) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter,
                ) {
                    TnTToast(
                        message = stringResource(R.string.code_is_copied),
                        onDismiss = { showToast = false },
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 50.dp),
                    )
                }
            }
        }
    }
}

private fun copyToClipboard(context: Context, text: String) {
    val clipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    clipboardManager.setPrimaryClip(ClipData.newPlainText("copied code", text))
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        clipboardManager.clearPrimaryClip()
    }
}

@Preview(showBackground = true)
@Composable
private fun CodeGenerationPagePreview() {
    TnTTheme {
        CodeGenerationPage(
            state = TODO(),
            onBackClick = {},
            onSkipClick = {},
            onRegenerateClick = {},
        )
    }
}
