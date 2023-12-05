package com.fansipan.callcolor.calltheme

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.fansipan.callcolor.calltheme.service.LockCallActivity
import com.fansipan.callcolor.calltheme.utils.SharePreferenceUtils

class MyApplication : Application() , Application.ActivityLifecycleCallbacks{

    var currentActivity: Activity? = null

    companion object{

    }
    fun finishActivity(){
        if (currentActivity is LockCallActivity){
            currentActivity?.finish()
        }
    }

    override fun onCreate() {
        super.onCreate()
        SharePreferenceUtils.init(this)
        registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }

}