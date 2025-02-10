package co.kr.tnt.trainer.addptsession

import android.widget.Toast
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.trainer.addptsession.AddPtSessionContract.AddPtSessionSideEffect
import co.kr.tnt.trainer.addptsession.AddPtSessionContract.AddPtSessionUiState

@Composable
internal fun AddPtSessionRoute(
    viewModel: AddPtSessionViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AddPtSessionScreen(state = uiState)

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is AddPtSessionSideEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@Composable
private fun AddPtSessionScreen(
    state: AddPtSessionUiState,
) {
    Text("Add pt session screen ${state.dummy}")
}
