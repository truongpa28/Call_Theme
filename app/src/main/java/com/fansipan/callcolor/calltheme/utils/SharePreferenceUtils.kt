package com.fansipan.callcolor.calltheme.utils

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color

object SharePreferenceUtils {

    private const val PER_NAME = "data_app_battery_charging"

    lateinit var sharePref: SharedPreferences

    fun init(context: Context) {
        if (!SharePreferenceUtils::sharePref.isInitialized) {
            sharePref = context.getSharedPreferences(PER_NAME, Context.MODE_PRIVATE)
        }
    }

    fun <T> saveKey(key: String, value: T) {
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

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return sharePref.getInt(key, defaultValue)
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharePref.getBoolean(key, defaultValue)
    }

    fun getLong(key: String): Long {
        return sharePref.getLong(key, 0L)
    }

    fun getFloat(key: String): Float {
        return sharePref.getFloat(key, 0f)
    }


    fun getCodeLanguageChoose(): String = getString("getCodeLanguageChoose", "en")
    fun setCodeLanguageChoose(value: String) = saveKey("getCodeLanguageChoose", value)


    fun isFirstRequestNoti(): Boolean = getBoolean("isFirstRequestNoti", true)
    fun setFirstRequestNoti(value: Boolean) = saveKey("isFirstRequestNoti", value)


    fun isEnableThemeCall(): Boolean = getBoolean("isEnableThemeCall", true)
    fun setEnableThemeCall(value: Boolean) = saveKey("isEnableThemeCall", value)


    fun isEnableFlashMode(): Boolean = getBoolean("isEnableFlashMode", true)
    fun setEnableFlashMode(value: Boolean) = saveKey("isEnableFlashMode", value)

    fun getTypeFlash() = getInt("type_flash", 0)
    fun setTypeFlash(typeFlash: Int) = saveKey("type_flash", typeFlash)

    fun isEnableSpeedMode() = getBoolean("is_enable_speed_mode", true)
    fun setIsEnableSpeedMode(isEnable: Boolean) = saveKey("is_enable_speed_mode", isEnable)

    fun getSpeedFlash() = getInt("get_speed_flash", -1)
    fun setSpeedFlash(value: Int) = saveKey("get_speed_flash", value)

    fun getVibrateRingtone() = getInt("get_vibrate_ringtone", -1)
    fun setVibrateRingtone(value: Int) = saveKey("get_vibrate_ringtone", value)

    fun isEnableVibrate() = getBoolean("is_enable_vibrate", false)
    fun setIsEnableVibrate(isEnable: Boolean) = saveKey("is_enable_vibrate", isEnable)

    fun isEnableCallMode() = getBoolean("isEnableCallMode", true)
    fun setIsEnableCallMode(isEnable: Boolean) = saveKey("isEnableCallMode", isEnable)


}