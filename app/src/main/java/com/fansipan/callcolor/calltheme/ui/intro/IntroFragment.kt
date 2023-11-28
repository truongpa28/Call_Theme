package com.fansipan.callcolor.calltheme.ui.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.databinding.FragmentIntroBinding

class IntroFragment(val position: Int) : Fragment() {

    private lateinit var binding: FragmentIntroBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (position) {
            1 -> {
                binding.imgContent.setImageResource(R.drawable.img_intro_2)
                binding.title.text = getString(R.string.intro_title_2)
                binding.content.text = getString(R.string.intro_content_2)
            }

            2 -> {
                binding.imgContent.setImageResource(R.drawable.img_intro_3)
                binding.title.text = getString(R.string.intro_title_3)
                binding.content.text = getString(R.string.intro_content_3)
            }

            else -> {
                binding.imgContent.setImageResource(R.drawable.img_intro_1)
                binding.title.text = getString(R.string.intro_title_1)
                binding.content.text = getString(R.string.intro_content_1)
            }
        }

    }
}