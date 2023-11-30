package com.fansipan.callcolor.calltheme.utils.data

import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.model.RingtoneModel
import com.fansipan.callcolor.calltheme.utils.SharePreferenceUtils

object RingtoneUtils {

    const val nameDefaultRingtone = "sound_neon1.mp3"

    val listRingtone = listOf<RingtoneModel>(
        RingtoneModel("sound_neon1.mp3", R.raw.sound_neon1, "00:48"),
        RingtoneModel("sound_neon2.mp3", R.raw.sound_neon2, "00:35"),
        RingtoneModel("sound_neon3.mp3", R.raw.sound_neon3, "00:44"),
        RingtoneModel("sound_neon4.mp3", R.raw.sound_neon4, "01:04"),
        RingtoneModel("sound_halloween1.mp3", R.raw.sound_halloween1, "00:21"),
        RingtoneModel("sound_halloween2.mp3", R.raw.sound_halloween2, "00:13"),
        RingtoneModel("sound_halloween3.mp3", R.raw.sound_halloween3, "00:22"),
        RingtoneModel("sound_marvel1.mp3", R.raw.sound_marvel1, "00:16"),
        RingtoneModel("sound_marvel2.mp3", R.raw.sound_marvel2, "00:34"),
        RingtoneModel("sound_marvel3.mp3", R.raw.sound_marvel3, "00:34"),
        RingtoneModel("sound_marvel4.mp3", R.raw.sound_marvel4, "00:11"),
        RingtoneModel("sound_moto1.mp3", R.raw.sound_moto1, "00:20"),
        RingtoneModel("sound_moto2.mp3", R.raw.sound_moto2, "00:28"),
        RingtoneModel("sound_moto3.mp3", R.raw.sound_moto3, "00:28"),
        RingtoneModel("sound_moto4.mp3", R.raw.sound_moto4, "00:38"),
        RingtoneModel("sound_nature1.mp3", R.raw.sound_nature1, "00:32"),
        RingtoneModel("sound_nature2.mp3", R.raw.sound_nature2, "00:34"),
        RingtoneModel("sound_nature3.mp3", R.raw.sound_nature3, "00:42"),
        RingtoneModel("sound_nature4.mp3", R.raw.sound_nature4, "00:38"),
        RingtoneModel("sound1.mp3", R.raw.sound1, "00:38"),
        RingtoneModel("sound2.mp3", R.raw.sound2, "00:23"),
        RingtoneModel("sound3.mp3", R.raw.sound3, "00:17"),
        RingtoneModel("sound4.mp3", R.raw.sound4, "00:18"),
        RingtoneModel("sound5.mp3", R.raw.sound5, "00:23"),
        RingtoneModel("sound6.mp3", R.raw.sound6, "00:48"),
        RingtoneModel("sound7.mp3", R.raw.sound7, "00:20"),
        RingtoneModel("sound8.mp3", R.raw.sound8, "00:37"),
        RingtoneModel("sound9.mp3", R.raw.sound9, "00:42"),
        RingtoneModel("sound10.mp3", R.raw.sound10, "00:19")
    )


    fun getListRingTone(): List<RingtoneModel> {
        val name = SharePreferenceUtils.getRingtone()
        listRingtone.forEach {
            it.isSelected = it.nameSound == name
        }
        return listRingtone
    }

    fun getRingToneByName(name: String): Int {
        return listRingtone.find { it.nameSound == name }?.sound ?: R.raw.sound_neon1
    }
}