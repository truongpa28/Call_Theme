package com.fansipan.callcolor.calltheme.ui.dialog

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.AlertDialog
import com.fansipan.callcolor.calltheme.utils.ex.clickSafe
import com.fansipan.callcolor.calltheme.utils.ex.gone
import com.fansipan.callcolor.calltheme.utils.ex.show
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.databinding.DialogDeleteDownloadedBinding
import com.fansipan.callcolor.calltheme.databinding.DialogNoInternetBinding
import com.fansipan.callcolor.calltheme.databinding.DialogQuitEditBinding

class DialogQuitEdit(private val context: Context) {
    private val binding by lazy {
        DialogQuitEditBinding.inflate(LayoutInflater.from(context))
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

    fun show(onYes : () -> Unit) {
        binding.btnOk.clickSafe {
            onYes()
            dialog.dismiss()
        }
        binding.btnCancel.clickSafe {
            hide()
        }
        if (!dialog.isShowing)
            dialog.show()
    }

}