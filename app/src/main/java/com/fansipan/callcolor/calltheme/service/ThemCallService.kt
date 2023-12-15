package com.fansipan.callcolor.calltheme.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.camera2.CameraManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.PowerManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.fansipan.callcolor.calltheme.MyApplication
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.ui.main.MainActivity
import com.fansipan.callcolor.calltheme.utils.Constants
import com.fansipan.callcolor.calltheme.utils.SharePreferenceUtils
import com.fansipan.callcolor.calltheme.utils.data.ThemeCallUtils
import com.fansipan.callcolor.calltheme.utils.ex.availableToSetThemeCall
import com.fansipan.callcolor.calltheme.utils.ex.initVibrator
import com.fansipan.callcolor.calltheme.utils.ex.startVibration
import com.fansipan.callcolor.calltheme.utils.ex.turnOffVibration

class ThemCallService : Service() {
    companion object {
        const val TAG = "IncomingCallService"
        var phoneNumber = ""
        var nameContact = ""
        var onGetContact: (() -> Unit)? = null
    }


    private var cameraManager: CameraManager? = null
    private var ringtone: Ringtone? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    private val phoneCallReceiver by lazy {
        PhoneCallReceiver2()
    }

    //endregion
    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startWithNotification() else startForeground(
            1, Notification()
        )
        val filters = IntentFilter().apply {
            addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
        }
        registerReceiver(phoneCallReceiver, filters)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(phoneCallReceiver)
        ringtone?.stop()
        if (availableToSetThemeCall() && SharePreferenceUtils.isEnableThemeCall()) {
            (application as? MyApplication)?.finishActivity()
        }
    }

    inner class PhoneCallReceiver2 : BroadcastReceiver() {
        private val TAG = "PhoneCallReceiver2"

        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context?, intent: Intent?) {

            if (intent?.action == null) {
                return
            }

            cameraManager = context?.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            context.let {
                SharePreferenceUtils.init(it)
            }
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                LockCallUtil.timeStartCalling = 0L
                LockCallUtil.isEndCall = false
                try {
                    if (availableToSetThemeCall() && SharePreferenceUtils.isEnableThemeCall()) {
                        lock()
                        initVibrator(context)
                        val ringtoneUri =
                            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                        ringtone = RingtoneManager.getRingtone(context, ringtoneUri)
                        ringtone?.play()
                        try {
                            if (SharePreferenceUtils.isEnableFlashMode()) {
                                FlashLockCallUtils.startFlash(context)
                            }
                            if (SharePreferenceUtils.isEnableVibrate()) {
                                startVibration(0)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    NotificationLockCallUtils.startRingNotification(context)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                ringtone?.stop()
                FlashLockCallUtils.stopFlash()
                turnOffVibration()
            } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                LockCallUtil.isEndCall = true
                FlashLockCallUtils.stopFlash()
                turnOffVibration()
                NotificationLockCallUtils.hide(context)
                ringtone?.stop()
                if (availableToSetThemeCall() && SharePreferenceUtils.isEnableThemeCall()) {
                    try {
                        Handler(Looper.getMainLooper()).postDelayed({
                            (application as? MyApplication)?.finishActivity()
                        }, 1000L)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    @SuppressLint("InvalidWakeLockTag")
    private fun lock() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or
                    PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG"
        )
        wakeLock.acquire()
        wakeLock.release()
        val intent = Intent(this, LockCallActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        Log.e("truongpa", "Call_startActivity")
        startActivity(intent)
    }
}

fun Service.startWithNotification() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_NONE
        )
        val manager = (getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(notificationChannel)

        val intentOpenApp = Intent(this, MainActivity::class.java).apply {}
        val pendingIntentOpenApp = PendingIntent.getActivity(
            this,
            0,
            intentOpenApp,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        //create notify
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
            this, Constants.NOTIFICATION_CHANNEL_ID
        ).apply {
            priority = NotificationCompat.PRIORITY_LOW
        }
        val notification = notificationBuilder.setOngoing(true).setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Theme Call detect incoming call")
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE).setContentIntent(pendingIntentOpenApp)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC).build()
        startForeground(2, notification)
    }

}