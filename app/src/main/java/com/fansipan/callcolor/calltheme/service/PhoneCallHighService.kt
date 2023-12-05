package com.fansipan.callcolor.calltheme.service

import android.Manifest
import android.telecom.Call
import android.telecom.CallScreeningService
import android.util.Log
import com.fansipan.callcolor.calltheme.utils.ex.getNameContactByPhoneNumber
import com.fansipan.callcolor.calltheme.utils.ex.hasPermission

class PhoneCallHighService : CallScreeningService() {
    companion object {
        const val TAG = "PhoneCallHighService"
    }

    override fun onScreenCall(p0: Call.Details) {
        ThemCallService.phoneNumber = p0.handle.schemeSpecificPart
        if (hasPermission(Manifest.permission.READ_CONTACTS)) {
            ThemCallService.nameContact = this.getNameContactByPhoneNumber(ThemCallService.phoneNumber)
        }
        Log.d(TAG, "onScreenCall: ${ThemCallService.phoneNumber} - ${ThemCallService.nameContact}")
        ThemCallService.onGetContact?.invoke()
    }

}