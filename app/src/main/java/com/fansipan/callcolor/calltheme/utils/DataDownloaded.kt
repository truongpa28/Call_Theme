package com.fansipan.callcolor.calltheme.utils

object DataDownloaded {

    /*const val DB_NAME = "data_name"
    const val DB_ANIM = "data_anim"
    const val DB_PHOTO = "data_photo"


    fun addNewAnim(context: Context, data: ItemDownloaded) {
        addData(DB_ANIM, context, data)
    }

    fun getAllAnim(context: Context) = getListData(DB_ANIM, context)


    fun addNewPhoto(context: Context, data: ItemDownloaded) {
        addData(DB_PHOTO, context, data)
    }

    fun getAllPhoto(context: Context) = getListData(DB_PHOTO, context)


    private fun getListData(key: String, context: Context): ArrayList<ItemDownloaded> {
        val pre = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE)
        val dataString = pre.getString(key, "")

        return if (dataString != "") {
            val listData: DownloadedModel = Gson().fromJson(dataString, DownloadedModel::class.java)
            listData.listData
        } else {
            ArrayList<ItemDownloaded>()
        }
    }

    private fun addData(key: String, context: Context, dataNew: ItemDownloaded) {
        val pre = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE)
        val listData = getListData(key, context)
        listData.add(dataNew)
        val data = DownloadedModel(listData)
        val dataString = data.toJson()
        pre.edit().putString(key, dataString).apply()
    }*/

}