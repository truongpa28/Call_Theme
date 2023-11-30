package com.fansipan.callcolor.calltheme.ui.main

import android.Manifest
import android.app.role.RoleManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telecom.TelecomManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.fansipan.callcolor.calltheme.base.BaseActivity
import com.fansipan.callcolor.calltheme.databinding.ActivityMainBinding
import com.fansipan.callcolor.calltheme.ui.dialog.DialogRequestPermission
import com.fansipan.callcolor.calltheme.ui.dialog.DialogRequestWritePermission
import com.fansipan.callcolor.calltheme.utils.DataUtils
import com.fansipan.callcolor.calltheme.utils.isSdk26
import com.fansipan.callcolor.calltheme.utils.showToast

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private var navHostFragment: NavHostFragment? = null

    private val dialogThemeCallPermission by lazy {
        DialogRequestPermission(this@MainActivity)
    }

    private val dialogWritePermission by lazy {
        DialogRequestWritePermission(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment =
            supportFragmentManager.findFragmentById(binding.containerFragment.id) as NavHostFragment
        navController = navHostFragment!!.navController


        DataUtils.readAnimation(this@MainActivity)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    private fun show(){
        dialogThemeCallPermission.show(false, onClickClose = {
            /*dialogWritePermission.show(false, onClickStorage = {
                requestWriteStorageLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }, onClickSetting = {
                openManageWriteSetting()
            })*/
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
        intent.data = Uri.parse("package:${packageName}")
        requestOpenWriteSettingLauncher.launch(intent)
    }

    private fun openSettingOverlay() {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        intent.data = Uri.fromParts("package", packageName, null as String?)
        requestOpenSettingLauncher.launch(intent)
    }
    private var isReSetDialler = false
    private fun setDefaultPhoneApp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = getSystemService(ROLE_SERVICE) as RoleManager
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
                TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName
            )
            startChangeDefaultDialler.launch(intent1)
        }
    }

    private val startChangeDefaultDialler = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        dialogThemeCallPermission.setupView()
        //val telecomManager = requireContext().getSystemService(TELECOM_SERVICE) as TelecomManager
        //if (requireContext().packageName != telecomManager.defaultDialerPackage) { } else { }
    }

}