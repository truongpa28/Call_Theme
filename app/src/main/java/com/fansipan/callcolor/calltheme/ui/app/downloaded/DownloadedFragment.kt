package com.fansipan.callcolor.calltheme.ui.app.downloaded

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.base.BaseFragment
import com.fansipan.callcolor.calltheme.databinding.FragmentDownloadedBinding
import com.fansipan.callcolor.calltheme.utils.ex.clickSafe
import com.google.android.material.tabs.TabLayout


class DownloadedFragment : BaseFragment() {


    private lateinit var binding: FragmentDownloadedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDownloadedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() {
        binding.tabLayout.addTab(
            binding.tabLayout.newTab().setText(getString(R.string.downloaded))
        )
        binding.tabLayout.addTab(
            binding.tabLayout.newTab().setText(getString(R.string.created))
        )

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    binding.viewPager.currentItem = it.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })

        val adapter = TabDownloadedAdapter(childFragmentManager, binding.tabLayout.tabCount)

        binding.viewPager.adapter = adapter

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                binding.tabLayout.getTabAt(position)?.select()
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
    }

    private fun initListener() {
        binding.imgBack.clickSafe {
            onBack()
        }
    }
}