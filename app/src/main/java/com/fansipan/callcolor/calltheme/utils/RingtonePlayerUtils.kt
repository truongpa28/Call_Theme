package com.fansipan.callcolor.calltheme.utils

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.widget.Toast
import com.fansipan.callcolor.calltheme.R

object RingtonePlayerUtils {


    var mp: MediaPlayer? = null
    private var runnable: Runnable? = null
    private var handler: Handler? = null

    fun startPlayer(context: Context, path : String, action : () -> Unit) {
        try {
            releasePlayer()
            mp = MediaPlayer()
            mp?.setDataSource(
                context,
                Uri.parse(path)
            )
            val volume = SharePreferenceUtils.getVolumeRingtone() / 100f
            mp?.setVolume(volume, volume)
            mp!!.prepare()
            mp!!.start()
            mp!!.setOnCompletionListener {
                action()
            }
            val handler: Handler? = handler
            runnable?.let { handler?.removeCallbacks(it) }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun releasePlayer() {
        val mediaPlayer: MediaPlayer? = mp
        if (mediaPlayer != null) {
            with(mediaPlayer) { stop() }
            mp?.release()
            mp = null
        }
        val handler = handler
        runnable?.let { handler?.removeCallbacks(it) }
    }

    fun stopPlayer() {
        val mediaPlayer: MediaPlayer? = mp
        if (mediaPlayer != null) {
            with(mediaPlayer) { stop() }
            mp?.release()
            mp = null
        }
        val handler = handler
        runnable?.let { handler?.removeCallbacks(it) }
    }

    fun setVolume(volume: Float = 1f) {
        mp?.setVolume(volume, volume)
    }
}