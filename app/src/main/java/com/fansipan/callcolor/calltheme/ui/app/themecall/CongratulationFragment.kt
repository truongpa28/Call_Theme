package com.fansipan.callcolor.calltheme.ui.app.themecall

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.base.BaseFragment
import com.fansipan.callcolor.calltheme.databinding.FragmentCollectionBinding
import com.fansipan.callcolor.calltheme.databinding.FragmentCongratulationBinding
import com.fansipan.callcolor.calltheme.utils.SharePreferenceUtils
import com.fansipan.callcolor.calltheme.utils.data.AvatarUtils
import com.fansipan.callcolor.calltheme.utils.data.IconCallUtils

class CongratulationFragment : BaseFragment() {

    private lateinit var binding: FragmentCongratulationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCongratulationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun showPreview() {
        Glide.with(requireContext()).load(SharePreferenceUtils.getBackgroundChoose()).into(binding.imgBackground)

        val posButton = SharePreferenceUtils.getIconCallChoose().toInt() - 1
        binding.imgIconCall1.setImageResource(IconCallUtils.listIconCall[posButton].icon1)
        binding.imgIconCall2.setImageResource(IconCallUtils.listIconCall[posButton].icon2)

        try {
            val posAvt = SharePreferenceUtils.getAvatarChoose()
            if (posAvt.length < 3) {
                Glide.with(this)
                    .asBitmap()
                    .load(AvatarUtils.listAvatar[posAvt.toInt()])
                    .into(binding.imgAvatar)
                    .onLoadFailed(ContextCompat.getDrawable(requireContext(), AvatarUtils.listAvatar[1]))
            } else {
                Glide.with(this)
                    .asBitmap()
                    .load(SharePreferenceUtils.getAvatarChoose())
                    .into(binding.imgAvatar)
                    .onLoadFailed(ContextCompat.getDrawable(requireContext(), AvatarUtils.listAvatar[1]))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //binding.animLottie.playAnimation()

    }

    private fun initView() {
        showPreview()
    }

    private fun initListener() {

    }
}