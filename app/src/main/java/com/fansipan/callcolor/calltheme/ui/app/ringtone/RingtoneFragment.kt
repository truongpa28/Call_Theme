package com.fansipan.callcolor.calltheme.ui.app.ringtone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.base.BaseFragment
import com.fansipan.callcolor.calltheme.databinding.FragmentRingtoneBinding
import com.fansipan.callcolor.calltheme.utils.clickSafe

class RingtoneFragment : BaseFragment() {


    private lateinit var binding: FragmentRingtoneBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRingtoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    private fun initListener() {
        binding.imgBack.clickSafe { onBack() }


        binding.llChooseRingtone.clickSafe {
            findNavController().navigate(R.id.action_ringtoneFragment_to_chooseRingtoneFragment)
        }


        binding.llChooseAudio.clickSafe {

        }
    }
}