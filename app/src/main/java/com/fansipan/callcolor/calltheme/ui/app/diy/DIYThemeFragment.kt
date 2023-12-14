package com.fansipan.callcolor.calltheme.ui.app.diy

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.base.BaseFragment
import com.fansipan.callcolor.calltheme.databinding.FragmentDIYThemeBinding
import com.fansipan.callcolor.calltheme.model.CallThemeScreenModel
import com.fansipan.callcolor.calltheme.model.ItemSavedModel
import com.fansipan.callcolor.calltheme.ui.app.diy.adapter.AvatarAdapter
import com.fansipan.callcolor.calltheme.ui.app.diy.adapter.BackgroundAdapter
import com.fansipan.callcolor.calltheme.ui.app.diy.adapter.IconCallAdapter
import com.fansipan.callcolor.calltheme.ui.app.diy.adapter.IconCallAdapterV3
import com.fansipan.callcolor.calltheme.utils.RealPathUtil
import com.fansipan.callcolor.calltheme.utils.SharePreferenceUtils
import com.fansipan.callcolor.calltheme.utils.data.AvatarUtils
import com.fansipan.callcolor.calltheme.utils.data.DataSaved
import com.fansipan.callcolor.calltheme.utils.data.DataUtils
import com.fansipan.callcolor.calltheme.utils.data.IconCallUtils
import com.fansipan.callcolor.calltheme.utils.ex.clickSafe
import com.fansipan.callcolor.calltheme.utils.ex.getPathOfBg
import com.fansipan.callcolor.calltheme.utils.ex.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class DIYThemeFragment : BaseFragment() {

    private lateinit var binding: FragmentDIYThemeBinding

    private val adapterAvatar by lazy {
        AvatarAdapter()
    }

    private val adapterBackground by lazy {
        BackgroundAdapter()
    }

    private val adapterIconCall by lazy {
        IconCallAdapter()
    }

    private var typeChoose = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDIYThemeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() {
        val dataBackground = DataUtils.listDataCallThemScreen.subList(0, 11).toMutableList()
        dataBackground.add(0, CallThemeScreenModel())
        adapterBackground.setDataList(dataBackground)
        binding.rcyBackground.adapter = adapterBackground

        adapterAvatar.setDataList(AvatarUtils.listAvatar.subList(0, 11))
        binding.rcyAvatar.adapter = adapterAvatar

        adapterIconCall.setDataList(IconCallUtils.listIconCall.subList(0, 11))
        binding.rcyCallIcon.adapter = adapterIconCall

        showPreview()
    }

    private fun showPreview() {
        Glide.with(requireContext()).load(DataUtils.callThemeEdit.background).into(binding.imgBackground)

        val posButton = DataUtils.callThemeEdit.buttonIndex.toInt() - 1
        binding.imgIconCall1.setImageResource(IconCallUtils.listIconCall[posButton].icon1)
        binding.imgIconCall2.setImageResource(IconCallUtils.listIconCall[posButton].icon2)

        try {
            val posAvt = DataUtils.callThemeEdit.avatar
            if (posAvt.length < 3) {
                Glide.with(this)
                    .load(AvatarUtils.listAvatar[posAvt.toInt()])
                    .into(binding.imgAvatar)
                    .onLoadFailed(ContextCompat.getDrawable(requireContext(), AvatarUtils.listAvatar[1]))
            } else {
                Glide.with(this)
                    .load(DataUtils.callThemeEdit.avatar)
                    .into(binding.imgAvatar)
                    .onLoadFailed(ContextCompat.getDrawable(requireContext(), AvatarUtils.listAvatar[1]))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initListener() {
        binding.imgBack.clickSafe { onBack() }

        adapterBackground.setOnClickItem { item, position ->
            if (position == 0) {
                typeChoose = 1
                chooseImage()
            } else {
                clickItemCollection(item, position)
            }

        }

        adapterAvatar.setOnClickItem { item, position ->
            if (position != 0) {
                DataUtils.tmpCallThemeEdit.avatar = (position).toString()
                //DataUtils.tmpCallThemeEdit = DataUtils.callThemeEdit.copy()
                findNavController().navigate(R.id.action_DIYThemeFragment_to_editThemeFragment)
            } else {
                typeChoose = 2
                chooseImage()
            }
        }

        adapterIconCall.setOnClickItem { item, position ->
            DataUtils.tmpCallThemeEdit.buttonIndex = (position + 1).toString()
            //DataUtils.tmpCallThemeEdit = DataUtils.callThemeEdit.copy()
            findNavController().navigate(R.id.action_DIYThemeFragment_to_editThemeFragment)
        }

        binding.txtPreview.clickSafe {
            findNavController().navigate(R.id.action_DIYThemeFragment_to_previewFragment, bundleOf("type" to "diy"))
        }
        binding.llPreview.clickSafe {
            findNavController().navigate(R.id.action_DIYThemeFragment_to_previewFragment, bundleOf("type" to "diy"))
        }

        binding.txtApply.clickSafe {
            if (!isAllPermissionCallTheme()) {
                showDialogPermission {
                    applyThemeCall()
                }
            } else {
                applyThemeCall()
            }
        }
    }

    private fun applyThemeCall() {
        SharePreferenceUtils.setAvatarChoose(DataUtils.callThemeEdit.avatar)
        SharePreferenceUtils.setIconCallChoose(DataUtils.callThemeEdit.buttonIndex)
        SharePreferenceUtils.setBackgroundChoose(DataUtils.callThemeEdit.background)
        DataSaved.addNewCreate(
            requireContext(),
            ItemSavedModel(
                DataUtils.callThemeEdit.background,
                DataUtils.callThemeEdit.avatar,
                DataUtils.callThemeEdit.buttonIndex,
                false
            )
        )
        findNavController().navigate(R.id.action_DIYThemeFragment_to_congratulationFragment)
    }

    private fun clickItemCollection(item : CallThemeScreenModel?, position: Int) {
        item?.let {
            val fileName = "${item.category}_${item.id}.png"
            if (SharePreferenceUtils.isBackgroundDownload(fileName)) {
                findNavController().navigate(
                    R.id.action_DIYThemeFragment_to_editThemeFragment,
                    bundleOf("type" to "diy")
                )
                //DataUtils.callThemeEdit = CallThemeScreenModel(0, 0, requireContext().getPathOfBg(item), item.avatar, item.buttonIndex)
                DataUtils.tmpCallThemeEdit.background = requireContext().getPathOfBg(item)
                //DataUtils.tmpCallThemeEdit = DataUtils.callThemeEdit.copy()
            } else {
                downloadAnim(it, onDone = {
                    lifecycleScope.launch(Dispatchers.Main) {
                        SharePreferenceUtils.setBackgroundDownload(fileName, true)
                        adapterBackground.notifyItemChanged(position)
                    }
                }, onFail = {
                    lifecycleScope.launch(Dispatchers.Main) {
                        requireContext().showToast(getString(R.string.error))
                    }
                })
            }
        }
    }

    private fun chooseImage() {
        if (Build.VERSION.SDK_INT >= 33) {
            checkPermissionAndPickImageMediaImage()
        } else {
            checkPermissionAndPickImageReadExternal()
        }
    }

    private fun pickImage() {
        imagePicker.launch("image/*")
    }

    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                if (typeChoose == 1) {
                    DataUtils.tmpCallThemeEdit.background = RealPathUtil.getRealPath(requireContext(), uri)
                } else {
                    DataUtils.tmpCallThemeEdit.avatar = RealPathUtil.getRealPath(requireContext(), uri)
                }

                findNavController().navigate(R.id.action_DIYThemeFragment_to_editThemeFragment)
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

    private fun downloadAnim(item: CallThemeScreenModel, onDone: () -> Unit, onFail: () -> Unit) =
        lifecycleScope.launch(Dispatchers.IO) {
            val outputStream: FileOutputStream
            val outputFile: File
            try {
                showDialogDownload()
                outputFile = File(
                    requireContext().getPathOfBg(item)
                )
                outputStream = FileOutputStream(outputFile)
                val url = URL("https://batterycharger.lutech.vn/app/calltheme/theme3/theme${item.id}/bgtheme${item.id}.png")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                val inputStream: InputStream = connection.inputStream
                val fileLength = connection.contentLength
                val buffer = ByteArray(1024)
                var total: Int = 0
                var bytesRead: Int
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                    total += bytesRead
                    updateUIDownload(total * 100 / fileLength)
                }
                outputStream.close()
                inputStream.close()
                connection.disconnect()
                Handler(Looper.getMainLooper()).postDelayed({
                    hideDialogDownload()
                    onDone.invoke()
                    DataUtils.tmpCallThemeEdit.background = outputFile.absolutePath
                    //DataSaved.addNewDownload(requireContext(), ItemSavedModel(outputFile.absolutePath, "1", "1", true))
                    findNavController().navigate(
                        R.id.action_DIYThemeFragment_to_editThemeFragment,
                        bundleOf("type" to "diy")
                    )
                    //DataUtils.tmpCallThemeEdit = DataUtils.callThemeEdit.copy()
                },200L)
            } catch (e: IOException) {
                e.printStackTrace()
                onFail.invoke()
            }
        }

    private lateinit var dialog: Dialog
    private fun showDialogDownload() {
        lifecycleScope.launch(Dispatchers.Main) {
            dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.layout_dialog_download)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.findViewById<ProgressBar>(R.id.prgDownload).progress = 0
            dialog.setCancelable(false)
            dialog.show()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun updateUIDownload(progress : Int) {
        lifecycleScope.launch(Dispatchers.Main){
            try {
                dialog.findViewById<ProgressBar>(R.id.prgDownload).progress = progress
                dialog.findViewById<TextView>(R.id.txtProgress).text = String.format("%d%%", progress)
            } catch (e: Exception) {
                e.printStackTrace()
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
        }
    }

    private fun hideDialogDownload() {
        try {
            lifecycleScope.launch(Dispatchers.Main){
                dialog.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

}