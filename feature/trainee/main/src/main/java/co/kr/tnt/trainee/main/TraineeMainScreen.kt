package co.kr.tnt.trainee.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun TraineeMainRoute() {
    TraineeMainScreen()
}

@Composable
private fun TraineeMainScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Text("Trainee Main", modifier = Modifier.padding(innerPadding))
    }
}
