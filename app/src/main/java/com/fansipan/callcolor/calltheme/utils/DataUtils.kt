package com.fansipan.callcolor.calltheme.utils

import android.content.Context
import com.fansipan.callcolor.calltheme.model.CallThemeScreenModel
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.io.InputStream

object DataUtils {

    var listDataCallThemScreen = ArrayList<CallThemeScreenModel>()

    fun readAnimation(context: Context) {
        listDataCallThemScreen.clear()
        try {

            val obj =
                JSONArray(loadJsonFromAsset("json/call_theme_screen.json", context).toString())

            for (i in 0 until obj.length()) {

                val item = obj.getJSONObject(i)

                val data = CallThemeScreenModel()

                data.id = item.getInt("id")
                data.index = item.getInt("index")
                data.background = item.getString("background")
                data.videoUrl = item.getString("videoUrl")
                data.buttonIndex = item.getString("buttonIndex")
                data.category = item.getString("category")
                data.isSetReward = item.getBoolean("isSetReward")

                listDataCallThemScreen.add(data)
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

}