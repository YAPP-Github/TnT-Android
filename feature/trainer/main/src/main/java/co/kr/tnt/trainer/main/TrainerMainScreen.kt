package co.kr.tnt.trainer.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
@Suppress("UnusedParameter")
internal fun TrainerMainRoute() {
    TrainerMainScreen()
}

@Composable
fun TrainerMainScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Text(
            text = "trainer main",
            modifier = Modifier.padding(innerPadding),
        )
    }
}
