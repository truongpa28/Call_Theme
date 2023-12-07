package com.fansipan.callcolor.calltheme.utils

import android.content.Context
import android.content.SharedPreferences
import com.fansipan.callcolor.calltheme.utils.data.RingtoneUtils

object SharePreferenceUtils {

    private const val PER_NAME = "data_app_call_theme"

    private lateinit var sharePref: SharedPreferences

    fun init(context: Context) {
        if (!SharePreferenceUtils::sharePref.isInitialized) {
            sharePref = context.getSharedPreferences(PER_NAME, Context.MODE_PRIVATE)
        }
    }

    private fun <T> saveKey(key: String, value: T) {
        when (value) {
            is String -> sharePref.edit().putString(key, value).apply()
            is Int -> sharePref.edit().putInt(key, value).apply()
            is Boolean -> sharePref.edit().putBoolean(key, value).apply()
            is Long -> sharePref.edit().putLong(key, value).apply()
            is Float -> sharePref.edit().putFloat(key, value).apply()
        }

    }

    fun getString(key: String, value: String = ""): String {
        return sharePref.getString(key, value)?.trim() ?: value
    }

    private fun getInt(key: String, defaultValue: Int = 0): Int {
        return sharePref.getInt(key, defaultValue)
    }

    private fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharePref.getBoolean(key, defaultValue)
    }

    fun getLong(key: String): Long {
        return sharePref.getLong(key, 0L)
    }

    fun getFloat(key: String): Float {
        return sharePref.getFloat(key, 0f)
    }


    /*-----------------------------------------Language---------------------------------------------*/
    fun getCodeLanguageChoose(): String = getString("getCodeLanguageChoose", "en")
    fun setCodeLanguageChoose(value: String) = saveKey("getCodeLanguageChoose", value)

    /*----------------------------------------Notification------------------------------------------*/
    fun isFirstRequestNotification(): Boolean = getBoolean("isFirstRequestNotification", true)
    fun setFirstRequestNotification(value: Boolean) = saveKey("isFirstRequestNotification", value)


    /*-----------------------------------------Theme Call-------------------------------------------*/
    fun isThemeDownload(key: String): Boolean = getBoolean("isThemeDownload_$key")
    fun setThemeDownload(key: String, value: Boolean) = saveKey("isThemeDownload_$key", value)

    fun isBackgroundDownload(key: String): Boolean = getBoolean("isBackgroundDownload_$key")
    fun setBackgroundDownload(key: String, value: Boolean) = saveKey("isBackgroundDownload_$key", value)


    fun isEnableThemeCall(): Boolean = getBoolean("isEnableThemeCall", true)
    fun setEnableThemeCall(value: Boolean) = saveKey("isEnableThemeCall", value)


    /*-----------------------------------------Flash------------------------------------------------*/
    fun isEnableFlashMode(): Boolean = getBoolean("isEnableFlashMode", true)
    fun setEnableFlashMode(value: Boolean) = saveKey("isEnableFlashMode", value)

    fun getTypeFlash() = getInt("type_flash", 0)
    fun setTypeFlash(typeFlash: Int) = saveKey("type_flash", typeFlash)

    fun isEnableSpeedMode() = getBoolean("is_enable_speed_mode", true)
    fun setIsEnableSpeedMode(isEnable: Boolean) = saveKey("is_enable_speed_mode", isEnable)


    /*-----------------------------------------Vibrate----------------------------------------------*/
    fun getVibrateRingtone() = getInt("get_vibrate_ringtone", 0)
    fun setVibrateRingtone(value: Int) = saveKey("get_vibrate_ringtone", value)

    fun isEnableVibrate() = getBoolean("is_enable_vibrate", true)
    fun setEnableVibrate(isEnable: Boolean) = saveKey("is_enable_vibrate", isEnable)

    /*----------------------------------------Call Mode---------------------------------------------*/
    fun isEnableCallMode() = getBoolean("isEnableCallMode", true)
    fun setIsEnableCallMode(isEnable: Boolean) = saveKey("isEnableCallMode", isEnable)


    /*-----------------------------------------Ringtone---------------------------------------------*/
    fun getRingtone() = getString("get_ring_tone", RingtoneUtils.nameDefaultRingtone)
    fun setRingtone(value: String) = saveKey("get_ring_tone", value)

    fun getPathRingtone() = getString("getPathRingtone", "")
    fun setPathRingtone(value: String) = saveKey("getPathRingtone", value)

    fun getTypeRingtone() = getString("getTypeRingtone", "device")
    fun setTypeRingtone(value: String) = saveKey("getTypeRingtone", value)

    fun getVolumeRingtone() = getInt("getVolumeRingtone", 100)
    fun setVolumeRingtone(value: Int) = saveKey("getVolumeRingtone", value)


    /*---------------------------------------Set Call Theme-----------------------------------------*/
    fun getIconCallChoose() = getString("getIconCallChoose", "1")
    fun setIconCallChoose(value: String) = saveKey("getIconCallChoose", value)

    fun getAvatarChoose() = getString("getAvatarChoose", "1")
    fun setAvatarChoose(value: String) = saveKey("getAvatarChoose", value)

    fun getBackgroundChoose() = getString("getBackgroundChoose", "")
    fun setBackgroundChoose(value: String) = saveKey("getBackgroundChoose", value)


}