package com.fansipan.callcolor.calltheme.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.os.Build
import android.os.CountDownTimer
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.ui.main.MainActivity
import com.fansipan.callcolor.calltheme.utils.Constants
import com.fansipan.callcolor.calltheme.utils.SharePreferenceUtils
import com.fansipan.callcolor.calltheme.utils.data.AvatarUtils
import com.fansipan.callcolor.calltheme.utils.data.IconCallUtils
import com.fansipan.callcolor.calltheme.utils.data.SpeedFlashUtils
import com.fansipan.callcolor.calltheme.utils.ex.availableToSetThemeCall
import com.fansipan.callcolor.calltheme.utils.ex.gone
import com.fansipan.callcolor.calltheme.utils.ex.initVibrator
import com.fansipan.callcolor.calltheme.utils.ex.isSdk26
import com.fansipan.callcolor.calltheme.utils.ex.show
import com.fansipan.callcolor.calltheme.utils.ex.startVibration
import com.fansipan.callcolor.calltheme.utils.ex.turnOffVibration

class IncomingCallService : Service() {

    val listAvatar = listOf<Int>(
        R.drawable.img_add_avatar,
        R.drawable.img_avater_1,
        R.drawable.img_avater_2,
        R.drawable.img_avater_3,
        R.drawable.img_avater_4,
        R.drawable.img_avater_5,
        R.drawable.img_avater_6,
        R.drawable.img_avater_7,
        R.drawable.img_avater_8,
        R.drawable.img_avater_9,
        R.drawable.img_avater_10,
        R.drawable.img_avater_11,
        R.drawable.img_avater_12,
        R.drawable.img_avater_13,
        R.drawable.img_avater_14,
        R.drawable.img_avater_15,
        R.drawable.img_avater_16,
        R.drawable.img_avater_17,
        R.drawable.img_avater_18,
        R.drawable.img_avater_19,
        R.drawable.img_avater_20,
        R.drawable.img_avater_21,
        R.drawable.img_avater_22,
        R.drawable.img_avater_23,
        R.drawable.img_avater_24,
        R.drawable.img_avater_25,
        R.drawable.img_avater_26,
        R.drawable.img_avater_27
    )
    companion object {
        const val TAG = "IncomingCallService"
        var phoneNumber = ""
        var nameContact = ""
        var onGetContact: (() -> Unit)? = null
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    private val phoneCallReceiver by lazy {
        PhoneCallReceiver2()
    }

    private var cameraManager: CameraManager? = null
    private var timeCountDown: CountDownTimer? = null

    //region theme-call
    private var windowManager: WindowManager? = null
    private var paramsIn: WindowManager.LayoutParams? = null
    private var view: View? = null

    //private lateinit var ctTimeCall: Chronometer
    private lateinit var tvSdt: TextView
    private lateinit var tvName: TextView
    private lateinit var ivAvatar: ImageView
    private lateinit var ivDecline: ImageView
    private lateinit var ivAccept: ImageView
    private lateinit var ivBackground: ImageView
    private lateinit var rlView: ConstraintLayout

    private lateinit var llBtnInCall: LinearLayout
    private lateinit var imgMicrophone: ImageView
    private lateinit var imgSpeaker: ImageView
    private lateinit var imgAddMember: ImageView
    private lateinit var imgHold: ImageView
    private lateinit var imgNewCall: ImageView
    private lateinit var imgKeyBoard: ImageView
    private lateinit var imgFinishCall: ImageView


    private var isMute: Boolean = false
    private var isSpeaker: Boolean = false
    private var isHold: Boolean = false

    //endregion
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startWithNotification() else startForeground(
            1, Notification()
        )
        val filters = IntentFilter().apply {
            addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
        }
        registerReceiver(phoneCallReceiver, filters)
        if (availableToSetThemeCall() && SharePreferenceUtils.isEnableThemeCall()) {
            initThemeCall()
        }

    }

