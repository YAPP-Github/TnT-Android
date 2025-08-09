package co.kr.tnt.ui.permission

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import co.kr.tnt.core.ui.R.string.core_close
import co.kr.tnt.core.ui.R.string.core_move_to_setting
import co.kr.tnt.core.ui.R.string.core_ok
import co.kr.tnt.designsystem.component.TnTPopupDialog
import co.kr.tnt.designsystem.theme.TnTTheme

@Composable
fun PermissionRequestDialog(
    permission: TnTPermission,
    isPermanentlyDenied: Boolean,
    onClickConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TnTPopupDialog(
        modifier = modifier,
        title = stringResource(
            if (isPermanentlyDenied) {
                permission.permanentlyDeniedTitle
            } else {
                permission.title
            },
        ),
        content = stringResource(
            if (isPermanentlyDenied) {
                permission.permanentlyDeniedDescription
            } else {
                permission.description
            },
        ),
        leftButtonText = stringResource(core_close),
        rightButtonText = stringResource(
            if (isPermanentlyDenied) {
                core_move_to_setting
            } else {
                core_ok
            },
        ),
        onLeftButtonClick = onDismiss,
        onRightButtonClick = onClickConfirm,
        onDismiss = onDismiss,
    )
}

@Preview
@Composable
private fun PermissionRequestDialogPreview() {
    TnTTheme {
        PermissionRequestDialog(
            permission = TnTPermission.NOTIFICATION,
            isPermanentlyDenied = true,
            onClickConfirm = { },
            onDismiss = { },
        )
    }
}
