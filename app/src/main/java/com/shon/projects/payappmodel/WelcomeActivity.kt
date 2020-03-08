package com.shon.projects.payappmodel

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class WelcomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_layout)

        val list: MutableList<String> = ArrayList()
        list.add("https://drive.google.com/uc?id=14vymrSw2JoCW95aJQK_SIWUJCFpuP0_z")
        list.add("https://drive.google.com/uc?id=1Mw4kddM55GkKI1aReoN0jUjWKfq4RA9F")
        list.add("https://drive.google.com/uc?id=1qXSnz9KhV7gltr-JjzeBprMp1HxxwWL7")

        val typeface = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-SemiBold.ttf")
        val viewPager = findViewById<ViewPager2>(R.id.view_pager)
        viewPager?.adapter = WelcomeSliderAdapter(list, this)

        val tabLayout = findViewById<TabLayout>(R.id.indicator_viewPager)

        TabLayoutMediator(tabLayout, viewPager) { tab, position -> }.attach()

        findViewById<Button>(R.id.login_btn).typeface = typeface
        findViewById<Button>(R.id.google_login_btn).typeface = typeface
        findViewById<Button>(R.id.sign_up_btn).typeface = typeface


        val handler = Handler()
        handler.postDelayed(Runnable {
            findViewById<RelativeLayout>(R.id.splash_screen_view).visibility = View.GONE
        }, 5000)





        findViewById<Button>(R.id.google_login_btn).setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }


    }


}
