package com.fansipan.callcolor.calltheme.base

import android.Manifest
import android.app.role.RoleManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telecom.TelecomManager
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fansipan.callcolor.calltheme.ui.dialog.DialogRequestPermission
import com.fansipan.callcolor.calltheme.utils.ex.hasOverlaySettingPermission
import com.fansipan.callcolor.calltheme.utils.ex.hasWriteSettingPermission
import com.fansipan.callcolor.calltheme.utils.ex.isPhoneAllCall
import com.fansipan.callcolor.calltheme.utils.ex.isPhoneDialer

abstract class BaseFragment() : Fragment() {

    private val dialogThemeCallPermission by lazy {
        DialogRequestPermission(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    open fun onBack() {
        findNavController().popBackStack()
    }

    fun isAllPermissionCallTheme() : Boolean {
        val isPhoneDialler = requireContext().isPhoneAllCall()
        val isReadContact = requireContext().isPhoneDialer()
        val isOverlayApp = requireContext().hasOverlaySettingPermission()
        val isAnswerCall = requireContext().hasWriteSettingPermission()
        return isPhoneDialler && isReadContact && isOverlayApp && isAnswerCall
    }

    fun showDialogPermission() {
        dialogThemeCallPermission.show(
            false,
            onClickClose = {

            }, onClickPhoneCall = {
                requestPermission()
            }, onClickCallDefault = {
                setDefaultPhoneApp()
            }, onClickOverlayApp = {
                openSettingOverlay()
            }, onClickSetRingtone = {
                openManageWriteSetting()
            })
    }

    //region Phone Call
    private fun requestPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ANSWER_PHONE_CALLS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("truongpa", "Check Permission Done")
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), permissionPhone,
                1111
            )
        }
    }

    private val permissionPhone = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.ANSWER_PHONE_CALLS,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    //endregion

    //region Default Phone App
    private fun setDefaultPhoneApp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager =
                requireContext().getSystemService(AppCompatActivity.ROLE_SERVICE) as RoleManager
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
                TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME,
                requireContext().packageName
            )
            startChangeDefaultDialler.launch(intent1)
        }
    }

    private val startChangeDefaultDialler = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        dialogThemeCallPermission.setupView()
    }

    //endregion

    //region Setting Overlay
    private fun openSettingOverlay() {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        intent.data = Uri.fromParts("package", requireContext().packageName, null as String?)
        requestOpenSettingLauncher.launch(intent)
    }

    private val requestOpenSettingLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        dialogThemeCallPermission.setupView()
    }
    //endregion

    //region Setting Overlay
    private fun openManageWriteSetting() {
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
        intent.data = Uri.parse("package:${requireContext().packageName}")
        requestOpenWriteSettingLauncher.launch(intent)
    }

    private val requestOpenWriteSettingLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        dialogThemeCallPermission.setupView()
    }
    //endregion

}