package com.fansipan.callcolor.calltheme.utils

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.RingtoneManager
import android.net.Uri
import android.os.Environment
import android.os.StrictMode
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


object Constants {

    const val TAG = "truongpa"


    const val NOTIFICATION_CHANNEL_ID = "com.fansipan.callcolor.CALLTHEME"
    const val NOTIFICATION_CHANNEL_ID2 = "com.fansipan.callcolor.CALLTHEME.ggg"
    const val NOTIFICATION_CHANNEL_NAME = "Call Theme"
    const val NOTIFICATION_CHANNEL_NAME2 = "Call Theme"
    const val NOTIFICATION_DETAILS = "Battery Charging"

    fun saveFile(fName: String, context: Context): File? {
        val assetFileDescriptor: AssetFileDescriptor?
        var file3: File? = null
        try {
            val file2 = File(
                Environment.getExternalStorageDirectory().absoluteFile.toString() + "/Android/data/" + context.packageName + "/"
            )
            file2.mkdir()
            file3 = File(file2, fName)
            if (file3.exists()) {
            }
            assetFileDescriptor = try {
                val contentResolver = context.contentResolver
                val sb = StringBuilder()
                sb.append("android.resource://")
                sb.append(context.packageName)
                sb.append("/raw/")
                sb.append(fName.substring(0, fName.lastIndexOf(46.toChar())))
                contentResolver.openAssetFileDescriptor(Uri.parse(sb.toString()), "r")
            } catch (unused: FileNotFoundException) {
                null
            }
            val bArr = ByteArray(1024)
            val createInputStream = assetFileDescriptor!!.createInputStream()
            val fileOutputStream = FileOutputStream(file3)
            while (true) {
                val read = createInputStream.read(bArr)
                if (read == -1) {
                    fileOutputStream.close()
                    return file3
                }
                fileOutputStream.write(bArr, 0, read)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

    fun setRingtone(file: File, context: Context) {
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        try {
            val path = Uri.fromFile(file)

            RingtoneManager.setActualDefaultRingtoneUri(
                context,
                RingtoneManager.TYPE_RINGTONE,
                path
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}