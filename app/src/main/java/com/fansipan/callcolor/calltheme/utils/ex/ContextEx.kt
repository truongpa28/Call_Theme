package com.fansipan.callcolor.calltheme.utils.ex

import android.Manifest
import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.ContactsContract
import android.provider.Settings
import android.telecom.TelecomManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.fansipan.callcolor.calltheme.model.CallThemeScreenModel


fun Context.connectService(pClass: Class<out Service>) {
    startService(Intent(this, pClass))
}

fun Context.endService(pClass: Class<out Service>) {
    stopService(Intent(this, pClass))
}


fun Context.hasWriteSettingPermission(): Boolean {
    return Settings.System.canWrite(this)
}

fun Context.hasWriteStoragePermission(): Boolean {
    return if (isSdkR()) {
        true
    } else {
        hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
}

fun Context.showToast(msg: String, isShowDurationLong: Boolean = false) {
    val duration = if (isShowDurationLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    Toast.makeText(this, msg, duration).show()
}

fun Context.isInternetAvailable(): Boolean {
    var result = false
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        @Suppress("DEPRECATION")
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
    }

    return result
}

fun Context.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

fun Context.getNameContactByPhoneNumber(str: String): String {
    val name: String
    val withAppendedPath =
        Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(str))

    contentResolver.query(
        withAppendedPath,
        null as Array<String?>?,
        null as String?,
        null as Array<String?>?,
        null as String?
    )?.use { query ->
        if (query.count > 0) {
            query.moveToNext()
            @SuppressLint("Range") val string =
                query.getString(query.getColumnIndex("display_name"))
            name = string
            return name
        } else {
            query.close()
        }
    }
    name = "Unknown number"
    return name
}

fun Context.availableToSetThemeCall() =
    isPhoneDialer() && hasOverlaySettingPermission()
            && hasAnswerCallComing() && hasReadContact()

fun Context.isPhoneDialer(): Boolean {
    //return true
    /*if (isSdk33()) {
        return true
    }*/
    val telecomManager = getSystemService(AppCompatActivity.TELECOM_SERVICE) as TelecomManager
    return packageName == telecomManager.defaultDialerPackage
}

fun Context.hasOverlaySettingPermission(): Boolean {
    return Settings.canDrawOverlays(this)
}

fun Context.hasAnswerCallComing(): Boolean {
    return if (isSdk26()) {
        hasPermission(Manifest.permission.ANSWER_PHONE_CALLS)
    } else {
        false
    }
}

fun Context.hasReadContact(): Boolean {
    return hasPermission(Manifest.permission.READ_CONTACTS)
}

fun Context.getPathOfBg(item: CallThemeScreenModel): String {
    return Environment.getExternalStorageDirectory().absoluteFile.toString() + "/Android/data/" + packageName +
            "/" + "${item.category}_${item.id}.png"
}

/*
fun Context.isEnableInRingerMode(): Boolean {
    val am = getSystemService(Service.AUDIO_SERVICE) as AudioManager

    return when (am.ringerMode) {
        AudioManager.RINGER_MODE_SILENT -> SharePreferenceUtils.isRingerSilentMode()
        AudioManager.RINGER_MODE_VIBRATE -> SharePreferenceUtils.isRingerVibrateMode()
        AudioManager.RINGER_MODE_NORMAL -> SharePreferenceUtils.isRingerNormalMode()
        else -> true
    }
}*/

fun Context.isPhoneAllCall(): Boolean {
    return ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ANSWER_PHONE_CALLS
            ) == PackageManager.PERMISSION_GRANTED
}

fun Context.getRingTone(): Ringtone {
    val defaultRingtoneUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
    return RingtoneManager.getRingtone(this, defaultRingtoneUri)
}

