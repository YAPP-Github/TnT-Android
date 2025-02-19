package co.kr.tnt.main

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.domain.monitor.SessionMonitor
import co.kr.tnt.main.MainContract.MainUiEvent
import co.kr.tnt.main.ui.TnTApp
import co.kr.tnt.main.ui.rememberTnTAppState
import co.kr.tnt.ui.permission.TnTPermission
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var sessionMonitor: SessionMonitor

    private val viewModel: MainViewModel by viewModels()

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        enableEdgeToEdge()

        getMessagingToken()
        splashScreen.setKeepOnScreenCondition { viewModel.uiState.value.showSplash }

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            if (uiState.showSplash.not()) {
                val appState = rememberTnTAppState(
                    sessionMonitor = sessionMonitor,
                    startDestination = uiState.startDestination,
                )

                TnTTheme {
                    TnTApp(appState)
                }
            }

            CheckPermissionEffect()

            LaunchedEffect(viewModel.effect) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is MainContract.MainSideEffect.ShowToast -> {
                            Toast.makeText(this@MainActivity, effect.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    private fun CheckPermissionEffect() {
        val notificationPermission = rememberMultiplePermissionsState(TnTPermission.NOTIFICATION.values)

        LaunchedEffect(Unit) {
            if (notificationPermission.shouldShowRationale.not()) {
                notificationPermission.launchMultiplePermissionRequest()
            }

            if (notificationPermission.allPermissionsGranted.not()) {
                viewModel.setEvent(MainUiEvent.OnNotificationPermissionRevoked)
            }
        }
    }

    // TODO API 변경 시 제거 예정 코드
    private fun getMessagingToken() {
        FirebaseMessaging
            .getInstance()
            .token
            .addOnSuccessListener { token ->
                token?.let {
                    viewModel.setEvent(MainUiEvent.OnGetMessagingTokenSucceeded(token))
                } ?: viewModel.setEvent(MainUiEvent.OnGetMessagingTokenFailed)
            }
            .addOnFailureListener {
                viewModel.setEvent(MainUiEvent.OnGetMessagingTokenFailed)
            }
    }
}
