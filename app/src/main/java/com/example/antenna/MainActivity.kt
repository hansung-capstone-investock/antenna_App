package com.example.antenna

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.antenna.Adapter.MainFragmentStatePagerAdapter
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    // val text = findViewById<TextView>(R.id.textView3)

    override fun onCreate(savedInstanceState: Bundle?) {
        // setTheme(R.style.SplashTheme)
        // StrictMode.enableDefaults(); // this is a enable
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pagerAdapter = MainFragmentStatePagerAdapter(supportFragmentManager, 5)
        val pager = findViewById<ViewPager>(R.id.main_pager)
        pager.adapter = pagerAdapter

        val tab = findViewById<TabLayout>(R.id.main_bottom_menu)
        tab.setupWithViewPager(pager)
    }

    // private fun configureBottomNavigation(){
        // main_pager.adapter = MainFragmentStatePagerAdapter(supportFragmentManager, 5)

        // val bottomNaviLayout: View = this.layoutInflater.inflate(R.layout.bottom_navigation_tab, null, false)
    //}
}