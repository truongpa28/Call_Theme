package com.fansipan.callcolor.calltheme.ui.splash

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.OnBackPressedCallback
import com.fansipan.callcolor.calltheme.R
import com.fansipan.callcolor.calltheme.databinding.ActivitySplashBinding
import com.fansipan.callcolor.calltheme.ui.language.LanguageActivity
import com.fansipan.callcolor.calltheme.utils.openActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        })

        Handler(Looper.getMainLooper()).postDelayed({
            openActivity(LanguageActivity::class.java, true)
        }, 1500L)

    }
}