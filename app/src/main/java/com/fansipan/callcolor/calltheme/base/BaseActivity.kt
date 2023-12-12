package com.fansipan.callcolor.calltheme.base

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.ui.dialog.DialogNoInternet
import com.fansipan.callcolor.calltheme.utils.ex.isInternetAvailable

abstract class BaseActivity() : AppCompatActivity() {

    companion object {
        const val ACTION_NETWORK_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE"
    }


    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        /*WindowCompat.setDecorFitsSystemWindows(window, false)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        )*/

        statusBar()

        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    private fun statusBar() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(networkReceiver, IntentFilter(ACTION_NETWORK_CHANGE))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkReceiver)
    }

    private val dialogNoInternet by lazy {
        DialogNoInternet(this)
    }

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == ACTION_NETWORK_CHANGE) {
                if (isInternetAvailable()) {
                    dialogNoInternet.hide()
                } else {
                    dialogNoInternet.show()
                }
            }
        }
    }


}