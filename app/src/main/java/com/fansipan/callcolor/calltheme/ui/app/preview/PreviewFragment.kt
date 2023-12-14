package com.fansipan.callcolor.calltheme.ui.app.preview

import android.content.Context
import android.hardware.camera2.CameraManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.base.BaseFragment
import com.fansipan.callcolor.calltheme.databinding.FragmentPreviewBinding
import com.fansipan.callcolor.calltheme.utils.RingtonePlayerUtils
import com.fansipan.callcolor.calltheme.utils.SharePreferenceUtils
import com.fansipan.callcolor.calltheme.utils.data.AvatarUtils
import com.fansipan.callcolor.calltheme.utils.data.DataUtils
import com.fansipan.callcolor.calltheme.utils.data.IconCallUtils
import com.fansipan.callcolor.calltheme.utils.data.SpeedFlashUtils
import com.fansipan.callcolor.calltheme.utils.ex.clickSafe
import com.fansipan.callcolor.calltheme.utils.ex.initVibrator
import com.fansipan.callcolor.calltheme.utils.ex.showOrGone
import com.fansipan.callcolor.calltheme.utils.ex.startVibration
import com.fansipan.callcolor.calltheme.utils.ex.turnOffVibration

class PreviewFragment : BaseFragment() {


    private lateinit var binding: FragmentPreviewBinding
    private var ringtone: Ringtone? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() {
        DataUtils.callThemeEdit.let {item ->
            Glide.with(requireContext())
                .load(item.background)
                .into(binding.imgBackground)
            val posButton = item.buttonIndex.toInt() - 1
            binding.imgIconCall1.setImageResource(IconCallUtils.listIconCall[posButton].icon1)
            binding.imgIconCall2.setImageResource(IconCallUtils.listIconCall[posButton].icon2)
            val loadAnimation: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.jump_button)
            binding.imgIconCall1.startAnimation(loadAnimation)
            binding.imgIconCall2.startAnimation(loadAnimation)

            try {
                val posAvt = item.avatar
                if (posAvt.length < 3) {
                    Glide.with(this)
                        .load(AvatarUtils.listAvatar[posAvt.toInt()])
                        .into(binding.imgAvatar)
                        .onLoadFailed(ContextCompat.getDrawable(requireContext(), AvatarUtils.listAvatar[1]))
                } else {
                    Glide.with(this)
                        .load(item.avatar)
                        .into(binding.imgAvatar)
                        .onLoadFailed(ContextCompat.getDrawable(requireContext(), AvatarUtils.listAvatar[1]))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun initListener() {
        binding.imgClose.clickSafe { onBack() }
    }

    private var cameraManager: CameraManager? = null
    private var timeCountDown: CountDownTimer? = null
    private fun startFlash() {
        cameraManager = requireContext().getSystemService(Context.CAMERA_SERVICE) as CameraManager
        var isFlashOn = false
        val speed = SpeedFlashUtils.getFlashDelayByType(SharePreferenceUtils.getTypeFlash())
        timeCountDown =
            object : CountDownTimer(speed.toLong() * 10000 + speed.toLong() / 2, speed.toLong()) {
                override fun onTick(millisUntilFinished: Long) {
                    try {
                        if (SharePreferenceUtils.isEnableFlashMode()) {
                            isFlashOn = !isFlashOn
                            testFlash(isFlashOn)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFinish() {
                    stopFlash()
                }
            }
        if (SharePreferenceUtils.isEnableVibrate()) {
            turnOffVibration()
            initVibrator(requireContext())
            startVibration(0)
        }
        timeCountDown?.start()
    }

    fun playRingtone() {
        val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        ringtone = RingtoneManager.getRingtone(context, ringtoneUri)
        ringtone?.play()
    }

    override fun onResume() {
        super.onResume()
        if (requireArguments().getString("type", "") != "diy") {
            startFlash()
            playRingtone()
        }
    }

    override fun onPause() {
        super.onPause()
        stopFlash()
        ringtone?.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopFlash()
        ringtone?.stop()
    }

    private fun testFlash(isFlashOn: Boolean) {
        if (isFlashOn) {
            val cameraId = cameraManager?.cameraIdList?.get(0)
            cameraManager?.setTorchMode(cameraId!!, true)
        } else {
            val cameraId = cameraManager?.cameraIdList?.get(0)
            cameraManager?.setTorchMode(cameraId!!, false)
        }
    }

    private fun stopFlash() {
        try {
            timeCountDown?.cancel()
            val cameraId = cameraManager?.cameraIdList?.get(0)
            if (cameraId != null) {
                cameraManager?.setTorchMode(cameraId, false)
                cameraManager = null
            }
            turnOffVibration()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}