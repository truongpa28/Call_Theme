package com.fansipan.callcolor.calltheme.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.utils.Constants

object NotificationLockCallUtils {

    const val idNotification = 6112023

    fun startRingNotification(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = Constants.NOTIFICATION_CHANNEL_NAME
            val description = Constants.NOTIFICATION_DETAILS
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID2, name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        val intentOpenApp = Intent(context, LockCallActivity::class.java).apply {
            action = "CLickAll"
        }
        val pendingIntentOpen = PendingIntent.getActivity(
            context,
            0,
            intentOpenApp,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val intentAccept = Intent(context, LockCallActivity::class.java).apply {
            action = "Accept"
        }
        val pendingIntentAccept = PendingIntent.getActivity(
            context,
            0,
            intentAccept,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val intentDecline = Intent(context, LockCallActivity::class.java).apply {
            action = "Decline"
        }
        val pendingIntentDecline = PendingIntent.getActivity(
            context,
            0,
            intentDecline,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        //create notify
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
            context, Constants.NOTIFICATION_CHANNEL_ID2
        ).apply {
            priority = NotificationCompat.PRIORITY_MAX
        }

        val bigView : RemoteViews = RemoteViews(context.packageName, R.layout.layout_calling_notification_big)
        val smallView : RemoteViews = RemoteViews(context.packageName, R.layout.layout_calling_notification_small)

        bigView.setOnClickPendingIntent(R.id.viewAll, pendingIntentOpen)
        smallView.setOnClickPendingIntent(R.id.viewAll, pendingIntentOpen)

        bigView.setOnClickPendingIntent(R.id.imgAccept, pendingIntentAccept)
        smallView.setOnClickPendingIntent(R.id.imgAccept, pendingIntentAccept)

        bigView.setOnClickPendingIntent(R.id.imgDecline, pendingIntentDecline)
        smallView.setOnClickPendingIntent(R.id.imgDecline, pendingIntentDecline)

        bigView.setTextViewText(R.id.txtPhoneNumber, ThemCallService.phoneNumber)
        smallView.setTextViewText(R.id.txtPhoneNumber, ThemCallService.phoneNumber)

        bigView.setTextViewText(R.id.txtStatus, context.getString(R.string.there_an_incoming_call))
        smallView.setTextViewText(R.id.txtStatus, context.getString(R.string.there_an_incoming_call))

        val notification = notificationBuilder.setOngoing(true).setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setCustomContentView(smallView)
            .setCustomBigContentView(bigView)
            .build()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context).notify(idNotification, notification)
            Log.e("truongpa", "POST_NOTIFICATIONS: DONE")
        } else {
            Log.e("truongpa", "POST_NOTIFICATIONS: NO")
        }
    }

    fun startCallingNotification(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = Constants.NOTIFICATION_CHANNEL_NAME
            val description = Constants.NOTIFICATION_DETAILS
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID2, name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        val intentOpenApp = Intent(context, LockCallActivity::class.java).apply {
            action = "ClickResume"
        }
        val pendingIntentOpen = PendingIntent.getActivity(
            context,
            0,
            intentOpenApp,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val intentDecline = Intent(context, LockCallActivity::class.java).apply {
            action = "ClickResume"
        }
        val pendingIntentDecline = PendingIntent.getActivity(
            context,
            0,
            intentDecline,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        //create notify
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
            context, Constants.NOTIFICATION_CHANNEL_ID2
        ).apply {
            priority = NotificationCompat.PRIORITY_LOW
        }

        val bigView : RemoteViews = RemoteViews(context.packageName, R.layout.layout_calling_notification_big)
        val smallView : RemoteViews = RemoteViews(context.packageName, R.layout.layout_calling_notification_small)

        bigView.setOnClickPendingIntent(R.id.viewAll, pendingIntentOpen)
        smallView.setOnClickPendingIntent(R.id.viewAll, pendingIntentOpen)

        bigView.setOnClickPendingIntent(R.id.imgDecline, pendingIntentDecline)
        smallView.setOnClickPendingIntent(R.id.imgDecline, pendingIntentDecline)

        bigView.setTextViewText(R.id.txtPhoneNumber, ThemCallService.phoneNumber)
        smallView.setTextViewText(R.id.txtPhoneNumber, ThemCallService.phoneNumber)

        bigView.setTextViewText(R.id.txtStatus, context.getString(R.string.call_in_progress))
        smallView.setTextViewText(R.id.txtStatus, context.getString(R.string.call_in_progress))

        bigView.setViewVisibility(R.id.imgAccept, View.GONE)
        smallView.setViewVisibility(R.id.imgAccept, View.GONE)

        val notification = notificationBuilder.setOngoing(true).setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setCustomContentView(smallView)
            .setCustomBigContentView(bigView)
            .build()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context).notify(idNotification, notification)
            Log.e("truongpa", "POST_NOTIFICATIONS: DONE")
        } else {
            Log.e("truongpa", "POST_NOTIFICATIONS: NO")
        }
    }

    fun hide(context: Context) {
        val manager = (context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager)
        manager.cancel(idNotification)
    }

}