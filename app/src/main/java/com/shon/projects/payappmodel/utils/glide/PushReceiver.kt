package com.shon.projects.payappmodel.utils.glide

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.util.Log


class PushReceiver : BroadcastReceiver() {

    val ACTION_SNOOZE = "OK"
    val EXTRA_NOTIFICATION_ID = "notification-id"

    private val TAG = "receiver"

    override fun onReceive(context: Context?, intent: Intent?) {
        if (ACTION_SNOOZE == intent?.action) {
            val notificationId: Int? = intent.extras?.getInt(EXTRA_NOTIFICATION_ID)
            Log.e(TAG, "Cancel notification with id $notificationId")
            val notificationmanager =
                context?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            if (notificationId != null)
                notificationmanager.cancel(notificationId)
        }
    }


}