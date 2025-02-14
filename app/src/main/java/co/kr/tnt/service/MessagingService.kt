package co.kr.tnt.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import co.kr.tnt.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        launchNotification(
            title = message.notification?.title,
            content = message.notification?.body ?: "알림이 도착했어요!"
        )
    }

    private fun launchNotification(
        title: String?,
        content: String,
    ) {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        val notificationManager: NotificationManager =
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).apply {
                createNotificationChannel(channel)
            }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_logo)
            .setColor(ContextCompat.getColor(this, R.color.ic_notification_background))
            .setContentTitle(title)
            .setContentText(content)
            .setAutoCancel(true)

        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.w("Messaging service", "token : $token") // TODO
    }

    companion object {
        private const val CHANNEL_ID = "TNT_NOTIFICATION_ID"
        private const val CHANNEL_NAME = "TNT_NOTIFICATION"
    }
}
