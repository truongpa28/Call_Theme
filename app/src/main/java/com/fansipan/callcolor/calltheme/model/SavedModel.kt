package com.fansipan.callcolor.calltheme.model

import android.os.Parcelable
import com.google.gson.Gson
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SavedModel(
    var listData : ArrayList<ItemSavedModel>
) : Parcelable {

    companion object {
        fun toSavedModel(jsonData: String): SavedModel? {
            return Gson().fromJson(jsonData, SavedModel::class.java)
        }
    }

    fun toJson(): String {
        return Gson().toJson(this)
    }

}