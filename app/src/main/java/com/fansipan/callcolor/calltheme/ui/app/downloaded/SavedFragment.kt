package com.fansipan.callcolor.calltheme.ui.app.downloaded

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.base.BaseFragment
import com.fansipan.callcolor.calltheme.databinding.FragmentSavedBinding
import com.fansipan.callcolor.calltheme.model.CallThemeScreenModel
import com.fansipan.callcolor.calltheme.ui.dialog.DialogDeleteDownloaded
import com.fansipan.callcolor.calltheme.utils.data.DataSaved
import com.fansipan.callcolor.calltheme.utils.data.DataUtils
import com.fansipan.callcolor.calltheme.utils.ex.clickSafe
import com.fansipan.callcolor.calltheme.utils.ex.getPathOfBg
import com.fansipan.callcolor.calltheme.utils.ex.showOrGone
import com.fansipan.callcolor.calltheme.utils.ex.showToast

class SavedFragment(val posView: Int) : BaseFragment() {


    private lateinit var binding: FragmentSavedBinding

    private val adapter by lazy {
        SavedAdapter()
    }

    private val dialog by lazy {
        DialogDeleteDownloaded(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
        initListener()
    }

    private fun initData() {
        val data = if (posView == 0) {
            binding.txtTitle.text = getString(R.string.no_theme_downloaded)
            DataSaved.getAllDownloaded(requireContext())
        } else {
            binding.txtTitle.text = getString(R.string.no_theme_created)
            DataSaved.getAllCreated(requireContext())
        }
        binding.llNoData.showOrGone(data.size == 0)
        adapter.setDataList(data)
    }

    private fun initView() {
        binding.rcyCollection.adapter = adapter

        binding.imgAddNew.showOrGone(posView!=0)
    }

    private fun initListener() {
        adapter.setOnClickItem { item, position ->
            item?.let {data->
                findNavController().navigate(
                    R.id.action_downloadedFragment_to_editThemeFragment,
                    bundleOf("type" to "theme")
                )
                DataUtils.callThemeEdit = CallThemeScreenModel(0, 0, data.background,  data.avatar,  data.buttonIndex)
                DataUtils.tmpCallThemeEdit = DataUtils.callThemeEdit.copy()
            }
        }

        adapter.setOnClickDelete { item, position ->
            dialog.show {
                if (posView == 0) {
                    DataSaved.deleteDownloaded(requireContext(), position)
                    initData()
                } else {
                    DataSaved.deleteCreated(requireContext(), position)
                    initData()
                }
            }
        }

        binding.imgAddNew.clickSafe {
            DataUtils.callThemeEdit = CallThemeScreenModel()
            DataUtils.tmpCallThemeEdit = CallThemeScreenModel()
            findNavController().navigate(R.id.action_downloadedFragment_to_DIYThemeFragment)
        }
    }
}