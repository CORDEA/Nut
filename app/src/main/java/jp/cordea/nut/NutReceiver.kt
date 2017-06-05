package jp.cordea.nut

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Created by Yoshihiro Tanaka on 2017/05/30.
 */
class NutReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let { context ->
            intent?.let {
                val action = it.action

                val sender = NotificationSender(context)
                val title = context.getString(R.string.reply_title)
                val text = context.getString(R.string.reply_text).format(
                        when (action) {
                            NotificationSender.ActionButtonA ->
                                context.getString(R.string.macadamia)
                            NotificationSender.ActionButtonB ->
                                context.getString(R.string.peanut)
                            NotificationSender.ActionButtonC ->
                                context.getString(R.string.almond)
                            NotificationSender.ActionMessage -> {
                                sender.getRemoteInputText(it)
                            }
                            else -> ""
                        }
                )

                sender.apply {
                    this.title = title
                    this.text = text
                }.send(NotificationType.SIMPLE)
            }
        }
    }
}
