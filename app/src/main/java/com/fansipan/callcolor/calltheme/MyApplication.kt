package com.fansipan.callcolor.calltheme

import android.app.Application
import com.fansipan.callcolor.calltheme.utils.SharePreferenceUtils

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SharePreferenceUtils.init(this)
    }

}