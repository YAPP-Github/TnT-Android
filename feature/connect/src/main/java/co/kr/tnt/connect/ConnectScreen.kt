package co.kr.tnt.connect

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
@Suppress("UnusedParameter")
internal fun ConnectRoute(
    viewModel: ConnectViewModel = hiltViewModel(),
) {
    ConnectScreen()
}

@Composable
fun ConnectScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Text(
            text = "connect",
            modifier = Modifier.padding(innerPadding),
        )
    }
}
