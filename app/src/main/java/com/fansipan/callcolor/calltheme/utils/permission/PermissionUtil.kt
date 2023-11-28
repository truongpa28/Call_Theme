package com.fansipan.callcolor.calltheme.utils.permission

import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi

const val PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
const val READ_MEDIA_IMAGE = android.Manifest.permission.READ_MEDIA_IMAGES

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
const val READ_MEDIA_AUDIO = android.Manifest.permission.READ_MEDIA_AUDIO

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
const val READ_MEDIA_VIDEO = android.Manifest.permission.READ_MEDIA_VIDEO

const val READ_EXTERNAL_STORAGE = android.Manifest.permission.READ_EXTERNAL_STORAGE
const val WRITE_EXTERNAL_STORAGE = android.Manifest.permission.WRITE_EXTERNAL_STORAGE

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
const val POST_NOTIFICATION = android.Manifest.permission.POST_NOTIFICATIONS
