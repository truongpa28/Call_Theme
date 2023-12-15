package com.fansipan.callcolor.calltheme.ui.app.diy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.base.BaseFragment
import com.fansipan.callcolor.calltheme.databinding.FragmentEditThemeBinding
import com.fansipan.callcolor.calltheme.ui.app.diy.adapter.IconCallAdapterV3
import com.fansipan.callcolor.calltheme.ui.dialog.DialogQuitEdit
import com.fansipan.callcolor.calltheme.utils.SharePreferenceUtils
import com.fansipan.callcolor.calltheme.utils.data.AvatarUtils
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

    private val dialogQuitEdit by lazy {
        DialogQuitEdit(requireContext())
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
            binding.imgChooseAvatar.show()
            adapterIconCall.setDataList(IconCallUtils.listIconCall.subList(0, COUNT_ICON))
            binding.rcyCallIcon.adapter = adapterIconCall
            binding.txtSave.text = getString(R.string.save)
        } else {
            binding.rcyCallIcon.gone()
            binding.imgChooseAvatar.gone()
            binding.imgChooseBackground.gone()
            binding.txtSave.text = getString(R.string.apply)
            val loadAnimation: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.jump_button)
            binding.imgIconCall1.startAnimation(loadAnimation)
            binding.imgIconCall2.startAnimation(loadAnimation)
        }

        showUiThemeCall()
    }

    private fun showUiThemeCall() {
        DataUtils.tmpCallThemeEdit.let { item ->
            Glide.with(requireContext())
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
            if (type == "diy") {
                findNavController().navigate(R.id.action_editThemeFragment_to_avatarFragment)
            }
        }

        adapterIconCall.setOnClickItem { item, position ->
            if (position == COUNT_ICON - 1) {
                findNavController().navigate(R.id.action_editThemeFragment_to_iconCallFragment)
            } else {
                DataUtils.tmpCallThemeEdit.buttonIndex = (position + 1).toString()
                binding.imgIconCall1.setImageResource(IconCallUtils.listIconCall[position].icon1)
                binding.imgIconCall2.setImageResource(IconCallUtils.listIconCall[position].icon2)
            }
        }

        binding.txtSave.clickSafe {
            saveChoose()
        }
    }


    private fun saveChoose() {
        if (type == "diy") {
            DataUtils.callThemeEdit = DataUtils.tmpCallThemeEdit.copy()
            findNavController().popBackStack()
        } else {
            if (!isAllPermissionCallTheme()) {
                showDialogPermission {
                    applyThemeCall()
                }
            } else {
                applyThemeCall()
            }
        }

    }

    private fun applyThemeCall() {
        SharePreferenceUtils.setAvatarChoose(DataUtils.callThemeEdit.avatar)
        SharePreferenceUtils.setIconCallChoose(DataUtils.callThemeEdit.buttonIndex)
        SharePreferenceUtils.setBackgroundChoose(DataUtils.callThemeEdit.background)
        findNavController().navigate(R.id.action_editThemeFragment_to_congratulationFragment)
    }

    override fun onBack() {
        if (type == "diy") {
            dialogQuitEdit.show {
                DataUtils.tmpCallThemeEdit = DataUtils.callThemeEdit.copy()
                findNavController().popBackStack()
            }
        } else {
            findNavController().popBackStack()
        }
    }
}