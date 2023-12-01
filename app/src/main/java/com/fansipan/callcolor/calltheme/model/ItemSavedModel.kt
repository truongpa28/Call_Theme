package com.fansipan.callcolor.calltheme.model

import android.os.Parcelable
import com.google.gson.Gson
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemSavedModel(
    var background: String = "1",
    var avatar: String = "1",
    var buttonIndex: String = "1",
    var isBackground: Boolean = false
) : Parcelable {

    companion object {
        fun toItemSavedModel(jsonData: String): ItemSavedModel? {
            return Gson().fromJson(jsonData, ItemSavedModel::class.java)
        }
    }

    fun toJson(): String {
        return Gson().toJson(this)
    }

}