package com.fansipan.callcolor.calltheme.ui.app.diy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.base.BaseFragment
import com.fansipan.callcolor.calltheme.databinding.FragmentDIYThemeBinding
import com.fansipan.callcolor.calltheme.ui.app.diy.adapter.AvatarAdapter
import com.fansipan.callcolor.calltheme.ui.app.diy.adapter.IconCallAdapter
import com.fansipan.callcolor.calltheme.utils.ex.clickSafe
import com.fansipan.callcolor.calltheme.utils.data.AvatarUtils
import com.fansipan.callcolor.calltheme.utils.data.IconCallUtils

class DIYThemeFragment : BaseFragment() {

    private lateinit var binding: FragmentDIYThemeBinding

    private val adapterAvatar by lazy {
        AvatarAdapter()
    }

    private val adapterIconCall by lazy {
        IconCallAdapter()
    }

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
        adapterAvatar.setDataList(AvatarUtils.listAvatar.subList(0, 11))
        binding.rcyAvatar.adapter = adapterAvatar
        adapterIconCall.setDataList(IconCallUtils.listIconCall.subList(0, 11))
        binding.rcyCallIcon.adapter = adapterIconCall
    }

    private fun initListener() {
        binding.imgBack.clickSafe { onBack() }


        adapterAvatar.setOnClickItem { item, position ->
            findNavController().navigate(R.id.action_DIYThemeFragment_to_editThemeFragment)
        }

        adapterIconCall.setOnClickItem { item, position ->
            findNavController().navigate(R.id.action_DIYThemeFragment_to_editThemeFragment)
        }

        binding.txtPreview.clickSafe {
            findNavController().navigate(R.id.action_DIYThemeFragment_to_previewFragment)
        }
        binding.llPreview.clickSafe {
            findNavController().navigate(R.id.action_DIYThemeFragment_to_previewFragment)
        }
    }
}