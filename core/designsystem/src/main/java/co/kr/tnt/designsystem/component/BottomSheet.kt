package co.kr.tnt.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.designsystem.theme.FILL_STRONG
import co.kr.tnt.designsystem.theme.TnTTheme

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
private fun TnTModalBottomSheetDragHandle() {
    Surface(
        modifier = Modifier.padding(vertical = 9.dp),
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
