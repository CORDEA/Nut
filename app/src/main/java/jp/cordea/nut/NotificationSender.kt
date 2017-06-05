package jp.cordea.nut

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.RemoteInput
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import android.support.v7.app.NotificationCompat
import android.widget.RemoteViews

/**
 * Created by Yoshihiro Tanaka on 2017/05/30.
 */
class NotificationSender(private val context: Context) {

    private val manager: NotificationManager
        get() {
            return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }

    var title: String = ""

    var text: String = ""

    fun send(type: NotificationType) {
        when (type) {
            NotificationType.SIMPLE -> sendNotification()
            NotificationType.ACTION -> sendActionNotification()
            NotificationType.CUSTOM -> sendCustomNotification()
        }
    }

    fun getRemoteInputText(intent: Intent): CharSequence {
        val input = RemoteInput.getResultsFromIntent(intent)
        return input.getCharSequence(TextKey)
    }

    private fun sendCustomNotification() {
        val remoteView = RemoteViews(context.packageName, R.layout.view_notification)
                .apply {
                    setOnClickPendingIntent(R.id.a_button, createPendingIntent(ActionButtonA))
                    setOnClickPendingIntent(R.id.b_button, createPendingIntent(ActionButtonB))
                    setOnClickPendingIntent(R.id.c_button, createPendingIntent(ActionButtonC))
                    setTextViewText(R.id.title_text_view, title)
                }

        val expandRemoteView = RemoteViews(context.packageName, R.layout.view_notification_expand)
                .apply {
                    setOnClickPendingIntent(R.id.a_button, createPendingIntent(ActionButtonA))
                    setOnClickPendingIntent(R.id.b_button, createPendingIntent(ActionButtonB))
                    setOnClickPendingIntent(R.id.c_button, createPendingIntent(ActionButtonC))
                    setTextViewText(R.id.title_text_view, title)
                    setTextViewText(R.id.text_text_view, text)
                }

        val notification =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Notification
                            .Builder(context)
                            .setCustomContentView(remoteView)
                            .setCustomBigContentView(expandRemoteView)
                            .setSmallIcon(Icon.createWithResource(context, R.drawable.ic_android_black_24dp))
                } else {
                    Notification
                            .Builder(context)
                            .setContent(remoteView)
                            .setSmallIcon(R.drawable.ic_android_black_24dp)
                }

        notification
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build()
                .apply {
                    manager.notify(1, this)
                }
    }

    private fun sendActionNotification() {
        val notification = Notification
                .Builder(context)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentTitle(title)
                .setContentText(text)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notification
                    .setSmallIcon(Icon.createWithResource(context, R.drawable.ic_android_black_24dp))
        } else {
            notification
                    .setSmallIcon(R.drawable.ic_android_black_24dp)
        }

        val input = RemoteInput
                .Builder(TextKey)
                .setLabel(context.getString(R.string.notification_input_hint))
                .build()

        val intent = createPendingIntent(ActionMessage)
        val action =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Notification.Action
                            .Builder(Icon.createWithResource(context, R.drawable.ic_android_black_24dp),
                                    context.getString(R.string.notification_input_send), intent)
                } else {
                    Notification.Action
                            .Builder(R.drawable.ic_android_black_24dp,
                                    context.getString(R.string.notification_input_send), intent)
                }

        notification
                .build()
                .apply {
                    actions = arrayOf(action.addRemoteInput(input).build())
                    manager.notify(1, this)
                }
    }

    private fun sendNotification() {
        val notification = Notification
                .Builder(context)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentTitle(title)
                .setContentText(text)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notification.setSmallIcon(Icon.createWithResource(context, R.drawable.ic_android_black_24dp))
        } else {
            notification.setSmallIcon(R.drawable.ic_android_black_24dp)
        }

        manager.notify(1, notification.build())
    }

    private fun createPendingIntent(action: String): PendingIntent {
        return PendingIntent.getBroadcast(context, 0, Intent(action), 0)
    }

    companion object {
        private val TextKey = "TextKey"

        val ActionMessage = "jp.cordea.nut.ACTION_MESSAGE"
        val ActionButtonA = "jp.cordea.nut.ACTION_A"
        val ActionButtonB = "jp.cordea.nut.ACTION_B"
        val ActionButtonC = "jp.cordea.nut.ACTION_C"
    }
}
