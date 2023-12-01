package com.fansipan.callcolor.calltheme.model

import android.os.Parcelable
import com.google.gson.Gson
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DownloadedModel(
    var isBackground: Boolean = false,
    var background: String = "1",
    var avatar: String = "1",
    var buttonIndex: String = "1"
) : Parcelable {

    companion object {
        fun toDownloadedModel(jsonData: String): DownloadedModel? {
            return Gson().fromJson(jsonData, DownloadedModel::class.java)
        }
    }

    fun toJson(): String {
        return Gson().toJson(this)
    }

}