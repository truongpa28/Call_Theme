package com.fansipan.callcolor.calltheme.service

import android.content.Context
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class NotificationService : NotificationListenerService(){
    var statusNote: StatusBarNotification? = null
    var scontext: Context? = null

    override fun getActiveNotifications(): Array<StatusBarNotification?>? {
        return super.getActiveNotifications()
    }

    override fun onCreate() {
        super.onCreate()
        scontext = applicationContext
    }

    override fun onNotificationPosted(statusBarNotification: StatusBarNotification) {
        super.onNotificationPosted(statusBarNotification)
        try {
            statusNote = statusBarNotification
            val packageName = statusBarNotification.packageName
            val intent = Intent("Msg")
            intent.putExtra("package", packageName)
            LocalBroadcastManager.getInstance(scontext!!).sendBroadcast(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onNotificationRemoved(statusBarNotification: StatusBarNotification?) {
        super.onNotificationRemoved(statusBarNotification)
    }
}