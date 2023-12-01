package com.fansipan.callcolor.calltheme.ui.intro

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.base.BaseActivity
import com.fansipan.callcolor.calltheme.databinding.ActivityIntroBinding
import com.fansipan.callcolor.calltheme.ui.main.MainActivity
import com.fansipan.callcolor.calltheme.utils.ex.clickSafe
import com.fansipan.callcolor.calltheme.utils.ex.openActivity
import com.google.android.material.tabs.TabLayout

class IntroActivity : BaseActivity() {

    private lateinit var binding: ActivityIntroBinding

    companion object {
        const val TAB_COUNT = 3
    }

    private var posView = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

        initListener()
    }

    private fun initListener() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        })

        binding.txtSkip.clickSafe {
            actionNext()
        }

        binding.txtNext.clickSafe {
            if (posView >= TAB_COUNT - 1) {
                actionNext()
            } else {
                binding.vpgTutorial.currentItem = posView + 1
            }
        }
    }

    private fun actionNext() {
        openActivity(MainActivity::class.java, true)
    }

    private fun initView() {
        repeat(TAB_COUNT) {
            binding.tabTutorial.addTab(
                binding.tabTutorial.newTab().setText("")
            )
        }
        binding.tabTutorial.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = TabIntroAdapter(
            this,
            supportFragmentManager,
            TAB_COUNT
        )
        binding.vpgTutorial.adapter = adapter

        binding.vpgTutorial.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                binding.tabTutorial.getTabAt(position)?.select()
                posView = position
                when (position) {
                    (TAB_COUNT - 1) -> {
                        binding.txtNext.text = getString(R.string.start)
                        binding.txtNext.setTextColor(ContextCompat.getColor(this@IntroActivity, R.color.main_color))
                    }
                    else -> {
                        binding.txtNext.text = getString(R.string.next)
                        binding.txtNext.setTextColor(ContextCompat.getColor(this@IntroActivity, R.color.black_app))
                    }
                }
            }

        })
    }
}