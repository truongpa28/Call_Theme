package com.fansipan.callcolor.calltheme.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.fansipan.callcolor.calltheme.MyApplication
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.utils.SharePreferenceUtils
import com.fansipan.callcolor.calltheme.utils.data.SpeedFlashUtils
import com.fansipan.callcolor.calltheme.utils.ex.availableToSetThemeCall
import com.fansipan.callcolor.calltheme.utils.ex.gone
import com.fansipan.callcolor.calltheme.utils.ex.isSdk26
import com.fansipan.callcolor.calltheme.utils.ex.show
import com.fansipan.callcolor.calltheme.utils.ex.turnOffVibration

class LockCallActivity : AppCompatActivity() {

    companion object {
        const val TAG = "truongpa"
    }

    private var cameraManager: CameraManager? = null
    private var timeCountDown: CountDownTimer? = null

    lateinit var mWindowManager: WindowManager

    lateinit var mView: View

    lateinit var txtName: TextView
    lateinit var txtSdt: TextView

    lateinit var imgDecline: ImageView
    lateinit var imgAccept: ImageView

    lateinit var llBtnInCall: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)

        super.onCreate(savedInstanceState)
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        val keyguardLock = keyguardManager.newKeyguardLock(Context.KEYGUARD_SERVICE)
        keyguardLock.disableKeyguard()

        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager

        mView = View.inflate(applicationContext, R.layout.layout_call_theme, null)
        mView.setTag("CustomLockScreen")
        val mLayoutParams = if (Build.VERSION.SDK_INT >= 26) {
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                        or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                PixelFormat.TRANSLUCENT
            )
        } else {
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                4719872,
                PixelFormat.TRANSLUCENT
            )
        }
        mWindowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        window.attributes = mLayoutParams

        txtName = mView.findViewById(R.id.txtName)
        txtSdt = mView.findViewById(R.id.txtSdt)
        imgDecline = mView.findViewById(R.id.imgDecline)
        imgAccept = mView.findViewById(R.id.imgAccept)
        llBtnInCall = mView.findViewById(R.id.llBtnInCall)

        txtName.text = ThemCallService.nameContact
        txtSdt.text = ThemCallService.phoneNumber

        val loadAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.jump_button)
        imgAccept.startAnimation(loadAnimation)
        imgDecline.startAnimation(loadAnimation)

        imgDecline.setOnClickListener {
            declineCallPhone()
            stopFlash()
            turnOffVibration()
        }

        imgAccept.setOnClickListener {
            imgDecline.clearAnimation()
            imgAccept.clearAnimation()
            acceptCallPhone()
            stopFlash()
            turnOffVibration()

            imgAccept.gone()
            imgDecline.gone()
            llBtnInCall.show()
        }


        mView.visibility = View.VISIBLE
        mWindowManager.addView(mView, mLayoutParams)

    }

    private fun startFlash(timeLoop: Long = 100000, speed: Long? = null) {
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        Log.d(TAG, "startFlash")
        val typeFlash = SharePreferenceUtils.getTypeFlash()
        var isFlashOn = false
        val spx: Long = speed ?: if (SharePreferenceUtils.isEnableSpeedMode()) {
            SpeedFlashUtils.getFlashDelayByType(SharePreferenceUtils.getTypeFlash())
        } else {
            SpeedFlashUtils.SPEED_FLASH_DEFAULT.toLong()
        }
        timeCountDown = object : CountDownTimer(timeLoop, spx) {
            override fun onTick(millisUntilFinished: Long) {
                try {
                    turnFlashWithStyle(typeFlash, isFlashOn)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFinish() {
                stopFlash()
            }
        }
        timeCountDown?.start()
    }

    private fun turnFlashWithStyle(typeFlash: Int, isFlashOn: Boolean) {
        var isFlashOn1 = isFlashOn
        if (cameraManager == null) {
            cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        }
        if (typeFlash == 0) {//continuous
            isFlashOn1 = !isFlashOn1
            if (isFlashOn1) {
                val cameraId = cameraManager?.cameraIdList?.get(0)
                cameraManager?.setTorchMode(cameraId!!, true)
            } else {
                val cameraId = cameraManager?.cameraIdList?.get(0)
                cameraManager?.setTorchMode(cameraId!!, false)
            }
        } else {//sos
            val cameraId = cameraManager?.cameraIdList?.get(0)
            cameraManager?.setTorchMode(cameraId!!, true)
            Handler(Looper.getMainLooper()).postDelayed({
                cameraManager?.setTorchMode(cameraId!!, false)
            }, 40)
            Handler(Looper.getMainLooper()).postDelayed({
                cameraManager?.setTorchMode(cameraId!!, true)
            }, 80)
            Handler(Looper.getMainLooper()).postDelayed({
                cameraManager?.setTorchMode(cameraId!!, false)
            }, 120)
        }
    }

    private fun stopFlash() {
        timeCountDown?.cancel()
        val cameraId = cameraManager?.cameraIdList?.get(0)
        if (cameraId != null) {
            cameraManager?.setTorchMode(cameraId, false)
            cameraManager = null
        }
    }

    @SuppressLint("ServiceCast")
    private fun acceptCallPhone() {
        if (isSdk26()) { // Pris en charge Android >= 8.0
            val tm = this.getSystemService(TELECOM_SERVICE) as TelecomManager
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ANSWER_PHONE_CALLS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            tm.acceptRingingCall()
        }

        if (Build.VERSION.SDK_INT in 23..25) { // Hangup in Android 6.x and 7.x
            val mediaSessionManager =
                this.getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager

            if (mediaSessionManager != null) {
                try {
                    val mediaControllerList: List<MediaController> =
                        mediaSessionManager.getActiveSessions(
                            ComponentName(
                                this,
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

                }
            }
        }

        if (Build.VERSION.SDK_INT < 23) { // Prend en charge jusqu'à Android 5.1
            try {
                if (Build.MANUFACTURER.equals("HTC", ignoreCase = true)) { // Uniquement pour HTC
                    val audioManager: AudioManager =
                        this.getSystemService(AUDIO_SERVICE) as AudioManager
                    if (!audioManager.isWiredHeadsetOn) {
                        val i = Intent(Intent.ACTION_HEADSET_PLUG)
                        i.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY)
                        i.putExtra("state", 0)
                        i.putExtra("name", "Orasi")
                        try {
                            this.sendOrderedBroadcast(i, null)
                        } catch (e: java.lang.Exception) { /* Do Nothing */
                        }
                    }
                }
                Runtime.getRuntime().exec(
                    "input keyevent " + Integer.toString(KeyEvent.KEYCODE_HEADSETHOOK)
                )
            } catch (e: java.lang.Exception) {
                // Runtime.exec(String) had an I/O problem, try to fall back
                val enforcedPerm = "android.permission.CALL_PRIVILEGED"
                val btnDown = Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(
                    Intent.EXTRA_KEY_EVENT, KeyEvent(
                        KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK
                    )
                )
                val btnUp = Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(
                    Intent.EXTRA_KEY_EVENT, KeyEvent(
                        KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK
                    )
                )
                this.sendOrderedBroadcast(btnDown, enforcedPerm)
                this.sendOrderedBroadcast(btnUp, enforcedPerm)
            }
        }
    }



    override fun onDestroy() {
        removeView()
        super.onDestroy()
    }

    private fun removeView() {
        mWindowManager.removeView(mView)
        finish()
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
                try {
                    if (availableToSetThemeCall() && SharePreferenceUtils.isEnableThemeCall()) {
                        lock()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                Log.d(TAG, "2.EXTRA_STATE_OFFHOOK")
            } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                Log.d(TAG, "3.EXTRA_STATE_IDLE")
                if (availableToSetThemeCall() && SharePreferenceUtils.isEnableThemeCall()) {
                    try {

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

}