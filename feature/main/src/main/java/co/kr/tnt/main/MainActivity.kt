package co.kr.tnt.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.domain.monitor.SessionMonitor
import co.kr.tnt.main.ui.TnTApp
import co.kr.tnt.main.ui.rememberTnTAppState
import co.kr.tnt.ui.permission.TnTPermission
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var sessionMonitor: SessionMonitor

    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        splashScreen.setKeepOnScreenCondition { viewModel.uiState.value.showSplash }

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val notificationPermission = rememberMultiplePermissionsState(TnTPermission.NOTIFICATION.values)

            LaunchedEffect(Unit) {
                if (notificationPermission.shouldShowRationale.not()) {
                    notificationPermission.launchMultiplePermissionRequest()
                }
            }

            if (uiState.showSplash.not()) {
                val appState = rememberTnTAppState(
                    sessionMonitor = sessionMonitor,
                    startDestination = uiState.startDestination,
                )

                TnTTheme {
                    TnTApp(appState)
                }
            }
        }
    }
}
