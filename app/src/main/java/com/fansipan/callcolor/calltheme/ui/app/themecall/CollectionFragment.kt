package com.fansipan.callcolor.calltheme.ui.app.themecall

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.base.BaseFragment
import com.fansipan.callcolor.calltheme.databinding.FragmentCollectionBinding
import com.fansipan.callcolor.calltheme.databinding.FragmentThemeCallBinding
import com.fansipan.callcolor.calltheme.model.CallThemeScreenModel
import com.fansipan.callcolor.calltheme.utils.DataUtils
import com.fansipan.callcolor.calltheme.utils.clickSafe
import com.fansipan.callcolor.calltheme.utils.data.ThemeCallUtils


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
        binding.rcyCategory.adapter = adapterCollection
        binding.txtNameCategory.text = ThemeCallUtils.listCategory[position].name
    }



    private fun initListener() {
        binding.imgBack.clickSafe { onBack() }
    }
}