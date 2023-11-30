package com.fansipan.callcolor.calltheme.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RingtoneModel(
    var nameSound: String,
    var sound: Int,
    var duration: String,
    var isSelected: Boolean = false
) : Parcelable {

}