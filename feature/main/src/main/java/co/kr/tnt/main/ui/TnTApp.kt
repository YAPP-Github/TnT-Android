package co.kr.tnt.main.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.component.TnTIconSingleButtonPopupDialog
import co.kr.tnt.designsystem.component.button.model.ButtonType
import co.kr.tnt.designsystem.snackbar.LocalSnackbar
import co.kr.tnt.designsystem.snackbar.TnTSnackbarLayout
import co.kr.tnt.designsystem.snackbar.rememberSnackbarState
import co.kr.tnt.login.navigation.navigateToLogin
import co.kr.tnt.core.ui.R as coreR

@Composable
fun TnTApp(
    appState: TnTAppState,
) {
    val context = LocalContext.current
    val toastState = rememberSnackbarState()

    if (appState.showSessionExpiredDialog) {
        TnTIconSingleButtonPopupDialog(
            title = stringResource(coreR.string.session_expired),
            content = stringResource(coreR.string.session_expired_description),
            topIcon = painterResource(R.drawable.ic_round_warning),
            buttonText = stringResource(coreR.string.ok),
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
