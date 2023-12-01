package com.fansipan.callcolor.calltheme.ui.app.diy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fansipan.callcolor.calltheme.base.BaseFragment
import com.fansipan.callcolor.calltheme.databinding.FragmentEditThemeBinding
import com.fansipan.callcolor.calltheme.ui.app.diy.adapter.IconCallAdapter
import com.fansipan.callcolor.calltheme.utils.ex.clickSafe
import com.fansipan.callcolor.calltheme.utils.data.IconCallUtils

class EditThemeFragment : BaseFragment() {

    private lateinit var binding: FragmentEditThemeBinding

    private val adapterIconCall by lazy {
        IconCallAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditThemeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() {
        adapterIconCall.setDataList(IconCallUtils.listIconCall.subList(0, 11))
        binding.rcyCallIcon.adapter = adapterIconCall
    }

    private fun initListener() {
        binding.imgBack.clickSafe { onBack() }
        binding.imgChooseBackground.clickSafe {

        }
    }
}