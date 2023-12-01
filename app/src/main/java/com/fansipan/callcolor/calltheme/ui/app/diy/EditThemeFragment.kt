package com.fansipan.callcolor.calltheme.ui.app.diy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.fansipan.callcolor.calltheme.base.BaseFragment
import com.fansipan.callcolor.calltheme.databinding.FragmentEditThemeBinding
import com.fansipan.callcolor.calltheme.ui.app.diy.adapter.IconCallAdapter
import com.fansipan.callcolor.calltheme.utils.data.AvatarUtils
import com.fansipan.callcolor.calltheme.utils.data.DataUtils
import com.fansipan.callcolor.calltheme.utils.ex.clickSafe
import com.fansipan.callcolor.calltheme.utils.data.IconCallUtils
import com.fansipan.callcolor.calltheme.utils.ex.gone
import com.fansipan.callcolor.calltheme.utils.ex.show

class EditThemeFragment : BaseFragment() {

    private lateinit var binding: FragmentEditThemeBinding

    private val adapterIconCall by lazy {
        IconCallAdapter()
    }

    private var type = "diy"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditThemeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
        initListener()
    }

    private fun initData() {
        type = requireArguments().getString("type", "diy")
    }

    private fun initView() {
        if (type == "diy") {
            binding.llChooseIconCall.show()
            binding.imgChooseBackground.show()
            adapterIconCall.setDataList(IconCallUtils.listIconCall.subList(0, 11))
            binding.rcyCallIcon.adapter = adapterIconCall

        } else {
            binding.llChooseIconCall.gone()
            binding.imgChooseBackground.gone()
        }

        showUiThemeCall()
    }

    private fun showUiThemeCall() {
        DataUtils.callThemeEdit.let { item ->
            Glide.with(requireContext())
                .asBitmap()
                .load(item.background)
                .into(binding.imgBackground)
            val posButton = item.buttonIndex.toInt() - 1
            binding.imgIconCall1.setImageResource(IconCallUtils.listIconCall[posButton].icon1)
            binding.imgIconCall2.setImageResource(IconCallUtils.listIconCall[posButton].icon2)
            binding.imgAvatar.setImageResource(AvatarUtils.listAvatar[item.avatar.toInt()])

        }
    }

    private fun initListener() {
        binding.imgBack.clickSafe { onBack() }
        binding.imgChooseBackground.clickSafe {

        }
    }
}