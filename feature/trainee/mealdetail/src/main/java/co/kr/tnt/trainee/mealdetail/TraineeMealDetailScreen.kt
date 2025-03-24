package co.kr.tnt.trainee.mealdetail

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.component.TnTTopBarWithBackButton
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.domain.model.RecordType.MealType
import co.kr.tnt.domain.utils.DateFormatter
import co.kr.tnt.trainee.mealdetail.TraineeMealDetailContract.TraineeMealDetailSideEffect
import co.kr.tnt.trainee.mealdetail.TraineeMealDetailContract.TraineeMealDetailUiState

@Composable
internal fun TraineeMealDetailScreen(
    viewModel: TraineeMealDetailViewModel,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val dateFormatter = remember { DateFormatter() }

    TraineeMealDetailScreen(
        state = state,
        dateFormatter = dateFormatter,
        onClickBack = { },
    )

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TraineeMealDetailSideEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }

                TraineeMealDetailSideEffect.NavigateToHome -> TODO()
            }
        }
    }
}

@Composable
private fun TraineeMealDetailScreen(
    state: TraineeMealDetailUiState,
    dateFormatter: DateFormatter,
    onClickBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TnTTopBarWithBackButton(
                title = dateFormatter.format(state.date.toLocalDate(), "M월 d일"),
                onBackClick = onClickBack,
                showStoke = true,
                trailingComponent = {
                    Icon(
                        painter = painterResource(R.drawable.ic_more),
                        contentDescription = null,
                    )
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            Text("Meal detail screen")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TraineeMealDetailScreenPreview() {
    TnTTheme {
        TraineeMealDetailScreen(
            state = TraineeMealDetailUiState(
                image = "image",
                mealType = MealType.BREAKFAST,
                memo = "오늘은 계란을 먹었습니다.",
            ),
            dateFormatter = DateFormatter(),
            onClickBack = { },
        )
    }
}
