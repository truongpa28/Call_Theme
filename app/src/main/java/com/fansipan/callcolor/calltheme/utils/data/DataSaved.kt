package com.fansipan.callcolor.calltheme.utils.data

import android.content.Context
import com.fansipan.callcolor.calltheme.model.ItemSavedModel
import com.fansipan.callcolor.calltheme.model.SavedModel
import com.google.gson.Gson

object DataSaved {

    const val DB_NAME = "data_name"
    const val DB_DOWNLOAD = "data_downloaded"
    const val DB_CREATE = "data_created"

    fun addNewDownload(context: Context, data: ItemSavedModel) {
        addData(DB_DOWNLOAD, context, data)
    }

    fun getAllDownloaded(context: Context) = getListData(DB_DOWNLOAD, context)


    fun addNewCreate(context: Context, data: ItemSavedModel) {
        addData(DB_CREATE, context, data)
    }

    fun getAllCreated(context: Context) = getListData(DB_CREATE, context)


    private fun getListData(key: String, context: Context): ArrayList<ItemSavedModel> {
        val pre = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE)
        val dataString = pre.getString(key, "")

        return if (dataString != "") {
            val listData: SavedModel = Gson().fromJson(dataString, SavedModel::class.java)
            listData.listData
        } else {
            ArrayList<ItemSavedModel>()
        }
    }

    private fun addData(key: String, context: Context, dataNew: ItemSavedModel) {
        val pre = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE)
        val listData = getListData(key, context)
        listData.add(dataNew)
        val data = SavedModel(listData)
        val dataString = data.toJson()
        pre.edit().putString(key, dataString).apply()
    }

}