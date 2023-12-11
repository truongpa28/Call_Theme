package com.fansipan.callcolor.calltheme.ui.app.home

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.base.BaseFragment
import com.fansipan.callcolor.calltheme.databinding.FragmentHomeBinding
import com.fansipan.callcolor.calltheme.model.CallThemeScreenModel
import com.fansipan.callcolor.calltheme.service.ThemCallService
import com.fansipan.callcolor.calltheme.utils.SharePreferenceUtils
import com.fansipan.callcolor.calltheme.utils.data.AvatarUtils
import com.fansipan.callcolor.calltheme.utils.data.DataUtils
import com.fansipan.callcolor.calltheme.utils.data.IconCallUtils
import com.fansipan.callcolor.calltheme.utils.ex.clickSafe
import com.fansipan.callcolor.calltheme.utils.ex.connectService


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
        requireContext().connectService(ThemCallService::class.java)
    }

    private fun initView() {
        binding.titleThemeCall.isSelected = true
        binding.titleDIYTheme.isSelected = true
        binding.titleAlert.isSelected = true
        binding.titleRingtone.isSelected = true
        showPreview()

        if (SharePreferenceUtils.isFirstRequestDialogPermission()) {
            try {
                showDialogPermission()
                SharePreferenceUtils.setFirstRequestDialogPermission(false)
            } catch (_: Exception) { }
        }

    }


    private fun showPreview() {
        Glide.with(requireContext()).load(SharePreferenceUtils.getBackgroundChoose()).into(binding.imgBackground)

        val posButton = SharePreferenceUtils.getIconCallChoose().toInt() - 1
        binding.imgIconCall1.setImageResource(IconCallUtils.listIconCall[posButton].icon1)
        binding.imgIconCall2.setImageResource(IconCallUtils.listIconCall[posButton].icon2)

        try {
            val posAvt = SharePreferenceUtils.getAvatarChoose()
            if (posAvt.length < 3) {
                Glide.with(this)
                    .asBitmap()
                    .load(AvatarUtils.listAvatar[posAvt.toInt()])
                    .into(binding.imgAvatar)
                    .onLoadFailed(ContextCompat.getDrawable(requireContext(), AvatarUtils.listAvatar[1]))
            } else {
                Glide.with(this)
                    .asBitmap()
                    .load(SharePreferenceUtils.getAvatarChoose())
                    .into(binding.imgAvatar)
                    .onLoadFailed(ContextCompat.getDrawable(requireContext(), AvatarUtils.listAvatar[1]))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun initListener() {

        binding.imgSetting.clickSafe {
            findNavController().navigate(R.id.action_homeFragment_to_settingFragment)
        }

        binding.imgDownloaded.clickSafe {
            findNavController().navigate(R.id.action_homeFragment_to_downloadedFragment)
        }

        binding.btnThemeCall.clickSafe {
            findNavController().navigate(R.id.action_homeFragment_to_themeCallFragment)
        }

        binding.btnDIYTheme.clickSafe {
            DataUtils.callThemeEdit = CallThemeScreenModel()
            DataUtils.tmpCallThemeEdit = CallThemeScreenModel()
            findNavController().navigate(R.id.action_homeFragment_to_DIYThemeFragment)
        }

        binding.btnAlert.clickSafe {
            findNavController().navigate(R.id.action_homeFragment_to_alertFragment)
        }

        binding.btnRingtone.clickSafe {
            findNavController().navigate(R.id.action_homeFragment_to_ringtoneFragment)
        }

        binding.txtPreview.clickSafe {
            DataUtils.callThemeEdit = CallThemeScreenModel(
                0,
                0,
                SharePreferenceUtils.getBackgroundChoose(),
                SharePreferenceUtils.getAvatarChoose(),
                SharePreferenceUtils.getIconCallChoose()
            )
            findNavController().navigate(R.id.action_homeFragment_to_previewFragment)
        }

        binding.llPreview.clickSafe {
            DataUtils.callThemeEdit = CallThemeScreenModel(
                0,
                0,
                SharePreferenceUtils.getBackgroundChoose(),
                SharePreferenceUtils.getAvatarChoose(),
                SharePreferenceUtils.getIconCallChoose()
            )
            findNavController().navigate(R.id.action_homeFragment_to_previewFragment)
        }
    }

}