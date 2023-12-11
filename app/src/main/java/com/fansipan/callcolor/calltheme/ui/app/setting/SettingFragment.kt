package com.fansipan.callcolor.calltheme.ui.app.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.fansipan.callcolor.calltheme.base.BaseFragment
import com.fansipan.callcolor.calltheme.databinding.FragmentSettingBinding
import com.fansipan.callcolor.calltheme.ui.language.LanguageActivity
import com.fansipan.callcolor.calltheme.utils.CommonUtils
import com.fansipan.callcolor.calltheme.utils.SharePreferenceUtils
import com.fansipan.callcolor.calltheme.utils.ex.clickSafe
import com.fansipan.callcolor.calltheme.utils.ex.openActivity
import com.fansipan.callcolor.calltheme.utils.ex.showToast


class SettingFragment : BaseFragment() {


    private lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() {
        binding.swEnableCallTheme.isChecked = (SharePreferenceUtils.isEnableThemeCall() && isAllPermissionCallTheme())
    }

    private fun initListener() {
        binding.imgBack.clickSafe { onBack() }

        binding.swEnableCallTheme.clickSafe {
            if (isAllPermissionCallTheme()) {
                SharePreferenceUtils.setEnableThemeCall(binding.swEnableCallTheme.isChecked)
            } else {
                showDialogPermission {
                    if (isAllPermissionCallTheme()) {
                        SharePreferenceUtils.setEnableThemeCall(binding.swEnableCallTheme.isChecked)
                    } else {
                        binding.swEnableCallTheme.isChecked = false
                    }
                }
            }
        }

        binding.llLanguage.clickSafe {
            requireContext().openActivity(LanguageActivity::class.java, bundleOf("setting" to true))
        }

        binding.llShareApp.clickSafe {
            CommonUtils.shareApp(requireContext())
        }

        binding.llRateApp.clickSafe {
            CommonUtils.rateApp(requireContext())
        }

        binding.llFeedback.clickSafe {
            requireContext().showToast("Feature is being developed.")
        }

        binding.llMoreApp.clickSafe {
            CommonUtils.moreApp(requireContext())
        }
    }


}