    private fun initThemeCall() {
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        val keyguardLock = keyguardManager.newKeyguardLock(Context.KEYGUARD_SERVICE)
        keyguardLock.disableKeyguard()
        windowManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
        /*paramsIn = WindowManager.LayoutParams(
            -1, -1, if (isSdk26()) 2038 else 2010, 19399552, -3
        )*/
        paramsIn = if (Build.VERSION.SDK_INT >= 26) {
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
        val layoutInflater: LayoutInflater =
            this.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.view = layoutInflater.inflate(R.layout.layout_service_call_theme1, null as ViewGroup?)

        //init
        //ctTimeCall = view!!.findViewById(R.id.ct_time_call)
        tvSdt = view!!.findViewById(R.id.tv_sdt)
        tvName = view!!.findViewById(R.id.tv_name)
        ivBackground = view!!.findViewById(R.id.iv_background)
        ivAvatar = view!!.findViewById(R.id.iv_avatar)
        ivDecline = view!!.findViewById(R.id.iv_decline)
        ivAccept = view!!.findViewById(R.id.iv_accept)
        rlView = view!!.findViewById(R.id.rl_view)

        llBtnInCall = view!!.findViewById(R.id.llBtnInCall)
        imgMicrophone = view!!.findViewById(R.id.imgMicrophone)
        imgSpeaker = view!!.findViewById(R.id.imgSpeaker)
        imgAddMember = view!!.findViewById(R.id.imgAddMember)
        imgHold = view!!.findViewById(R.id.imgHold)
        imgNewCall = view!!.findViewById(R.id.imgNewCall)
        imgKeyBoard = view!!.findViewById(R.id.imgKeyBoard)
        imgFinishCall = view!!.findViewById(R.id.imgFinishCall)

        //background
        Glide.with(this).load(SharePreferenceUtils.getBackgroundChoose()).into(ivBackground)

        //photo avatar
        Glide.with(this).load(SharePreferenceUtils.getAvatarChoose()).into(ivAvatar)

        val posButton = SharePreferenceUtils.getIconCallChoose().toInt() - 1
        ivDecline.setImageResource(IconCallUtils.listIconCall[posButton].icon1)
        ivAccept.setImageResource(IconCallUtils.listIconCall[posButton].icon2)

        try {
            val posAvt = SharePreferenceUtils.getAvatarChoose()
            if (posAvt.length < 3) {
                Glide.with(this)
                    .asBitmap()
                    .load(listAvatar[posAvt.toInt()])
                    .into(ivAvatar)
                    .onLoadFailed(ContextCompat.getDrawable(this, AvatarUtils.listAvatar[1]))
            } else {
                Glide.with(this)
                    .asBitmap()
                    .load(SharePreferenceUtils.getAvatarChoose())
                    .into(ivAvatar)
                    .onLoadFailed(ContextCompat.getDrawable(this, AvatarUtils.listAvatar[1]))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ivAvatar.setImageResource(AvatarUtils.listAvatar[1])
        }

        tvSdt.text = phoneNumber
        tvName.text = nameContact

        val loadAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.jump_button)
        ivAccept.startAnimation(loadAnimation)
        ivDecline.startAnimation(loadAnimation)

        ivAccept.setOnClickListener {
            //ctTimeCall.visibility = View.VISIBLE
            //ctTimeCall.start()
            ivDecline.clearAnimation()
            ivAccept.clearAnimation()
            acceptCallPhone()
            stopFlash()
            turnOffVibration()

            ivAccept.gone()
            ivDecline.gone()
            llBtnInCall.show()
        }

        ivDecline.setOnClickListener {
            rlView.visibility = View.GONE
            declineCallPhone()
            stopFlash()
            turnOffVibration()
        }

        imgMicrophone.setOnClickListener {
            val telecomManager = getSystemService(TELECOM_SERVICE) as TelecomManager
            val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

            if (telecomManager != null && audioManager != null) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_PHONE_STATE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return@setOnClickListener
                }
                if (telecomManager.isInCall) {
                    isMute = !isMute
                    audioManager.mode = AudioManager.MODE_IN_CALL
                    audioManager.isMicrophoneMute = isMute
                }

                if (isMute) {
                    imgMicrophone.setImageResource(R.drawable.ic_theme_call_mute)
                } else {
                    imgMicrophone.setImageResource(R.drawable.ic_theme_call_mic)
                }
            }
        }
        imgSpeaker.setOnClickListener {
            val telecomManager = getSystemService(TELECOM_SERVICE) as TelecomManager
            val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

            try {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_PHONE_STATE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return@setOnClickListener
                }
                if (telecomManager.isInCall) {
                    isSpeaker = !isSpeaker
                    audioManager.mode = AudioManager.MODE_IN_CALL
                    audioManager.isSpeakerphoneOn = isSpeaker
                }
                if (isSpeaker) {
                    imgSpeaker.setImageResource(R.drawable.ic_theme_call_speaker_on)
                } else {
                    imgSpeaker.setImageResource(R.drawable.ic_theme_call_speaker_off)
                }
            } catch (_: Exception) {
            }
        }

        imgAddMember.setOnClickListener {

        }

        imgHold.setOnClickListener {
            val telecomManager = getSystemService(TELECOM_SERVICE) as TelecomManager
            if (telecomManager.isInCall) {
                //telecomManager.holdCall()
            }
        }
        imgNewCall.setOnClickListener {

        }
        imgKeyBoard.setOnClickListener {

        }

        imgFinishCall.setOnClickListener {
            try {
                val telecomManager: TelecomManager =
                    this.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
                if (ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ANSWER_PHONE_CALLS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return@setOnClickListener
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    telecomManager.endCall()
                }
            } catch (_: Exception) {

            }
            try {
                val tm: TelephonyManager =
                    this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                tm.javaClass.getMethod("endCall").invoke(tm)
            } catch (_: Exception) {

            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "detect_notification") {
            SharePreferenceUtils.init(this)
            if (SharePreferenceUtils.isEnableFlashMode()) {
                startFlash(100 * 2 + 50, 100)
            }
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(phoneCallReceiver)
        if (availableToSetThemeCall() && SharePreferenceUtils.isEnableThemeCall()) {
            view?.let { windowManager?.removeView(it) }
        }
    }

    inner class PhoneCallReceiver2 : BroadcastReceiver() {

        private val TAG = "PhoneCallReceiver2"
        private fun getContentRandom(list: MutableList<String>): String {
            repeat(5) {
                list.shuffle()
            }
            return list[0]
        }

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
                if (SharePreferenceUtils.isEnableFlashMode()) {
                    startFlash()
                }
                if (SharePreferenceUtils.isEnableVibrate()) {
                    initVibrator(context)
                    startVibration(0)
                }
                try {
                    if (availableToSetThemeCall() && SharePreferenceUtils.isEnableThemeCall()) {
                        initThemeCall()
                        onGetContact = {
                            tvSdt.text = phoneNumber
                            tvName.text = nameContact
                        }
                        view?.visibility = View.VISIBLE
                        windowManager?.addView(view, paramsIn)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                Log.d(TAG, "2.EXTRA_STATE_OFFHOOK")
            } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                Log.d(TAG, "3.EXTRA_STATE_IDLE")
                stopFlash()
                turnOffVibration()
                if (availableToSetThemeCall() && SharePreferenceUtils.isEnableThemeCall()) {
                    try {
                        view?.let { windowManager?.removeView(it) }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
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
        Log.d(TAG, "stopFlash")
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

    private fun declineCallPhone() {
        try {
            val telecomManager: TelecomManager =
                this.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ANSWER_PHONE_CALLS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                telecomManager.endCall()
            }
        } catch (_: Exception) {

        }
        try {
            val tm: TelephonyManager =
                this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            tm.javaClass.getMethod("endCall").invoke(tm)
        } catch (_: Exception) {

        }

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