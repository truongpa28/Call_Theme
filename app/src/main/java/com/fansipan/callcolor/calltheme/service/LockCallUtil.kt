package com.fansipan.callcolor.calltheme.service

import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.os.Build
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.fansipan.callcolor.calltheme.utils.ex.isSdk26

object LockCallUtil {

    @SuppressLint("ServiceCast")
    fun acceptCallPhone(context : Context) {
        if (isSdk26()) {
            val tm = context.getSystemService(AppCompatActivity.TELECOM_SERVICE) as TelecomManager
            if (ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ANSWER_PHONE_CALLS
                ) != PackageManager.PERMISSION_GRANTED
            ) { return }
            tm.acceptRingingCall()
        }

        if (Build.VERSION.SDK_INT in 23..25) { // Hangup in Android 6.x and 7.x
            val mediaSessionManager =
                context.getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager
            try {
                val mediaControllerList: List<MediaController> =
                    mediaSessionManager.getActiveSessions(
                        ComponentName(
                            context,
                            NotificationService::class.java
                        )
                    )

                for (m in mediaControllerList) {
                    if ("com.android.server.telecom" == m.packageName) {
                        m.dispatchMediaButtonEvent(
                            KeyEvent(
                                KeyEvent.ACTION_DOWN,
                                KeyEvent.KEYCODE_HEADSETHOOK
                            )
                        )
                        m.dispatchMediaButtonEvent(
                            KeyEvent(
                                KeyEvent.ACTION_UP,
                                KeyEvent.KEYCODE_HEADSETHOOK
                            )
                        )
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun declineCallPhone(context: Context) {
        try {
            val telecomManager: TelecomManager =
                context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
            if (ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ANSWER_PHONE_CALLS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                telecomManager.endCall()
            }
        } catch (e: Exception) { e.printStackTrace() }
        try {
            val tm: TelephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            tm.javaClass.getMethod("endCall").invoke(tm)
        } catch (e: Exception) { e.printStackTrace() }
    }

}