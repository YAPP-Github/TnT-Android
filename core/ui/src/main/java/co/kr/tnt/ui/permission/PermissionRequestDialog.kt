package co.kr.tnt.ui.permission

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import co.kr.tnt.core.ui.R
import co.kr.tnt.designsystem.component.TnTPopupDialog
import co.kr.tnt.designsystem.theme.TnTTheme

@Composable
fun PermissionRequestDialog(
    permission: TnTPermission,
    isPermanentlyDenied: Boolean,
    onOkButtonClick: (isPermanentlyDenied: Boolean) -> Unit,
    onDismissRequest: () -> Unit,
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
        leftButtonText = stringResource(R.string.close),
        rightButtonText = stringResource(R.string.ok),
        onLeftButtonClick = onDismissRequest,
        onRightButtonClick = { onOkButtonClick(isPermanentlyDenied) },
        onDismiss = onDismissRequest,
    )
}

@Preview
@Composable
private fun PermissionRequestDialogPreview() {
    TnTTheme {
        PermissionRequestDialog(
            permission = TnTPermission.NOTIFICATION,
            isPermanentlyDenied = false,
            onOkButtonClick = { },
            onDismissRequest = { },
        )
    }
}
