package co.kr.tnt.main.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.core.ui.R.string.core_ok
import co.kr.tnt.core.ui.R.string.core_session_expired
import co.kr.tnt.core.ui.R.string.core_session_expired_description
import co.kr.tnt.designsystem.component.TnTIconSingleButtonPopupDialog
import co.kr.tnt.designsystem.component.button.model.ButtonType
import co.kr.tnt.designsystem.snackbar.LocalSnackbar
import co.kr.tnt.designsystem.snackbar.TnTSnackbarLayout
import co.kr.tnt.designsystem.snackbar.rememberSnackbarState
import co.kr.tnt.login.navigation.navigateToLogin

@Composable
fun TnTApp(
    appState: TnTAppState,
) {
    val toastState = rememberSnackbarState()

    if (appState.showSessionExpiredDialog) {
        TnTIconSingleButtonPopupDialog(
            title = stringResource(core_session_expired),
            content = stringResource(core_session_expired_description),
            topIcon = painterResource(R.drawable.ic_round_warning),
            buttonText = stringResource(core_ok),
            cancelable = false,
            onButtonClick = {
                appState.dismissSessionDialog()
                appState.navController.navigateToLogin(clearBackStack = true)
            },
            onDismiss = {
                appState.dismissSessionDialog()
            },
            type = ButtonType.Gray,
        )
    }

    CompositionLocalProvider(LocalSnackbar provides toastState) {
        TnTSnackbarLayout(
            snackbarState = toastState,
            content = {
                TnTNavHost(
                    appState = appState,
                )
            },
        )
    }
}
