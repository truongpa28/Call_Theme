package com.fansipan.callcolor.calltheme.ui.intro

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TabIntroAdapter(private val myContext: Context, fm: FragmentManager, private var totalTabs: Int) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return IntroFragment(position)
    }

    override fun getCount(): Int {
        return totalTabs
    }
}