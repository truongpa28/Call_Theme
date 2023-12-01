package com.fansipan.callcolor.calltheme.model

import android.os.Parcelable
import com.google.gson.Gson
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CreatedModel(
    var background: String = "1",
    var avatar: String = "1",
    var buttonIndex: String = "1"
) : Parcelable {

    companion object {
        fun toCreatedModel(jsonData: String): CreatedModel? {
            return Gson().fromJson(jsonData, CreatedModel::class.java)
        }
    }

    fun toJson(): String {
        return Gson().toJson(this)
    }

}