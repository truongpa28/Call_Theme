package com.fansipan.callcolor.calltheme.service

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telecom.CallAudioState
import android.telephony.TelephonyManager
import android.util.Log
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.bumptech.glide.Glide
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.databinding.LayoutCallThemeBinding
import com.fansipan.callcolor.calltheme.utils.SharePreferenceUtils
import com.fansipan.callcolor.calltheme.utils.data.AvatarUtils
import com.fansipan.callcolor.calltheme.utils.data.IconCallUtils
import com.fansipan.callcolor.calltheme.utils.data.ThemeCallUtils
import com.fansipan.callcolor.calltheme.utils.ex.availableToSetThemeCall
import com.fansipan.callcolor.calltheme.utils.ex.gone
import com.fansipan.callcolor.calltheme.utils.ex.show
import com.fansipan.callcolor.calltheme.utils.ex.showOrGone
import com.fansipan.callcolor.calltheme.utils.ex.turnOffVibration
import com.google.android.material.snackbar.Snackbar


class LockCallActivity : AppCompatActivity() {

    companion object {
        const val TAG = "truongpa"
    }

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


        FlashLockCallUtils.stopFlash()
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

        binding = LayoutCallThemeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ThemCallService.nameContact.let {
            binding.txtName.showOrGone(it != "")
            binding.txtName.text = it
        }
        binding.txtSdt.text = ThemCallService.phoneNumber

        val posButton = SharePreferenceUtils.getIconCallChoose().toInt() - 1
        binding.imgDecline.setImageResource(IconCallUtils.listIconCall[posButton].icon1)
        binding.imgAccept.setImageResource(IconCallUtils.listIconCall[posButton].icon2)

        //background
        Glide.with(this).load(SharePreferenceUtils.getBackgroundChoose())
            .into(binding.imgBackground)

        try {
            val posAvt = SharePreferenceUtils.getAvatarChoose()
            if (posAvt.length < 3) {
                Glide.with(this)
                    .load(AvatarUtils.listAvatar[posAvt.toInt()])
                    .into(binding.imgAvatar)
                    .onLoadFailed(ContextCompat.getDrawable(this, AvatarUtils.listAvatar[1]))
            } else {
                Glide.with(this)
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

        intent.action?.let { action ->
            if (action == "ClickResume") {
                binding.imgAccept.clearAnimation()
                binding.imgAccept.clearAnimation()
                secondsCall = System.currentTimeMillis() - ThemeCallUtils.timeStartCalling
                secondsCall /= 1000
                runnableCountTime = object : Runnable {
                    override fun run() {
                        val minutes = (secondsCall) / 60
                        val secs = secondsCall % 60
                        val timeString = String.format("%02d:%02d", minutes, secs)
                        binding.txtStatus.text = timeString
                        secondsCall++
                        handlerCountTime.postDelayed(this, 1000)
                    }
                }
                handlerCountTime.post(runnableCountTime as Runnable)
                binding.imgAccept.gone()
                binding.imgDecline.gone()
                binding.llBtnInCall.show()
            }
        }


        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onClickBack()
            }
        })

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


    var secondsCall: Long = 0
    val handlerCountTime = Handler()
    private var runnableCountTime: Runnable? = null

    private var isMuteMic = false
    private var isSpeaker = false

    @SuppressLint("SetTextI18n")
    private fun initListener() {
        binding.imgDecline.setOnClickListener {
            FlashLockCallUtils.stopFlash()
            turnOffVibration()
            binding.txtStatus.text = getString(R.string.call_ended)
            LockCallUtil.declineCallPhone(this)
        }

        binding.imgAccept.setOnClickListener {
            FlashLockCallUtils.stopFlash()
            turnOffVibration()
            binding.imgDecline.clearAnimation()
            binding.imgAccept.clearAnimation()
            NotificationLockCallUtils.startCallingNotification(this@LockCallActivity)
            LockCallUtil.acceptCallPhone(this)
            secondsCall = 0L
            ThemeCallUtils.timeStartCalling = System.currentTimeMillis()
            runnableCountTime = object : Runnable {
                override fun run() {
                    val minutes = (secondsCall) / 60
                    val secs = secondsCall % 60
                    val timeString = String.format("%02d:%02d", minutes, secs)
                    binding.txtStatus.text = timeString
                    secondsCall++
                    handlerCountTime.postDelayed(this, 1000)
                }
            }
            handlerCountTime.post(runnableCountTime as Runnable)

            binding.imgAccept.gone()
            binding.imgDecline.gone()
            binding.llBtnInCall.show()
        }

        binding.imgMicrophone.setOnClickListener {
            isMuteMic = !isMuteMic
            val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.mode = AudioManager.MODE_IN_CALL
            audioManager.isMicrophoneMute = isMuteMic
            if (isMuteMic) {
                binding.imgMicrophone.setImageResource(R.drawable.ic_theme_call_mute)
            } else {
                binding.imgMicrophone.setImageResource(R.drawable.ic_theme_call_mic)
            }
        }
        binding.imgSpeaker.setOnClickListener {
            isSpeaker = !isSpeaker
            val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.mode = AudioManager.MODE_IN_CALL
            val earpiece = CallAudioState.ROUTE_WIRED_OR_EARPIECE
            val speaker = CallAudioState.ROUTE_SPEAKER
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                PhoneCallHighServiceAPI33.sInstance?.setAudioRoute(if (!isSpeaker) earpiece else speaker)
            } else {
                audioManager.isSpeakerphoneOn = isSpeaker
            }
            if (isSpeaker) {
                binding.imgSpeaker.setImageResource(R.drawable.ic_theme_call_speaker_on)
            } else {
                binding.imgSpeaker.setImageResource(R.drawable.ic_theme_call_speaker_off)
            }
        }
        binding.imgAddMember.setOnClickListener {
            snackBar.show()
        }
        binding.imgHold.setOnClickListener {
            snackBar.show()
            /*val telecomManager = getSystemService(TELECOM_SERVICE) as TelecomManager

            if (telecomManager != null) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_PHONE_STATE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    if (telecomManager.isInCall) {
                        telecomManager.javaClass.getMethod("holdCall").invoke(telecomManager)
                    }
                }

            }

            try {
                val tm: TelephonyManager =
                    getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                tm.javaClass.getMethod("holdCall").invoke(tm)
            } catch (e: Exception) { e.printStackTrace() }*/
        }
        binding.imgNewCall.setOnClickListener {
            snackBar.show()
        }
        binding.imgKeyBoard.setOnClickListener {
            snackBar.show()
        }
        binding.imgFinishCall.setOnClickListener {
            LockCallUtil.declineCallPhone(this)
            runnableCountTime?.let { it1 -> handlerCountTime.removeCallbacks(it1) }
            binding.txtStatus.text = "(${binding.txtStatus.text}) " + getString(R.string.call_ended)
        }

    }


    val broadcastCall = object : BroadcastReceiver() {
        private val TAG = "truongpa"

        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context?, intent: Intent?) {

            if (intent?.action == null) {
                return
            }

            context?.let {
                SharePreferenceUtils.init(it)
            }
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {

            } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                FlashLockCallUtils.stopFlash()
                turnOffVibration()
            } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                Log.d(TAG, "3.EXTRA_STATE_IDLE")
                FlashLockCallUtils.stopFlash()
                turnOffVibration()
                if (availableToSetThemeCall() && SharePreferenceUtils.isEnableThemeCall()) {
                    try {
                        NotificationLockCallUtils.hide(this@LockCallActivity)
                        finish()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        finish()
                    }
                }
            }
        }
    }

}