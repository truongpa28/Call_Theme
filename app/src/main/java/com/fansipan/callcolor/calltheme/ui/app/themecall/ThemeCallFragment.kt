package com.fansipan.callcolor.calltheme.ui.app.themecall

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.base.BaseFragment
import com.fansipan.callcolor.calltheme.databinding.FragmentAlertBinding
import com.fansipan.callcolor.calltheme.databinding.FragmentThemeCallBinding
import com.fansipan.callcolor.calltheme.utils.clickSafe
import com.fansipan.callcolor.calltheme.utils.data.ThemeCallUtils

class ThemeCallFragment : BaseFragment() {

    private lateinit var binding: FragmentThemeCallBinding

    private val adapterCategory by lazy {
        CategoryThemeAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentThemeCallBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() {
        adapterCategory.setDataList(ThemeCallUtils.listCategory)
        binding.rcyCategory.adapter = adapterCategory
    }

    private fun initListener() {
        binding.imgBack.clickSafe { onBack() }

        adapterCategory.setOnClickItem { item, position ->
            findNavController().navigate(R.id.action_themeCallFragment_to_collectionFragment, bundleOf(
                "position" to position
            ))
        }

    }
}