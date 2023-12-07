package com.fansipan.callcolor.calltheme.ui.app.diy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fansipan.callcolor.calltheme.base.BaseFragment
import com.fansipan.callcolor.calltheme.databinding.FragmentIconCallBinding
import com.fansipan.callcolor.calltheme.ui.app.diy.adapter.IconCallAdapterV2
import com.fansipan.callcolor.calltheme.utils.data.DataUtils
import com.fansipan.callcolor.calltheme.utils.ex.clickSafe
import com.fansipan.callcolor.calltheme.utils.data.IconCallUtils

class IconCallFragment : BaseFragment() {

    private lateinit var binding: FragmentIconCallBinding

    private val adapterIconCall by lazy {
        IconCallAdapterV2()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIconCallBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() {
        adapterIconCall.setDataList(IconCallUtils.listIconCall)
        binding.rcyIconCall.adapter = adapterIconCall
    }

    private fun initListener() {
        binding.imgBack.clickSafe { onBack() }
        adapterIconCall.setOnClickItem { _, position ->
            DataUtils.callThemeEdit.buttonIndex = (position + 1).toString()
            onBack()
        }
    }

}