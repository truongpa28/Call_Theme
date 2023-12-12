package com.fansipan.callcolor.calltheme.ui.app.alert

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.base.BaseFragment
import com.fansipan.callcolor.calltheme.databinding.FragmentFlashBinding
import com.fansipan.callcolor.calltheme.databinding.FragmentVibrateBinding
import com.fansipan.callcolor.calltheme.utils.SharePreferenceUtils
import com.fansipan.callcolor.calltheme.utils.data.SpeedFlashUtils
import com.fansipan.callcolor.calltheme.utils.data.VibrateRingtoneUtils
import com.fansipan.callcolor.calltheme.utils.ex.clickSafe
import com.fansipan.callcolor.calltheme.utils.ex.initVibrator
import com.fansipan.callcolor.calltheme.utils.ex.startVibration
import com.fansipan.callcolor.calltheme.utils.ex.turnOffVibration

class VibrateFragment : BaseFragment() {

    private lateinit var binding: FragmentVibrateBinding

    private val adapter by lazy {
        VibrateAdapter()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVibrateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() {
        adapter.setDataList(VibrateRingtoneUtils.listIcon)
        binding.rcyVibrate.adapter = adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initListener() {
        binding.imgBack.clickSafe { onBack() }
        adapter.setOnClickItem { _, position ->
            SharePreferenceUtils.setVibrateRingtone(position)
            adapter.notifyDataSetChanged()
            initVibrator(requireContext())
            startVibration(-1)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        turnOffVibration()
    }

    override fun onPause() {
        super.onPause()
        turnOffVibration()
    }
}