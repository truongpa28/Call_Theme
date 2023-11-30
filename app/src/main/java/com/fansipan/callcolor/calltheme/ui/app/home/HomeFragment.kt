package com.fansipan.callcolor.calltheme.ui.app.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.base.BaseFragment
import com.fansipan.callcolor.calltheme.databinding.FragmentHomeBinding
import com.fansipan.callcolor.calltheme.service.IncomingCallService
import com.fansipan.callcolor.calltheme.utils.clickSafe
import com.fansipan.callcolor.calltheme.utils.connectService


class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        requireContext().connectService(IncomingCallService::class.java)
    }

    private fun initView() {
        binding.titleThemeCall.isSelected = true
        binding.titleDIYTheme.isSelected = true
        binding.titleAlert.isSelected = true
        binding.titleRingtone.isSelected = true
    }

    private fun initListener() {

        binding.imgSetting.clickSafe {
            findNavController().navigate(R.id.action_homeFragment_to_settingFragment)
        }

        binding.imgDownloaded.clickSafe {
            findNavController().navigate(R.id.action_homeFragment_to_downloadedFragment)
        }

        binding.btnThemeCall.clickSafe {
            findNavController().navigate(R.id.action_homeFragment_to_downloadedFragment)
        }

        binding.btnDIYTheme.clickSafe {
            findNavController().navigate(R.id.action_homeFragment_to_DIYThemeFragment)
        }

        binding.btnAlert.clickSafe {
            findNavController().navigate(R.id.action_homeFragment_to_downloadedFragment)
        }

        binding.btnRingtone.clickSafe {
            findNavController().navigate(R.id.action_homeFragment_to_ringtoneFragment)
        }

        binding.txtPreview.clickSafe {
            findNavController().navigate(R.id.action_homeFragment_to_previewFragment)
        }
    }

}