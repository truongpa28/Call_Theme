package com.fansipan.callcolor.calltheme.utils.ex

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.fansipan.callcolor.calltheme.utils.Constants
import com.fansipan.callcolor.calltheme.utils.SharePreferenceUtils
import com.fansipan.callcolor.calltheme.utils.data.Amp
import com.fansipan.callcolor.calltheme.utils.data.VibrateRingtoneUtils
import com.google.android.material.tabs.TabLayout

fun View.disableView() {
    this.isClickable = false
    this.postDelayed({ this.isClickable = true }, 1000)
}

class SafeClickListener(val onSafeClickListener: (View) -> Unit) : View.OnClickListener {
    override fun onClick(v: View) {
        v.disableView()
        onSafeClickListener(v)
    }
}

fun View.setOnSafeClick(onSafeClickListener: (View) -> Unit) {
    val safeClick = SafeClickListener {
        onSafeClickListener(it)
    }
    setOnClickListener(safeClick)
}

var isAvailableClick = true
fun handleAvailableClick(time: Long) {
    Handler(Looper.getMainLooper()).postDelayed({
        isAvailableClick = true
    }, time)
}

fun View.clickSafe(time: Long = 400, action: () -> Unit) {
    this.setOnClickListener {
        if (isAvailableClick) {
            isAvailableClick = false
            handleAvailableClick(time)
            action.invoke()
        }
    }
}

fun Context.openActivity(pClass: Class<out Activity>, bundle: Bundle?) {
    val intent = Intent(this, pClass)
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    startActivity(intent)
}

fun Context.openActivity(pClass: Class<out Activity>, isFinish: Boolean = false) {
    openActivity(pClass, null)
    if (isFinish) {
        (this as Activity).finish()
    }
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.showOrGone(isShow: Boolean) {
    if (isShow) {
        show()
    } else {
        gone()
    }
}

fun ImageView.setTint() {
    val matrix = ColorMatrix()
    matrix.setSaturation(0f)

    val filter = ColorMatrixColorFilter(matrix)
    this.colorFilter = filter
}

fun ImageView.clearTint() {
    this.colorFilter = null
}

fun View.setMargins(left: Int, top: Int, right: Int, bottom: Int) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val p = layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(left, top, right, bottom)
        requestLayout()
    }
}

fun TabLayout.createTab(tabName: String): TabLayout.Tab {
    return newTab().setText(tabName)
}

val Int.toDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

fun Context.getBatteryLevel(): Int {
    val batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
    return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
}

var vibrator: Vibrator? = null

fun initVibrator(context: Context) {
    vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
}

fun turnOffVibration() {
    if (vibrator != null) {
        Log.d(Constants.TAG, "turnOffVibration")
        vibrator?.cancel()
    }
}

fun startVibration(repeat: Int) {
    Log.d(Constants.TAG, "startVibration")
    val vibrationType = if (SharePreferenceUtils.getVibrateRingtone() == -1) 0 else SharePreferenceUtils.getVibrateRingtone()
    val index = VibrateRingtoneUtils.getPositionWithIcon(vibrationType)
    val ampIndex = if (index != -1) {
        index
    } else {
        0
    }
    val ampEntity = Amp.getItem(ampIndex)
    val mTime = ampEntity.timeArr
    val mAmp = ampEntity.ampArr
    val listTime =
        arrayOf(mTime, mTime, mTime).flatMap { it.toList() }.toLongArray()
    val listAmp = arrayOf(mAmp, mAmp, mAmp).flatMap { it.toList() }.toIntArray()
    if (isSdk26()) {
        val effect = when (repeat) {
            -1 -> {
                VibrationEffect.createWaveform(listTime, listAmp, repeat)
            }

            else -> {
                VibrationEffect.createWaveform(mTime, mAmp, repeat)
            }
        }
        vibrator?.vibrate(effect)
    } else {
        @Suppress("DEPRECATION")
        vibrator?.vibrate(mTime, repeat)
    }
}