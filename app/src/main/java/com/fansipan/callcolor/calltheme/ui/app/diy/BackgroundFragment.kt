package com.fansipan.callcolor.calltheme.ui.app.diy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.base.BaseFragment
import com.fansipan.callcolor.calltheme.databinding.FragmentBackgroundBinding
import com.fansipan.callcolor.calltheme.databinding.FragmentIconCallBinding
import com.fansipan.callcolor.calltheme.ui.app.diy.adapter.IconCallAdapterV2
import com.fansipan.callcolor.calltheme.utils.clickSafe
import com.fansipan.callcolor.calltheme.utils.data.IconCallUtils

class BackgroundFragment : BaseFragment() {

    private lateinit var binding: FragmentBackgroundBinding

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

    }

    private fun initListener() {
        binding.imgBack.clickSafe { onBack() }
    }
}