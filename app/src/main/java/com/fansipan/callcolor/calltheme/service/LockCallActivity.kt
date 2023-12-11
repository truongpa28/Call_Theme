package com.fansipan.callcolor.calltheme.service

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Chronometer
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.view.WindowCompat
import com.bumptech.glide.Glide
import com.fansipan.callcolor.calltheme.MyApplication
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.databinding.ItemFlashBinding
import com.fansipan.callcolor.calltheme.databinding.LayoutCallThemeBinding
import com.fansipan.callcolor.calltheme.ui.main.MainActivity
import com.fansipan.callcolor.calltheme.utils.Constants
import com.fansipan.callcolor.calltheme.utils.SharePreferenceUtils
import com.fansipan.callcolor.calltheme.utils.data.AvatarUtils
import com.fansipan.callcolor.calltheme.utils.data.IconCallUtils
import com.fansipan.callcolor.calltheme.utils.data.SpeedFlashUtils
import com.fansipan.callcolor.calltheme.utils.ex.availableToSetThemeCall
import com.fansipan.callcolor.calltheme.utils.ex.gone
import com.fansipan.callcolor.calltheme.utils.ex.initVibrator
import com.fansipan.callcolor.calltheme.utils.ex.show
import com.fansipan.callcolor.calltheme.utils.ex.showOrGone
import com.fansipan.callcolor.calltheme.utils.ex.startVibration
import com.fansipan.callcolor.calltheme.utils.ex.turnOffVibration
import com.google.android.material.snackbar.Snackbar

class LockCallActivity : AppCompatActivity() {

    companion object {
        const val TAG = "truongpa"
    }

    private var cameraManager: CameraManager? = null
    private var timeCountDown: CountDownTimer? = null

    private lateinit var binding: LayoutCallThemeBinding

    val snackBar by lazy {
        Snackbar.make(
            binding.root, getString(R.string.feature_is_being_developed), Snackbar.LENGTH_SHORT
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        //region Status Bar Color
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        //endregion

        super.onCreate(savedInstanceState)

        initVibrator(this)
        SharePreferenceUtils.init(this)


        //region KEYGUARD_SERVICE
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        val keyguardLock = keyguardManager.newKeyguardLock(Context.KEYGUARD_SERVICE)
        keyguardLock.disableKeyguard()
        //endregion

        //region LayoutParams
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
        window.attributes = mLayoutParams
        //endregion

        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager

        binding = LayoutCallThemeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ThemCallService.nameContact.let {
            binding.txtName.showOrGone(it.isEmpty())
            binding.txtName.text = it
        }
        binding.txtSdt.text = ThemCallService.phoneNumber

        val posButton = SharePreferenceUtils.getIconCallChoose().toInt() - 1
        binding.imgDecline.setImageResource(IconCallUtils.listIconCall[posButton].icon1)
        binding.imgAccept.setImageResource(IconCallUtils.listIconCall[posButton].icon2)

        //background
        Glide.with(this).load(SharePreferenceUtils.getBackgroundChoose()).into(binding.imgBackground)

        try {
            val posAvt = SharePreferenceUtils.getAvatarChoose()
            if (posAvt.length < 3) {
                Glide.with(this)
                    .asBitmap()
                    .load(AvatarUtils.listAvatar[posAvt.toInt()])
                    .into(binding.imgAvatar)
                    .onLoadFailed(ContextCompat.getDrawable(this, AvatarUtils.listAvatar[1]))
            } else {
                Glide.with(this)
                    .asBitmap()
                    .load(SharePreferenceUtils.getAvatarChoose())
                    .into(binding.imgAvatar)
                    .onLoadFailed(ContextCompat.getDrawable(this, AvatarUtils.listAvatar[1]))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val loadAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.jump_button)
        binding.imgAccept.startAnimation(loadAnimation)
        binding.imgDecline.startAnimation(loadAnimation)

        initListener()


        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onClickBack()
            }
        })

        try {
            if (availableToSetThemeCall() && SharePreferenceUtils.isEnableThemeCall()) {
                if (SharePreferenceUtils.isEnableFlashMode()) {
                    startFlash()
                }
                if (SharePreferenceUtils.isEnableVibrate()) {
                    startVibration(0)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val filters = IntentFilter().apply {
            addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
        }
        registerReceiver(broadcastCall, filters)
    }

    fun onClickBack() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastCall)
    }


    var secondsCall = 0
    val handler = Handler()
    private var runnable: Runnable? = null

    @SuppressLint("SetTextI18n")
    private fun initListener() {
        binding.imgDecline.setOnClickListener {
            binding.txtStatus.text = getString(R.string.call_ended)
            LockCallUtil.declineCallPhone(this)
            stopFlash()
            turnOffVibration()
        }

        binding.imgAccept.setOnClickListener {
            binding.imgDecline.clearAnimation()
            binding.imgAccept.clearAnimation()
            LockCallUtil.acceptCallPhone(this)
            stopFlash()
            turnOffVibration()

            runnable = object : Runnable {
                override fun run() {
                    val minutes = (secondsCall) / 60
                    val secs = secondsCall % 60
                    val timeString = String.format("%02d:%02d", minutes, secs)
                    binding.txtStatus.text = timeString
                    secondsCall++
                    handler.postDelayed(this, 1000)
                }
            }
            handler.post(runnable as Runnable)

            binding.imgAccept.gone()
            binding.imgDecline.gone()
            binding.llBtnInCall.show()
        }

        binding.imgMicrophone.setOnClickListener {
            snackBar.show()
        }
        binding.imgSpeaker.setOnClickListener {
            snackBar.show()
        }
        binding.imgAddMember.setOnClickListener {
            snackBar.show()
        }
        binding.imgHold.setOnClickListener {
            snackBar.show()
        }
        binding.imgNewCall.setOnClickListener {
            snackBar.show()
        }
        binding.imgKeyBoard.setOnClickListener {
            snackBar.show()
        }
        binding.imgFinishCall.setOnClickListener {
            LockCallUtil.declineCallPhone(this)
            runnable?.let { it1 -> handler.removeCallbacks(it1) }
            binding.txtStatus.text = "(${binding.txtStatus.text}) " + getString(R.string.call_ended)
        }

    }

    //region Flash
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

    //endregion


    val broadcastCall = object : BroadcastReceiver() {
        private val TAG = "truongpa"

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

            } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {

            } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                Log.d(TAG, "3.EXTRA_STATE_IDLE")
                if (availableToSetThemeCall() && SharePreferenceUtils.isEnableThemeCall()) {
                    try {
                        Handler(Looper.getMainLooper()).postDelayed({
                            finish()
                        }, 1000L)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

}