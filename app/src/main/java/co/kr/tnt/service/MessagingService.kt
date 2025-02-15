package co.kr.tnt.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import co.kr.tnt.R
import co.kr.tnt.domain.repository.SettingRepository
import co.kr.tnt.main.MainActivity
import com.google.firebase.messaging.Constants.MessageNotificationKeys
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MessagingService: FirebaseMessagingService() {
    @Inject
    lateinit var settingRepository: SettingRepository

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        scope.launch {
            // TODO message.data 형태로 오도록 요청
            val isEnabled = settingRepository.isEnablePushNotification().first()

            if (isEnabled) {
                launchNotification(
                    title = message.data["title"] ?: "트레이너 연결 알림",
                    content = message.data["content"] ?: "트레이니님과 연결되었어요!",
                    pendingIntent = createPendingIntent(message.data)
                )
            }
        }
    }

    private fun createPendingIntent(
        data: Map<String, String>,
    ): PendingIntent? {
        val trainerId = data["trainerId"] ?: "0"
        val traineeId = data["traineeId"] ?: "0"

        val intent = Intent(this, MainActivity::class.java).apply {
            this.data = "tnt-deeplink://trainee-connect-complete/$trainerId/$traineeId".toUri()
        }

        return TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(
                INTENT_REQUEST_CODE,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

    override fun handleIntent(intent: Intent?) {
        // Background 일 때 FCM notification 필드가 있으면
        // data 필드를 수신할 수 없는 현상을 해결하기 위해,
        // notification key 값 제거
        val new = intent?.apply {
            val temp = extras?.apply {
                remove(MessageNotificationKeys.ENABLE_NOTIFICATION)
                remove(keyWithOldPrefix(MessageNotificationKeys.ENABLE_NOTIFICATION))
            }
            replaceExtras(temp)
        }
        super.handleIntent(new)
    }

    private fun launchNotification(
        title: String?,
        content: String,
        pendingIntent: PendingIntent?,
    ) {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        val notificationManager: NotificationManager =
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).apply {
                createNotificationChannel(channel)
            }

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_logo)
            .setColor(ContextCompat.getColor(this, R.color.ic_notification_background))
            .setContentTitle(title)
            .setContentText(content)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun keyWithOldPrefix(key: String): String {
        if (!key.startsWith(MessageNotificationKeys.NOTIFICATION_PREFIX)) {
            return key
        }

        return key.replace(
            MessageNotificationKeys.NOTIFICATION_PREFIX,
            MessageNotificationKeys.NOTIFICATION_PREFIX_OLD
        )
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.w("Messaging service", "token : $token") // TODO
    }

    companion object {
        private const val CHANNEL_ID = "TNT_NOTIFICATION_ID"
        private const val CHANNEL_NAME = "TNT_NOTIFICATION"
        private const val INTENT_REQUEST_CODE = 100
    }
}
