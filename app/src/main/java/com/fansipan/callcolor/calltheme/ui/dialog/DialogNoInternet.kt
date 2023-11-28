package com.fansipan.callcolor.calltheme.ui.dialog

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.AlertDialog
import com.fansipan.callcolor.calltheme.utils.clickSafe
import com.fansipan.callcolor.calltheme.utils.gone
import com.fansipan.callcolor.calltheme.utils.show
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.databinding.DialogNoInternetBinding

class DialogNoInternet(private val context: Context) {
    private val binding by lazy {
        DialogNoInternetBinding.inflate(LayoutInflater.from(context))
    }

    private val dialog: AlertDialog by lazy {
        AlertDialog.Builder(context, R.style.dialog_transparent_width).setView(binding.root)
            .create()
    }

    init {
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
    }

    fun isShowing(): Boolean {
        return dialog.isShowing
    }

    fun hide() {
        dialog.dismiss()
    }

    fun show() {
        binding.prgLoading.gone()
        dialog.setCancelable(false)
        binding.btnRetry.clickSafe {
            onRetry()
        }
        if (!dialog.isShowing)
            dialog.show()
    }

    private fun onRetry() {

        binding.btnRetry.alpha = 0.4f
        binding.btnRetry.isClickable = false
        binding.btnRetry.isEnabled = false

        binding.prgLoading.show()

        Handler(Looper.getMainLooper()).postDelayed({
            binding.btnRetry.alpha = 1f
            binding.btnRetry.isClickable = true
            binding.btnRetry.isEnabled = true
            binding.prgLoading.gone()
        }, 1000L)
    }

}