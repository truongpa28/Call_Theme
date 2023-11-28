package com.fansipan.callcolor.calltheme.ui.language

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.fansipan.callcolor.calltheme.base.BaseActivity
import com.fansipan.callcolor.calltheme.databinding.ActivityLanguageBinding
import com.fansipan.callcolor.calltheme.model.LanguageModel
import com.fansipan.callcolor.calltheme.ui.intro.IntroActivity
import com.fansipan.callcolor.calltheme.utils.LanguageUtils
import com.fansipan.callcolor.calltheme.utils.SharePreferenceUtils
import com.fansipan.callcolor.calltheme.utils.clickSafe
import com.fansipan.callcolor.calltheme.utils.openActivity
import com.fansipan.callcolor.calltheme.utils.setLanguageApp

class LanguageActivity : BaseActivity(), ClickLanguageListener {

    private lateinit var binding: ActivityLanguageBinding

    private val adapter by lazy {
        LanguageAdapter(this, LanguageUtils.listLanguage, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

        initListener()

    }

    private fun initListener() {
        binding.txtDone.clickSafe {
            val code = LanguageUtils.listLanguage[adapter.getChoose()].key
            setLanguageApp(code)
            SharePreferenceUtils.setCodeLanguageChoose(code)
            openActivity(IntroActivity::class.java, true)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        })
    }

    private fun initView() {
        adapter.setChoose(LanguageUtils.getPositionChoose(this))
        binding.rcyLanguage.adapter = adapter
    }

    override fun clickLanguage(position: Int, languageModel: LanguageModel) {
        adapter.setChoose(position)
    }
}