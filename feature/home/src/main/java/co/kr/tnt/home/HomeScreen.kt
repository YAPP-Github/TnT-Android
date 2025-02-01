package co.kr.tnt.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
@Suppress("UnusedParameter")
internal fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    HomeScreen()
}

@Composable
fun HomeScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Text(
            text = "home",
            modifier = Modifier.padding(innerPadding),
        )
    }
}
