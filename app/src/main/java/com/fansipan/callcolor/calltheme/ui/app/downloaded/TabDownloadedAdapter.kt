package com.fansipan.callcolor.calltheme.ui.app.downloaded

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TabDownloadedAdapter(private val fm: FragmentManager, private val count: Int) :
    FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return count
    }

    override fun getItem(position: Int): Fragment {
        return SavedFragment(position)
    }
}