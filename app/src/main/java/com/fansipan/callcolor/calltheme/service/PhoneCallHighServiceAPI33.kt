package com.fansipan.callcolor.calltheme.service

import android.telecom.Call
import android.telecom.CallAudioState
import android.telecom.InCallService
import android.util.Log

class PhoneCallHighServiceAPI33 : InCallService() {



    companion object {
        const val TAG = "PhoneCallHighServiceAPI33"
        var sInstance : PhoneCallHighServiceAPI33? = null
    }

    override fun onCreate() {
        super.onCreate()
        sInstance = this
    }

    override fun onCallAudioStateChanged(audioState: CallAudioState?) {
        super.onCallAudioStateChanged(audioState)
        Log.d(TAG, "onCallAudioStateChanged: $audioState")
    }

    override fun onCallAdded(call: Call?) {
        super.onCallAdded(call)

    }
}