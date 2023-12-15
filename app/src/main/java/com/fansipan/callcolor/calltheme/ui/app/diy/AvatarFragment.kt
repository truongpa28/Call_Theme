package com.fansipan.callcolor.calltheme.ui.app.diy

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.base.BaseFragment
import com.fansipan.callcolor.calltheme.databinding.FragmentAvatarBinding
import com.fansipan.callcolor.calltheme.ui.app.diy.adapter.AvatarAdapterV2
import com.fansipan.callcolor.calltheme.utils.RealPathUtil
import com.fansipan.callcolor.calltheme.utils.data.AvatarUtils
import com.fansipan.callcolor.calltheme.utils.data.DataUtils
import com.fansipan.callcolor.calltheme.utils.ex.clickSafe
import com.fansipan.callcolor.calltheme.utils.ex.showToast


class AvatarFragment : BaseFragment() {

    private lateinit var binding: FragmentAvatarBinding

    private val adapterAvatar by lazy {
        AvatarAdapterV2()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAvatarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() {
        adapterAvatar.setDataList(AvatarUtils.listAvatar)
        binding.rcyAvatar.adapter = adapterAvatar
    }

    private fun initListener() {
        binding.imgBack.clickSafe { onBack() }
        adapterAvatar.setOnClickItem { item, position ->
            if (position != 0) {
                DataUtils.tmpCallThemeEdit.avatar = (position).toString()
                findNavController().popBackStack()
            } else {
                chooseAvatar()
            }
        }
    }

    private fun chooseAvatar() {
        if (Build.VERSION.SDK_INT >= 33) {
            checkPermissionAndPickImageMediaImage()
        } else {
            checkPermissionAndPickImageReadExternal()
        }
    }

    @SuppressLint("IntentReset")
    private fun pickImage() {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"
        val chooserIntent = Intent.createChooser(pickIntent, "Select Image")
        startActivityForResult(chooserIntent, 1010)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1010) {
            try {
                data?.data?.let {
                    DataUtils.tmpCallThemeEdit.avatar =
                        RealPathUtil.getRealPath(requireContext(), it)
                    findNavController().popBackStack()
                }
            } catch (e : Exception) {
                e.printStackTrace()
                requireContext().showToast(getString(R.string.error))
            }
        }
    }

    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            try {
                uri?.let {
                    DataUtils.tmpCallThemeEdit.avatar =
                        RealPathUtil.getRealPath(requireContext(), uri)
                    findNavController().popBackStack()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                requireContext().showToast(getString(R.string.error))
            }
        }

    private fun checkPermissionAndPickImageReadExternal() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            pickImage()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkPermissionAndPickImageMediaImage() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            pickImage()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                pickImage()
            } else {
                requireContext().showToast(getString(R.string.permission_denied))
            }
        }

}