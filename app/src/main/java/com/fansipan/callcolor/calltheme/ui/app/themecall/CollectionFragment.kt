package com.fansipan.callcolor.calltheme.ui.app.themecall

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.base.BaseFragment
import com.fansipan.callcolor.calltheme.databinding.FragmentCollectionBinding
import com.fansipan.callcolor.calltheme.model.CallThemeScreenModel
import com.fansipan.callcolor.calltheme.model.ItemSavedModel
import com.fansipan.callcolor.calltheme.utils.SharePreferenceUtils
import com.fansipan.callcolor.calltheme.utils.ex.clickSafe
import com.fansipan.callcolor.calltheme.utils.data.DataSaved
import com.fansipan.callcolor.calltheme.utils.data.ThemeCallUtils
import com.fansipan.callcolor.calltheme.utils.ex.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class CollectionFragment : BaseFragment() {

    private lateinit var binding: FragmentCollectionBinding

    private var position = 0

    val data = ArrayList<CallThemeScreenModel>()

    private val adapterCollection by lazy {
        CollectionAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCollectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
        initListener()
    }

    private fun initData() {
        position = requireArguments().getInt("position")
        data.clear()
        data.addAll(ThemeCallUtils.getThemeOfCategory(position))
    }

    private fun initView() {
        adapterCollection.setDataList(data)
        binding.rcyCollection.adapter = adapterCollection
        binding.txtNameCategory.text = ThemeCallUtils.listCategory[position].name
    }



    private fun initListener() {
        binding.imgBack.clickSafe { onBack() }


        adapterCollection.setOnClickItem { item, position ->
            clickItemCollection(item, position)
        }
    }

    private fun clickItemCollection(item : CallThemeScreenModel?, position: Int) {
        item?.let {
            val fileName = "${item.category}_${item.id}.png"
            if (SharePreferenceUtils.isThemeDownload(fileName)) {
                findNavController().navigate(
                    R.id.action_collectionFragment_to_editThemeFragment,
                    bundleOf("type" to "theme", "data" to "")
                )
            } else {
                downloadAnim(it, onDone = {
                    lifecycleScope.launch(Dispatchers.Main) {
                        SharePreferenceUtils.setThemeDownload(fileName, true)
                        adapterCollection.notifyItemChanged(position)
                    }
                }, onFail = {
                    lifecycleScope.launch(Dispatchers.Main) {
                        requireContext().showToast(getString(R.string.error))
                    }
                })
            }
        }
    }

    private fun downloadAnim(item: CallThemeScreenModel, onDone: () -> Unit, onFail: () -> Unit) =
        lifecycleScope.launch(Dispatchers.IO) {
            val outputStream: FileOutputStream
            val outputFile: File
            try {
                showDialogDownload()
                val fileName = "${item.category}_${item.id}.png"
                outputFile = File(
                    Environment.getExternalStorageDirectory().absoluteFile.toString() + "/Android/data/" + requireContext().packageName +
                            "/" + fileName
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
                    DataSaved.addNewDownload(requireContext(), ItemSavedModel(outputFile.absolutePath, item.avatar, item.buttonIndex))
                    findNavController().navigate(
                        R.id.action_collectionFragment_to_editThemeFragment,
                        bundleOf("type" to "theme", "data" to "")
                    )
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