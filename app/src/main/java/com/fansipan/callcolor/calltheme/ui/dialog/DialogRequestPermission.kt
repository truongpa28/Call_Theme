package com.fansipan.callcolor.calltheme.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.AlertDialog
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.databinding.DialogRequestPermissionBinding
import com.fansipan.callcolor.calltheme.utils.ex.hasOverlaySettingPermission
import com.fansipan.callcolor.calltheme.utils.ex.hasWriteSettingPermission
import com.fansipan.callcolor.calltheme.utils.ex.isPhoneAllCall
import com.fansipan.callcolor.calltheme.utils.ex.isPhoneDialer
import com.fansipan.callcolor.calltheme.utils.ex.setOnSafeClick
import com.fansipan.callcolor.calltheme.utils.ex.showToast

class DialogRequestPermission(private val context: Context) {

    private val binding by lazy {
        DialogRequestPermissionBinding.inflate(LayoutInflater.from(context))
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

    fun show(
        cancelAble: Boolean? = false,
        onClickClose: (() -> Unit)? = null,
        onClickPhoneCall: (() -> Unit)? = null,
        onClickCallDefault: (() -> Unit)? = null,
        onClickOverlayApp: (() -> Unit)? = null,
        onClickSetRingtone: (() -> Unit)? = null
    ) {
        setupView()
        dialog.setOnDismissListener {
            onClickClose?.invoke()
        }
        binding.btnClose.setOnSafeClick {
            hide()
        }
        binding.swChangeDialler.setOnSafeClick {
            onClickPhoneCall?.invoke()
        }
        binding.swReadContact.setOnSafeClick {
            onClickCallDefault?.invoke()
        }
        binding.swOverlayApp.setOnSafeClick {
            onClickOverlayApp?.invoke()
        }
        binding.swAnswerCall.setOnSafeClick {
            onClickSetRingtone?.invoke()
        }
        dialog.setCancelable(cancelAble ?: false)

        if (!isShowing())
            dialog.show()
    }

    fun setupView() {
        val isPhoneDialler = context.isPhoneAllCall()
        val isReadContact = context.isPhoneDialer()
        val isOverlayApp = context.hasOverlaySettingPermission()
        val isAnswerCall = context.hasWriteSettingPermission()

        if (isPhoneDialler && isReadContact && isOverlayApp && isAnswerCall) {
            //context.showToast(context.getString(R.string.theme_call_is_available))
            hide()
        }
        binding.swChangeDialler.apply {
            isChecked = isPhoneDialler
            isEnabled = !isPhoneDialler
        }
        binding.swReadContact.apply {
            isChecked = isReadContact
            isEnabled = !isReadContact
        }
        binding.swOverlayApp.apply {
            isChecked = isOverlayApp
            isEnabled = !isOverlayApp
        }
        binding.swAnswerCall.apply {
            isChecked = isAnswerCall
            isEnabled = !isAnswerCall
        }
    }
}