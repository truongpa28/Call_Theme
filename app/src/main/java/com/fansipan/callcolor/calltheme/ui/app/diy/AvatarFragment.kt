package com.fansipan.callcolor.calltheme.ui.app.diy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fansipan.callcolor.calltheme.base.BaseFragment
import com.fansipan.callcolor.calltheme.databinding.FragmentAvatarBinding
import com.fansipan.callcolor.calltheme.ui.app.diy.adapter.AvatarAdapterV2
import com.fansipan.callcolor.calltheme.utils.ex.clickSafe
import com.fansipan.callcolor.calltheme.utils.data.AvatarUtils


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