package co.kr.tnt.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.designsystem.component.TnTDivider
import co.kr.tnt.designsystem.component.TnTModalBottomSheet
import co.kr.tnt.designsystem.component.button.TnTBottomButton
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.domain.model.AuthType
import co.kr.tnt.domain.model.LoginResult
import co.kr.tnt.domain.model.UserType
import co.kr.tnt.feature.login.R
import co.kr.tnt.login.LoginContract.LoginSideEffect
import co.kr.tnt.login.LoginContract.LoginUiEvent
import co.kr.tnt.login.LoginContract.LoginUiState
import co.kr.tnt.login.kakao.KakaoLoginSdk
import co.kr.tnt.login.model.TermState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LoginRoute(
    viewModel: LoginViewModel = hiltViewModel(),
    navigateToWebView: (url: String) -> Unit,
    navigateToHome: (UserType) -> Unit,
    navigateToSignup: (LoginResult) -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    val kakaoLoginSdk = remember { KakaoLoginSdk() }

    LoginScreen(
        onClickKakaoLogin = {
            coroutineScope.launch {
                // TODO ViewModel 이관 검토
                kakaoLoginSdk.login(context)
                    .onSuccess { accessToken ->
                        viewModel.setEvent(
                            LoginUiEvent.OnAuthSuccess(
                                authType = AuthType.KAKAO,
                                accessToken = accessToken.value,
                            ),
                        )
                    }
                    .onFailure { throwable ->
                        viewModel.setEvent(LoginUiEvent.OnAuthFail(throwable))
                    }
            }
        },
    )

    if (showBottomSheet) {
        TnTModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                showBottomSheet = false
            },
            content = {
                TermBottomSheetContent(
                    state = uiState,
                    onCheckAllTermAgree = { viewModel.setEvent(LoginUiEvent.OnCheckAllTermAgree) },
                    onCheckTerm = { term -> viewModel.setEvent(LoginUiEvent.OnCheckTerm(term)) },
                    onClickTermLink = { link ->
                        viewModel.setEvent(LoginUiEvent.OnClickTermLink(link))
                    },
                    onClickNext = { viewModel.setEvent(LoginUiEvent.OnClickNext) },
                )
            },
        )
    }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                LoginSideEffect.ShowTermBottomSheet -> {
                    showBottomSheet = true
                }

                is LoginSideEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }

                is LoginSideEffect.NavigateToWebView -> {
                    navigateToWebView(effect.url)
                }

                is LoginSideEffect.NavigateToHome -> {
                    showBottomSheet = false
                    navigateToHome(effect.userType)
                }

                is LoginSideEffect.NavigateToSignup -> {
                    showBottomSheet = false
                    navigateToSignup(effect.loginResult)
                }
            }
        }
    }
}

@Composable
private fun LoginScreen(
    onClickKakaoLogin: () -> Unit,
) {
    Scaffold(containerColor = TnTTheme.colors.commonColors.Common0) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp),
                    text = stringResource(R.string.nice_to_meet_you),
                    color = TnTTheme.colors.neutralColors.Neutral500,
                    style = TnTTheme.typography.body1Medium,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp),
                    text = stringResource(R.string.trainer_trainee_chemistry_boom),
                    color = TnTTheme.colors.neutralColors.Neutral950,
                    style = TnTTheme.typography.h1,
                )
                Spacer(modifier = Modifier.height(48.dp))
                Image(
                    painter = painterResource(id = R.drawable.img_trainer_and_trainee),
                    contentDescription = "trainer and trainee",
                    modifier = Modifier.size(310.dp),
                )
                Spacer(modifier = Modifier.height(48.dp))
                KakaoLoginButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    onClick = onClickKakaoLogin,
                )
            }
        }
    }
}

