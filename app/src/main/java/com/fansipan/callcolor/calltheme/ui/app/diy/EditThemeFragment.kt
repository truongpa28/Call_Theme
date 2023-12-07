package com.fansipan.callcolor.calltheme.ui.app.diy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.base.BaseFragment
import com.fansipan.callcolor.calltheme.databinding.FragmentEditThemeBinding
import com.fansipan.callcolor.calltheme.model.ItemSavedModel
import com.fansipan.callcolor.calltheme.ui.app.diy.adapter.IconCallAdapter
import com.fansipan.callcolor.calltheme.ui.app.diy.adapter.IconCallAdapterV3
import com.fansipan.callcolor.calltheme.utils.SharePreferenceUtils
import com.fansipan.callcolor.calltheme.utils.data.AvatarUtils
import com.fansipan.callcolor.calltheme.utils.data.DataSaved
import com.fansipan.callcolor.calltheme.utils.data.DataUtils
import com.fansipan.callcolor.calltheme.utils.data.IconCallUtils
import com.fansipan.callcolor.calltheme.utils.ex.clickSafe
import com.fansipan.callcolor.calltheme.utils.ex.gone
import com.fansipan.callcolor.calltheme.utils.ex.show

class EditThemeFragment : BaseFragment() {

    private lateinit var binding: FragmentEditThemeBinding

    private val adapterIconCall by lazy {
        IconCallAdapterV3()
    }

    private var type = "diy"

    companion object {
        const val COUNT_ICON = 12
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
        initData()
        initView()
        initListener()
    }

    private fun initData() {
        type = requireArguments().getString("type", "diy")
    }

    private fun initView() {
        if (type == "diy") {
            binding.rcyCallIcon.show()
            binding.imgChooseBackground.show()
            adapterIconCall.setDataList(IconCallUtils.listIconCall.subList(0, COUNT_ICON))
            binding.rcyCallIcon.adapter = adapterIconCall

        } else {
            binding.rcyCallIcon.gone()
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

            try {
                val posAvt = item.avatar.toInt()
                binding.imgAvatar.setImageResource(AvatarUtils.listAvatar[posAvt])
            } catch (e: Exception) {
                Glide.with(requireContext())
                    .asBitmap()
                    .load(item.avatar)
                    .into(binding.imgAvatar)
            }

        }
    }

    private fun initListener() {
        binding.imgBack.clickSafe { onBack() }

        binding.imgChooseBackground.clickSafe {
            findNavController().navigate(R.id.action_editThemeFragment_to_backgroundFragment)
        }

        binding.imgAvatar.clickSafe {
            findNavController().navigate(R.id.action_editThemeFragment_to_avatarFragment)
        }

        adapterIconCall.setOnClickItem { item, position ->
            if (position == COUNT_ICON -1 ) {
                findNavController().navigate(R.id.action_editThemeFragment_to_iconCallFragment)
            } else {
                DataUtils.callThemeEdit.buttonIndex = (position + 1).toString()
                binding.imgIconCall1.setImageResource(IconCallUtils.listIconCall[position].icon1)
                binding.imgIconCall2.setImageResource(IconCallUtils.listIconCall[position].icon2)
            }

        }

        binding.txtSave.clickSafe {
            saveChoose()
        }
    }


    private fun saveChoose() {
        SharePreferenceUtils.setAvatarChoose(DataUtils.callThemeEdit.avatar)
        SharePreferenceUtils.setIconCallChoose(DataUtils.callThemeEdit.buttonIndex)
        SharePreferenceUtils.setBackgroundChoose(DataUtils.callThemeEdit.background)
        if (type == "diy") {
            DataSaved.addNewCreate(
                requireContext(),
                ItemSavedModel(
                    DataUtils.callThemeEdit.background,
                    DataUtils.callThemeEdit.avatar,
                    DataUtils.callThemeEdit.buttonIndex,
                    false
                )
            )
        }
        findNavController().navigate(R.id.action_editThemeFragment_to_congratulationFragment)

    }
}