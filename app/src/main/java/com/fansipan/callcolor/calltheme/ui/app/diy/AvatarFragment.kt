package com.fansipan.callcolor.calltheme.ui.app.diy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.base.BaseFragment
import com.fansipan.callcolor.calltheme.databinding.FragmentAvatarBinding
import com.fansipan.callcolor.calltheme.databinding.FragmentDIYThemeBinding
import com.fansipan.callcolor.calltheme.ui.app.diy.adapter.AvatarAdapter
import com.fansipan.callcolor.calltheme.ui.app.diy.adapter.AvatarAdapterV2
import com.fansipan.callcolor.calltheme.ui.app.diy.adapter.IconCallAdapter
import com.fansipan.callcolor.calltheme.utils.clickSafe
import com.fansipan.callcolor.calltheme.utils.data.AvatarUtils
import com.fansipan.callcolor.calltheme.utils.data.IconCallUtils


class AvatarFragment : BaseFragment() {

    private lateinit var binding: FragmentAvatarBinding

    private val adapterAvatar by lazy {
        AvatarAdapterV2()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAvatarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() {
        adapterAvatar.setDataList(AvatarUtils.listAvatar)
        binding.rcyAvatar.adapter = adapterAvatar
    }

    private fun initListener() {
        binding.imgBack.clickSafe { onBack() }
    }

}