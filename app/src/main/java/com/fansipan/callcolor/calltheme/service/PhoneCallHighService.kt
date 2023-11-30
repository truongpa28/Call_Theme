package com.fansipan.callcolor.calltheme.service

import android.Manifest
import android.telecom.Call
import android.telecom.CallScreeningService
import android.util.Log
import com.fansipan.callcolor.calltheme.utils.getNameContactByPhoneNumber
import com.fansipan.callcolor.calltheme.utils.hasPermission

class PhoneCallHighService : CallScreeningService() {
    companion object {
        const val TAG = "PhoneCallHighService"
    }

    override fun onScreenCall(p0: Call.Details) {
        IncomingCallService.phoneNumber = p0.handle.schemeSpecificPart
        if (hasPermission(Manifest.permission.READ_CONTACTS)) {
            IncomingCallService.nameContact = this.getNameContactByPhoneNumber(IncomingCallService.phoneNumber)
        }
        Log.d(TAG, "onScreenCall: ${IncomingCallService.phoneNumber} - ${IncomingCallService.nameContact}")
        IncomingCallService.onGetContact?.invoke()
    }

}