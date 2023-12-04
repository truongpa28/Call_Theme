package com.fansipan.callcolor.calltheme.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.AlertDialog
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.databinding.DialogRequestPermissionBinding
import com.fansipan.callcolor.calltheme.utils.ex.hasAnswerCallComing
import com.fansipan.callcolor.calltheme.utils.ex.hasOverlaySettingPermission
import com.fansipan.callcolor.calltheme.utils.ex.hasReadContact
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

    var onAllPermissionGranted: (() -> Unit)? = null

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
        cancelAble: Boolean? = false,
        onClickClose: (() -> Unit)? = null,
        onClickPhoneCall: (() -> Unit)? = null,
        onClickCallDefault: (() -> Unit)? = null,
        onClickOverlayApp: (() -> Unit)? = null,
        onClickSetRingtone: (() -> Unit)? = null
    ) {
        setupView()
        binding.btnClose.setOnSafeClick {
            hide()
            onClickClose?.invoke()
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
            context.showToast("theme_call_available")
            hide()
            onAllPermissionGranted?.invoke()
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