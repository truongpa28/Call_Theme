package com.fansipan.callcolor.calltheme.utils

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.widget.Toast
import com.fansipan.callcolor.calltheme.R

object MediaPlayerUtils {


    var mp: MediaPlayer? = null
    private var runnable: Runnable? = null
    private var handler: Handler? = null

    fun startMediaPlayer(context: Context, data: Int, volume: Float = 0.5f) {
        try {
            releaseMediaPlayer()
            mp = MediaPlayer()
            mp?.setDataSource(
                context,
                Uri.parse("android.resource://${context.packageName}/$data")
            )
            mp?.setVolume(volume, volume)
            mp!!.prepare()
            mp!!.start()
            mp!!.setOnCompletionListener {

            }
            val handler: Handler? = handler
            runnable?.let { handler?.removeCallbacks(it) }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun releaseMediaPlayer() {
        val mediaPlayer: MediaPlayer? = mp
        if (mediaPlayer != null) {
            with(mediaPlayer) { stop() }
            mp?.release()
            mp = null
        }
        val handler = handler
        runnable?.let { handler?.removeCallbacks(it) }
    }

    fun stopMediaPlayer() {
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