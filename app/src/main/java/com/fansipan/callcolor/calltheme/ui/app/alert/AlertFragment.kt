package com.fansipan.callcolor.calltheme.ui.app.alert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.base.BaseFragment
import com.fansipan.callcolor.calltheme.databinding.FragmentAlertBinding
import com.fansipan.callcolor.calltheme.utils.SharePreferenceUtils
import com.fansipan.callcolor.calltheme.utils.ex.clickSafe

class AlertFragment : BaseFragment() {

    private lateinit var binding: FragmentAlertBinding

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
    }

    private fun initListener() {
        binding.imgBack.clickSafe { onBack() }

        binding.llChooseFlash.clickSafe {
            findNavController().navigate(R.id.action_alertFragment_to_flashFragment)
        }

        binding.llChooseVibrate.clickSafe {
            findNavController().navigate(R.id.action_alertFragment_to_vibrateFragment)
        }

        binding.swEnableFlash.clickSafe {
            SharePreferenceUtils.setEnableFlashMode(binding.swEnableFlash.isChecked)
        }

        binding.swEnableVibrate.clickSafe {
            SharePreferenceUtils.setEnableVibrate(binding.swEnableVibrate.isChecked)
        }
    }
}