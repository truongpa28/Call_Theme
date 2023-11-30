package com.fansipan.callcolor.calltheme.ui.app.alert

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.base.BaseFragment
import com.fansipan.callcolor.calltheme.databinding.FragmentAlertBinding
import com.fansipan.callcolor.calltheme.databinding.FragmentRingtoneBinding
import com.fansipan.callcolor.calltheme.utils.clickSafe

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

    }

    private fun initListener() {
        binding.imgBack.clickSafe { onBack() }

        binding.llChooseFlash.clickSafe {
            findNavController().navigate(R.id.action_alertFragment_to_flashFragment)
        }

        binding.llChooseVibrate.clickSafe {
            findNavController().navigate(R.id.action_alertFragment_to_vibrateFragment)
        }
    }
}