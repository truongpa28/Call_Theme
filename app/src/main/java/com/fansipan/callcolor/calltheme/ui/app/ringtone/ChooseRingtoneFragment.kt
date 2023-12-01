package com.fansipan.callcolor.calltheme.ui.app.ringtone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fansipan.callcolor.calltheme.base.BaseFragment
import com.fansipan.callcolor.calltheme.databinding.FragmentChooseRingtoneBinding
import com.fansipan.callcolor.calltheme.utils.MediaPlayerUtils
import com.fansipan.callcolor.calltheme.utils.SharePreferenceUtils
import com.fansipan.callcolor.calltheme.utils.ex.clickSafe
import com.fansipan.callcolor.calltheme.utils.data.RingtoneUtils


class ChooseRingtoneFragment : BaseFragment() {

    private lateinit var binding: FragmentChooseRingtoneBinding

    private val adapter by lazy {
        RingtoneV2Adapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChooseRingtoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() {
        binding.rcyRingTone.adapter = adapter
        adapter.setDataList(RingtoneUtils.getListRingTone())
    }

    private fun initListener() {
        binding.imgBack.clickSafe {
            onBack()
        }

        adapter.setOnClickItem { item, position ->
            /*Constants.isRingToneChange = true*/
            SharePreferenceUtils.setRingtone(item?.nameSound ?: RingtoneUtils.nameDefaultRingtone)
            adapter.chooseRingTone(position)
            item?.let { MediaPlayerUtils.startMediaPlayer(requireContext(), it.sound) }
        }
    }


    override fun onPause() {
        super.onPause()
        MediaPlayerUtils.stopMediaPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        MediaPlayerUtils.stopMediaPlayer()
    }
}