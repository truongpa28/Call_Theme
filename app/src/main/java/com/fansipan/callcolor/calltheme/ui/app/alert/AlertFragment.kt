package com.fansipan.callcolor.calltheme.ui.app.alert

import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.base.BaseFragment
import com.fansipan.callcolor.calltheme.databinding.FragmentAlertBinding
import com.fansipan.callcolor.calltheme.utils.SharePreferenceUtils
import com.fansipan.callcolor.calltheme.utils.data.SpeedFlashUtils
import com.fansipan.callcolor.calltheme.utils.ex.clickSafe
import com.fansipan.callcolor.calltheme.utils.ex.initVibrator
import com.fansipan.callcolor.calltheme.utils.ex.startVibration
import com.fansipan.callcolor.calltheme.utils.ex.turnOffVibration

class AlertFragment : BaseFragment() {

    private lateinit var binding: FragmentAlertBinding

    private var isPlay = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() {
        binding.swEnableFlash.isChecked = SharePreferenceUtils.isEnableFlashMode()
        binding.swEnableVibrate.isChecked = SharePreferenceUtils.isEnableVibrate()
        if (isPlay) {
            binding.imgPlay.setImageResource(R.drawable.ic_pause_gun)
        } else {
            binding.imgPlay.setImageResource(R.drawable.ic_play_gun)
        }
    }

    private fun initListener() {
        binding.imgBack.clickSafe { onBack() }

        binding.llChooseFlash.clickSafe {
            findNavController().navigate(R.id.action_alertFragment_to_flashFragment)
        }

        binding.llChooseVibrate.clickSafe {
            findNavController().navigate(R.id.action_alertFragment_to_vibrateFragment)
            stopFlash()
        }

        binding.swEnableFlash.clickSafe {
            SharePreferenceUtils.setEnableFlashMode(binding.swEnableFlash.isChecked)
            stopFlash()
        }

        binding.swEnableVibrate.clickSafe {
            SharePreferenceUtils.setEnableVibrate(binding.swEnableVibrate.isChecked)
            stopFlash()
        }

        binding.imgPlay.clickSafe {
            isPlay = !isPlay
            if (isPlay) {
                startFlash()
                binding.imgPlay.setImageResource(R.drawable.ic_pause_gun)
            } else {
                binding.imgPlay.setImageResource(R.drawable.ic_play_gun)
                stopFlash()
            }
        }
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
                    isPlay = false
                }
            }
        if (SharePreferenceUtils.isEnableVibrate()) {
            turnOffVibration()
            initVibrator(requireContext())
            startVibration(-1)
        }
        timeCountDown?.start()
    }

    override fun onPause() {
        super.onPause()
        stopFlash()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopFlash()
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
            isPlay = false
            binding.imgPlay.setImageResource(R.drawable.ic_play_gun)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}