@Composable
private fun KakaoLoginButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFDE500),
            contentColor = TnTTheme.colors.neutralColors.Neutral900,
        ),
        contentPadding = PaddingValues(
            vertical = 16.dp,
            horizontal = 20.dp,
        ),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_kakao),
                contentDescription = "Kakao login",
            )
            Text(
                text = stringResource(R.string.continue_with_kakao),
                style = TnTTheme.typography.body1Medium,
            )
        }
    }
}

@Composable
private fun TermBottomSheetContent(
    state: LoginUiState,
    onCheckAllTermAgree: () -> Unit,
    onCheckTerm: (TermState) -> Unit,
    onClickTermLink: (String) -> Unit,
    onClickNext: () -> Unit,
) {
    val isAllTermChecked = state.isAllTermChecked()

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
        ) {
            Text(
                text = stringResource(R.string.please_agree_terms),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 17.dp),
                color = TnTTheme.colors.neutralColors.Neutral900,
                style = TnTTheme.typography.h3,
            )
            Text(
                text = stringResource(R.string.privacy_protection_promise),
                modifier = Modifier.fillMaxWidth(),
                color = TnTTheme.colors.neutralColors.Neutral500,
                style = TnTTheme.typography.body2Medium,
            )
            Spacer(modifier = Modifier.height(40.dp))
            AllTermCheckItem(
                isAllTermChecked = isAllTermChecked,
                onCheck = onCheckAllTermAgree,
            )
            TnTDivider(modifier = Modifier.padding(vertical = 8.dp))
            state.terms.entries.forEachIndexed { index, (termState, isChecked) ->
                val isLastIndex = (state.terms.size - 1) == index

                TermItem(
                    termState = termState,
                    isChecked = isChecked,
                    onCheck = onCheckTerm,
                    onClickTermLink = { onClickTermLink(termState.link) },
                )

                if (!isLastIndex) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(94.dp))
        TnTBottomButton(
            text = stringResource(R.string.next),
            enabled = isAllTermChecked,
            onClick = onClickNext,
        )
    }
}

@Composable
private fun AllTermCheckItem(
    isAllTermChecked: Boolean,
    onCheck: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        TermCheckBox(
            isChecked = isAllTermChecked,
            onClick = onCheck,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = stringResource(R.string.agree_all),
                color = TnTTheme.colors.neutralColors.Neutral900,
                style = TnTTheme.typography.body2Bold,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.agree_all_terms_for_service),
                color = TnTTheme.colors.neutralColors.Neutral500,
                style = TnTTheme.typography.body2Medium,
            )
        }
    }
}

@Composable
private fun TermItem(
    termState: TermState,
    isChecked: Boolean,
    onCheck: (termState: TermState) -> Unit,
    onClickTermLink: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TermCheckBox(
            isChecked = isChecked,
            onClick = { onCheck(termState) },
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = buildString {
                append("(")
                append(
                    if (termState.isRequired) {
                        stringResource(R.string.required)
                    } else {
                        stringResource(R.string.optional)
                    },
                )
                append(")")
                append(stringResource(termState.titleRes))
            },
            color = TnTTheme.colors.neutralColors.Neutral500,
            style = TnTTheme.typography.body2Medium,
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.see),
            color = TnTTheme.colors.neutralColors.Neutral300,
            style = TnTTheme.typography.body2Medium,
            modifier = Modifier.clickable(onClick = onClickTermLink),
        )
    }
}

@Composable
private fun TermCheckBox(
    isChecked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Icon(
        modifier = modifier.clickable(onClick = onClick),
        painter =
            if (isChecked) {
                painterResource(R.drawable.ic_check_box_enable)
            } else {
                painterResource(R.drawable.ic_check_box_disable)
            },
        contentDescription = "Check box",
        tint = Color.Unspecified,
    )
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    TnTTheme {
        LoginScreen(
            onClickKakaoLogin = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TermBottomSheetContentPreview() {
    TnTTheme {
        TermBottomSheetContent(
            state = LoginUiState(),
            onCheckAllTermAgree = { },
            onCheckTerm = { },
            onClickTermLink = { },
            onClickNext = { },
        )
    }
}
