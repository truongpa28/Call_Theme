package com.fansipan.callcolor.calltheme.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.AlertDialog
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.databinding.DialogReadmiPermissionBinding
import com.fansipan.callcolor.calltheme.utils.ex.setOnSafeClick

class DialogReadMiPermission(private val context: Context) {

    private val binding by lazy {
        DialogReadmiPermissionBinding.inflate(LayoutInflater.from(context))
    }

    private val dialog: AlertDialog by lazy {
        AlertDialog.Builder(context, R.style.dialog_transparent_width).setView(binding.root)
            .create()
    }

    init {
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
    }

    private fun isShowing(): Boolean {
        return dialog.isShowing
    }

    fun hide() {
        dialog.dismiss()
    }

    fun show(
        onClickGoToSetting: (() -> Unit)? = null,
        onDone: (() -> Unit)? = null
    ) {

        binding.btnClose.setOnSafeClick {
            hide()
            onDone?.invoke()
        }

        binding.txtGotoSetting.setOnSafeClick {
            onClickGoToSetting?.invoke()
            onDone?.invoke()
            hide()
        }


        if (!isShowing())
            dialog.show()
    }
}