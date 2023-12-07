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
import android.graphics.PixelFormat
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
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
import com.fansipan.callcolor.calltheme.utils.ex.startVibration
import com.fansipan.callcolor.calltheme.utils.ex.turnOffVibration

class LockCallActivity : AppCompatActivity() {

    companion object {
        const val TAG = "truongpa"
    }

    private var cameraManager: CameraManager? = null
    private var timeCountDown: CountDownTimer? = null

    private lateinit var binding: LayoutCallThemeBinding

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


        binding.txtName.text = ThemCallService.nameContact
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

    }

    fun onClickBack() {
        finish()
    }

    private fun initListener() {
        binding.imgDecline.setOnClickListener {
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

            binding.imgAccept.gone()
            binding.imgDecline.gone()
            binding.llBtnInCall.show()
        }

        binding.imgMicrophone.setOnClickListener {

        }
        binding.imgSpeaker.setOnClickListener {

        }
        binding.imgAddMember.setOnClickListener {

        }
        binding.imgHold.setOnClickListener {

        }
        binding.imgNewCall.setOnClickListener {

        }
        binding.imgKeyBoard.setOnClickListener {

        }
        binding.imgFinishCall.setOnClickListener {
            LockCallUtil.declineCallPhone(this)
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


    val sert = object : BroadcastReceiver() {
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
                try {
                    if (availableToSetThemeCall() && SharePreferenceUtils.isEnableThemeCall()) {
                        startFlash()
                        startVibration(0)
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
                        finish()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

}