package com.fansipan.callcolor.calltheme.ui.main

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.fansipan.callcolor.calltheme.base.BaseActivity
import com.fansipan.callcolor.calltheme.databinding.ActivityMainBinding
import com.fansipan.callcolor.calltheme.utils.SharePreferenceUtils
import com.fansipan.callcolor.calltheme.utils.data.DataUtils

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private var navHostFragment: NavHostFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment =
            supportFragmentManager.findFragmentById(binding.containerFragment.id) as NavHostFragment
        navController = navHostFragment!!.navController

        DataUtils.readAnimation(this@MainActivity)

        if (SharePreferenceUtils.isFirstRequestNotification()) {
            try {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) {
                    ActivityCompat.requestPermissions(
                        this, arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        112
                    )
                    SharePreferenceUtils.setFirstRequestNotification(false)
                }
            } catch (_: Exception) { }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}