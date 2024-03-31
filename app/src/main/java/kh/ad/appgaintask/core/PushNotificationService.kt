package kh.ad.appgaintask.core

import android.app.NotificationChannel
import android.app.NotificationManager
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kh.ad.appgaintask.R

class PushNotificationService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        LogUtil.d("OnNewToken", token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val notification = remoteMessage.notification
        if (notification != null) {
            val title = remoteMessage.notification!!.title
            val body = remoteMessage.notification!!.body
            showNotification(title, body)
        }
    }

    private var notificationId = 0
    private fun showNotification(title: String?, body: String?) {
        val channelId = "MyChannelId"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                "Firebase Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            getSystemService(NotificationManager::class.java).createNotificationChannel(
                notificationChannel
            )
        }
        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSound)
            .setNumber(++notificationId)
        val notify = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notify.notify(notificationId, builder.build())
    }
}