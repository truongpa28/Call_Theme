package com.fansipan.callcolor.calltheme.utils.data

import com.fansipan.callcolor.calltheme.R


object VibrateRingtoneUtils {
    val listIcon = listOf<Int>(
        R.drawable.ic_vibration_01,
        R.drawable.ic_vibration_02,
        R.drawable.ic_vibration_03,
        R.drawable.ic_vibration_04,
        R.drawable.ic_vibration_05,
        R.drawable.ic_vibration_06,
        R.drawable.ic_vibration_07,
        R.drawable.ic_vibration_08,
        R.drawable.ic_vibration_09,
        R.drawable.ic_vibration_10,
        R.drawable.ic_vibration_11,
        R.drawable.ic_vibration_12
    )

    fun getPositionWithIcon(src: Int): Int {
        return listIcon.indexOfFirst { it == src }
    }
}