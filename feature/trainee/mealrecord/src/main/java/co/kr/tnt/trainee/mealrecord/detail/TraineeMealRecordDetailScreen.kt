package co.kr.tnt.trainee.mealrecord.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.component.TnTTopBarWithBackButton
import co.kr.tnt.designsystem.theme.TnTTheme

@Composable
@Suppress("UnusedParameter")
internal fun TraineeMealRecordDetailRoute(
    navigateToPrevious: () -> Unit,
) {
    TraineeMealRecordDetailScreen(
        onClickBack = navigateToPrevious,
    )
}

@Composable
private fun TraineeMealRecordDetailScreen(
    onClickBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TnTTopBarWithBackButton(
                title = "",
                onBackClick = onClickBack,
                showStoke = true,
                trailingComponent = {
                    Icon(
                        painter = painterResource(R.drawable.ic_kebab),
                        contentDescription = null,
                    )
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
        ) {
            Text("meal record detail")
        }
    }
}

@Preview
@Composable
private fun TraineeMealRecordDetailPreview() {
    TnTTheme {
        TraineeMealRecordDetailScreen(
            onClickBack = { },
        )
    }
}
