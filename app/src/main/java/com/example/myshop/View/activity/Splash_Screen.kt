package com.example.myshop.View.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.myshop.MainActivity
import com.example.myshop.R
import com.example.myshop.databinding.ActivitySplashScreenBinding
import com.kiprotich.japheth.TextAnim


@Suppress("DEPRECATION")
class Splash_Screen : AppCompatActivity() {
    lateinit var Splash_binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Splash_binding = DataBindingUtil.setContentView(this, R.layout.activity_splash__screen)
        hide_statusbar()
        load_splash()
    }

    fun hide_statusbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    fun load_splash() {
        Splash_binding.textWriter
            .setWidth(12f)
            .setDelay(40)
            .setColor(Color.WHITE)
            .setConfig(TextAnim.Configuration.INTERMEDIATE)
            .setSizeFactor(30f)
            .setLetterSpacing(20f)
            .setText("MY SHOP")
            .setListener( {
                Handler(mainLooper).postDelayed({
                    startActivity(Intent(this, Login::class.java))
                    finish()
                }, 500)

            })
            .startAnimation()
    }
}