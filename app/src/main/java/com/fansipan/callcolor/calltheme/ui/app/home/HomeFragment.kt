package com.fansipan.callcolor.calltheme.ui.app.home

import android.Manifest
import android.app.role.RoleManager
import android.content.Context.ROLE_SERVICE
import android.content.Context.TELECOM_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telecom.TelecomManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.fragment.findNavController
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.base.BaseFragment
import com.fansipan.callcolor.calltheme.databinding.FragmentHomeBinding
import com.fansipan.callcolor.calltheme.service.IncomingCallService
import com.fansipan.callcolor.calltheme.ui.dialog.DialogRequestThemeCallPermission
import com.fansipan.callcolor.calltheme.ui.dialog.DialogRequestWritePermission
import com.fansipan.callcolor.calltheme.utils.clickSafe
import com.fansipan.callcolor.calltheme.utils.connectService
import com.fansipan.callcolor.calltheme.utils.isSdk26
import com.fansipan.callcolor.calltheme.utils.showToast


class HomeFragment : BaseFragment() {


    private lateinit var binding: FragmentHomeBinding

    private val dialogThemeCallPermission by lazy {
        DialogRequestThemeCallPermission(requireContext())
    }

    private val dialogWritePermission by lazy {
        DialogRequestWritePermission(requireContext())
    }

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
        show()
        requireContext().connectService(IncomingCallService::class.java)
    }

    private fun show(){
        dialogThemeCallPermission.show(false, onClickClose = {
            dialogWritePermission.show(false, onClickStorage = {
                requestWriteStorageLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }, onClickSetting = {
                openManageWriteSetting()
            })
        }, onClickChangeDialler = {
            setDefaultPhoneApp()
        }, onClickReadContact = {
            requestReadContactPermission.launch(Manifest.permission.READ_CONTACTS)
        }, onClickOverlayApp = {
            openSettingOverlay()
        }, onClickAnswerPhoneCall = {
            if (isSdk26()) {
                requestAnswerCallPermission.launch(Manifest.permission.ANSWER_PHONE_CALLS)
            } else {
                dialogThemeCallPermission.setupView()
                //showSnackBar(getString(R.string.txt_device_not_support_feature), 0, true)
            }
        })
        dialogThemeCallPermission.onAllPermissionGranted = {
            //SharePreferenceUtils.setIsEnableThemeCall(true)
        }
    }

    private val requestOpenSettingLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        dialogThemeCallPermission.setupView()
    }

    private val requestOpenWriteSettingLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        dialogWritePermission.updateView()
    }

    private val requestWriteStorageLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        dialogWritePermission.updateView()
    }

    private val requestAnswerCallPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        dialogThemeCallPermission.setupView()
    }

    private val requestReadContactPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        dialogThemeCallPermission.setupView()
    }

    private fun openManageWriteSetting() {
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
        intent.data = Uri.parse("package:${requireContext().packageName}")
        requestOpenWriteSettingLauncher.launch(intent)
    }

    private fun openSettingOverlay() {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        intent.data = Uri.fromParts("package", requireContext().packageName, null as String?)
        requestOpenSettingLauncher.launch(intent)
    }
    private var isReSetDialler = false
    private fun setDefaultPhoneApp() {
        requireContext().showToast("setDefaultPhoneApp")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = requireContext().getSystemService(ROLE_SERVICE) as RoleManager
            val isHasRole = roleManager.isRoleAvailable(RoleManager.ROLE_DIALER)
            val isAppRoleHeld = roleManager.isRoleHeld(RoleManager.ROLE_DIALER)
            if (isHasRole) {
                if (!isAppRoleHeld) {
                    val intent2 = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER)
                    startChangeDefaultDialler.launch(intent2)
                }
            }
        } else {
            val intent1 = Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
            intent1.putExtra(
                TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, requireContext().packageName
            )
            startChangeDefaultDialler.launch(intent1)
        }
    }

    private val startChangeDefaultDialler = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        dialogThemeCallPermission.setupView()
        val telecomManager = requireContext().getSystemService(TELECOM_SERVICE) as TelecomManager
        if (requireContext().packageName != telecomManager.defaultDialerPackage) {
            //failed
        } else {
            //ok set default phone call
        }
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
    }

}