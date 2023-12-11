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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.base.BaseFragment
import com.fansipan.callcolor.calltheme.databinding.FragmentBackgroundBinding
import com.fansipan.callcolor.calltheme.model.CallThemeScreenModel
import com.fansipan.callcolor.calltheme.model.ItemSavedModel
import com.fansipan.callcolor.calltheme.ui.app.diy.adapter.BackgroundAdapterV2
import com.fansipan.callcolor.calltheme.ui.app.diy.adapter.CategoryBackgroundAdapter
import com.fansipan.callcolor.calltheme.utils.RealPathUtil
import com.fansipan.callcolor.calltheme.utils.SharePreferenceUtils
import com.fansipan.callcolor.calltheme.utils.data.DataSaved
import com.fansipan.callcolor.calltheme.utils.data.DataUtils
import com.fansipan.callcolor.calltheme.utils.data.ThemeCallUtils
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

class BackgroundFragment : BaseFragment() {

    private lateinit var binding: FragmentBackgroundBinding

    private val adapterCategory by lazy {
        CategoryBackgroundAdapter()
    }

    private val adapterBackground by lazy {
        BackgroundAdapterV2()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBackgroundBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() {
        adapterCategory.setDataList(ThemeCallUtils.listCategoryOfBackground)
        binding.rcyCategory.adapter = adapterCategory

        adapterBackground.setDataList(getBackgroundOfCategory(adapterCategory.getPositionChoose()))

        binding.rcyBackground.adapter = adapterBackground
    }

    private fun initListener() {
        binding.imgBack.clickSafe { onBack() }

        adapterCategory.setOnClickItem { item, position ->
            adapterCategory.choose(position)
            adapterBackground.setDataList(getBackgroundOfCategory(adapterCategory.getPositionChoose()))
            binding.rcyBackground.scrollToPosition(0)
        }

        binding.imgChooseBackground.clickSafe {
            chooseAvatar()
        }

        adapterBackground.setOnClickItem { item, position ->
            clickItemCollection(item, position)
        }

    }

    private fun clickItemCollection(item: CallThemeScreenModel?, position: Int) {
        item?.let {
            val fileName = "${item.category}_${item.id}.png"
            if (SharePreferenceUtils.isBackgroundDownload(fileName)) {
                findNavController().popBackStack()
                DataUtils.tmpCallThemeEdit.background = requireContext().getPathOfBg(item)
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

    private fun chooseAvatar() {
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
                DataUtils.tmpCallThemeEdit.background = RealPathUtil.getRealPath(requireContext(), uri)
                findNavController().popBackStack()
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
                val url =
                    URL("https://batterycharger.lutech.vn/app/calltheme/theme3/theme${item.id}/bgtheme${item.id}.png")
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
                    /*DataSaved.addNewDownload(
                        requireContext(),
                        ItemSavedModel(outputFile.absolutePath, "1", "1", true)
                    )*/
                    DataUtils.tmpCallThemeEdit.background = outputFile.absolutePath
                    findNavController().popBackStack()
                }, 200L)
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
    private fun updateUIDownload(progress: Int) {
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                dialog.findViewById<ProgressBar>(R.id.prgDownload).progress = progress
                dialog.findViewById<TextView>(R.id.txtProgress).text =
                    String.format("%d%%", progress)
            } catch (e: Exception) {
                e.printStackTrace()
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
        }
    }

    private fun hideDialogDownload() {
        try {
            lifecycleScope.launch(Dispatchers.Main) {
                dialog.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }


    private fun getBackgroundOfCategory(position: Int): ArrayList<CallThemeScreenModel> {
        if (position == 0) {
            return DataUtils.listDataCallThemScreen
        }
        val key = ThemeCallUtils.listCategoryOfBackground[position].key
        val data = ArrayList<CallThemeScreenModel>()
        data.addAll(DataUtils.listDataCallThemScreen.filter {
            it.category == key
        })
        return data
    }

}