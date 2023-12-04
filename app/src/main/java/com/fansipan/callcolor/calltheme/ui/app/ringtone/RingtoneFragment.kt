package com.fansipan.callcolor.calltheme.ui.app.ringtone

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.base.BaseFragment
import com.fansipan.callcolor.calltheme.databinding.FragmentRingtoneBinding
import com.fansipan.callcolor.calltheme.utils.RealPathUtil
import com.fansipan.callcolor.calltheme.utils.data.DataUtils
import com.fansipan.callcolor.calltheme.utils.ex.clickSafe
import com.fansipan.callcolor.calltheme.utils.ex.showToast

class RingtoneFragment : BaseFragment() {


    private lateinit var binding: FragmentRingtoneBinding

    private var isPlay = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRingtoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() {
        if (isPlay) {
            binding.imgPlay.setImageResource(R.drawable.ic_pause_gun)
        } else {
            binding.imgPlay.setImageResource(R.drawable.ic_play_gun)
        }
    }

    private fun initListener() {
        binding.imgBack.clickSafe { onBack() }


        binding.llChooseRingtone.clickSafe {
            findNavController().navigate(R.id.action_ringtoneFragment_to_chooseRingtoneFragment)
        }


        binding.llChooseAudio.clickSafe {
            chooseAudio()
        }
    }

    private fun chooseAudio() {
        if (Build.VERSION.SDK_INT >= 33) {
            checkPermissionAndPickAudioMediaAudio()
        } else {
            checkPermissionAndPickAudioReadExternal()
        }
    }

    private fun pickAudio() {
        imagePicker.launch("audio/*")
    }

    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                //DataUtils.callThemeEdit.background = RealPathUtil.getRealPath(requireContext(), uri)
                //findNavController().popBackStack()
            }
        }

    private fun checkPermissionAndPickAudioReadExternal() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            pickAudio()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkPermissionAndPickAudioMediaAudio() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_MEDIA_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            pickAudio()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_AUDIO)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                pickAudio()
            } else {
                requireContext().showToast(getString(R.string.permission_denied))
            }
        }
}