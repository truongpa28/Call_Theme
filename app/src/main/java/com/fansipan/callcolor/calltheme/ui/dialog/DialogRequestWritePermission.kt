package com.fansipan.callcolor.calltheme.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.AlertDialog
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.databinding.DialogRequestWritePermissionBinding
import com.fansipan.callcolor.calltheme.utils.ex.hasWriteSettingPermission
import com.fansipan.callcolor.calltheme.utils.ex.hasWriteStoragePermission
import com.fansipan.callcolor.calltheme.utils.ex.setOnSafeClick

class DialogRequestWritePermission(private val context: Context) {
    private val binding by lazy {
        DialogRequestWritePermissionBinding.inflate(LayoutInflater.from(context))
    }
    private val dialog: AlertDialog by lazy {
        AlertDialog.Builder(context, R.style.dialog_transparent_width).setView(binding.root)
            .create()
    }

    var onAllPermissionGranted: (() -> Unit)? = null

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
        cancelAble: Boolean? = true,
        onClickStorage: (() -> Unit?)? = null,
        onClickSetting: (() -> Unit?)? = null,
        onClickClose: (() -> Unit)? = null
    ) {
        dialog.setCancelable(cancelAble ?: false)
        binding.btnClose.setOnSafeClick {
            hide()
        }
        val isHaveWriteStoragePermission = context.hasWriteStoragePermission()
        val isHaveWriteSettingPermission = context.hasWriteSettingPermission()

        binding.swWriteStorage.isEnabled = !isHaveWriteStoragePermission
        binding.swWriteStorage.isChecked = isHaveWriteStoragePermission

        binding.swWriteSetting.isEnabled = !isHaveWriteSettingPermission
        binding.swWriteSetting.isChecked = isHaveWriteSettingPermission
        binding.swWriteStorage.setOnSafeClick {
            onClickStorage?.invoke()
        }
        binding.swWriteSetting.setOnSafeClick {
            onClickSetting?.invoke()
        }
        if (!dialog.isShowing)
            dialog.show()
    }

    fun updateView() {
        val isHaveWriteStoragePermission = context.hasWriteStoragePermission()
        val isHaveWriteSettingPermission = context.hasWriteSettingPermission()
        binding.swWriteStorage.isEnabled = !isHaveWriteStoragePermission
        binding.swWriteStorage.isChecked = isHaveWriteStoragePermission

        binding.swWriteSetting.isEnabled = !isHaveWriteSettingPermission
        binding.swWriteSetting.isChecked = isHaveWriteSettingPermission
        if (isHaveWriteStoragePermission && isHaveWriteSettingPermission) {
            hide()
            onAllPermissionGranted?.invoke()
        }
    }
}