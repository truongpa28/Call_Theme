package com.fansipan.callcolor.calltheme.utils

import android.content.Context
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.io.InputStream

object DataUtils {

    /*var listDataAnim = ArrayList<AnimCatalogModel>()

    var listDataBackground = ArrayList<BackgroundModel>()

    fun readAnimation(context: Context) {
        listDataAnim.clear()
        try {

            val obj = JSONArray(loadJsonFromAsset("json/animations_catalog_v2.json", context).toString())

            for (i in 0 until obj.length()) {

                var catalogModel = AnimCatalogModel()

                val catalog = obj.getJSONObject(i)

                catalogModel.name = catalog.getString("name")

                val content = catalog.getJSONArray("content")

                for (index in 0 until content.length()) {

                    var animationModel = AnimationModel()

                    val anim = content.getJSONObject(index)

                    animationModel.isPremium = anim.getBoolean("isPremium")
                    animationModel.mediaLowQuality = anim.getString("mediaLowQuality")
                    animationModel.mediaOriginal = anim.getString("mediaOriginal")
                    animationModel.thumbnail = anim.getString("thumbnail")
                    animationModel.type = anim.getString("type")

                    catalogModel.content.add(animationModel)

                }

                listDataAnim.add(catalogModel)
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun readBackground(context: Context) {
        listDataBackground.clear()
        try {
            val obj = JSONArray(loadJsonFromAsset("json/image_background.json", context).toString())

            for (i in 0 until obj.length()) {

                var backgroundModel = BackgroundModel()

                val bgData = obj.getJSONObject(i)

                backgroundModel.id = bgData.getInt("id")
                backgroundModel.link = bgData.getString("link")
                backgroundModel.avatar = bgData.getString("avatar")
                backgroundModel.type = bgData.getInt("type")
                backgroundModel.idCategory = bgData.getInt("idCategory")
                backgroundModel.keyWord = bgData.getString("keyWord")
                backgroundModel.point = bgData.getInt("point")

                listDataBackground.add(backgroundModel)
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun loadJsonFromAsset(path: String, context: Context): String? {
        var json: String? = null
        try {
            val ios: InputStream = context.assets.open(path)
            val size = ios.available()
            val buffer = ByteArray(size)
            ios.read(buffer)
            ios.close()
            json = String(buffer, Charsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return json
    }


    fun getFileNameFromOriginal(mediaOriginal: String): String {
        val index = mediaOriginal.lastIndexOf("/")
        return mediaOriginal.substring(index+1)
    }

    fun getListPlayDuration(context: Context) : List<String> {
        return listOf<String>(
            "5s", "10s", "30s", "1m", context.getString(R.string.always))
    }

    fun getListClosingMethod(context: Context) : List<String> {
        return listOf<String>(
            context.getString(R.string.single_click), context.getString(R.string.double_click))
    }*/

}