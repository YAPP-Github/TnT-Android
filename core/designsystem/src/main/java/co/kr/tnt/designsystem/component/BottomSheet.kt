package co.kr.tnt.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.designsystem.theme.FILL_STRONG
import co.kr.tnt.designsystem.theme.TnTTheme
import com.holix.android.bottomsheetdialog.compose.BottomSheetBehaviorProperties
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialog
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialogProperties
import com.holix.android.bottomsheetdialog.compose.NavigationBarProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TnTModalBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    ModalBottomSheet(
        modifier = modifier,
        containerColor = TnTTheme.colors.commonColors.Common0,
        sheetState = sheetState,
        shape = RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp,
        ),
        onDismissRequest = onDismissRequest,
        dragHandle = { TnTModalBottomSheetDragHandle() },
        content = content,
    )
}

@Composable
fun TnTBottomSheetDialog(
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    val context = LocalContext.current
    val maxHeight = remember { (context.resources.displayMetrics.heightPixels * 0.8).toInt() }

    BottomSheetDialog(
        properties = BottomSheetDialogProperties(
            enableEdgeToEdge = true,
            navigationBarProperties = NavigationBarProperties(
                color = TnTTheme.colors.commonColors.Common0,
            ),
            behaviorProperties = BottomSheetBehaviorProperties(
                isDraggable = false,
                state = BottomSheetBehaviorProperties.State.Expanded,
                skipCollapsed = true,
                maxHeight = BottomSheetBehaviorProperties.Size(value = maxHeight),
            ),
        ),
        onDismissRequest = onDismissRequest,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(
                            topStart = 12.dp,
                            topEnd = 12.dp,
                        ),
                    )
                    .background(TnTTheme.colors.commonColors.Common0),
            ) {
                content()
            }
        },
    )
}

@Composable
private fun TnTModalBottomSheetDragHandle(
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.padding(vertical = 9.dp),
        color = FILL_STRONG,
        shape = RoundedCornerShape(40.dp),
    ) {
        Box(
            modifier = Modifier.size(
                width = 40.dp,
                height = 5.dp,
            ),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun TnTModalBottomSheetPreview() {
    TnTTheme {
        TnTModalBottomSheet(
            sheetState = rememberStandardBottomSheetState(
                initialValue = SheetValue.Expanded,
            ),
            onDismissRequest = { },
        ) {
            Text(
                modifier = Modifier.padding(vertical = 24.dp),
                text = "TnT Modal Bottom Sheet :)",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TnTBottomSheetDialogPreview() {
    TnTTheme {
        TnTBottomSheetDialog(
            onDismissRequest = { },
        ) {
            Text(
                modifier = Modifier.padding(vertical = 24.dp),
                text = "TnT Bottom sheet dialog :)",
            )
        }
    }
}
