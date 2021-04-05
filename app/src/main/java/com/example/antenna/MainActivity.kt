package com.example.antenna

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.example.antenna.Adapter.MainFragmentStatePagerAdapter
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureBottomNavigation()
    }

    private fun configureBottomNavigation(){
        val vp_ac_main_frag_pager = findViewById<ViewPager>(R.id.vp_ac_main_frag_pager)


        vp_ac_main_frag_pager.adapter = MainFragmentStatePagerAdapter(supportFragmentManager, 5)

        val tl_ac_main_bottom_menu = findViewById<TabLayout>(R.id.tl_ac_main_bottom_menu)

        tl_ac_main_bottom_menu.setupWithViewPager(vp_ac_main_frag_pager)

        val bottomNaviLayout: View = this.layoutInflater.inflate(R.layout.bottom_navigation_tab, null, false)

        tl_ac_main_bottom_menu.getTabAt(0)!!.customView = bottomNaviLayout.findViewById(R.id.btn_bottom_navi_home_tab)
        tl_ac_main_bottom_menu.getTabAt(1)!!.customView = bottomNaviLayout.findViewById(R.id.btn_bottom_navi_search_tab)
        tl_ac_main_bottom_menu.getTabAt(2)!!.customView = bottomNaviLayout.findViewById(R.id.btn_bottom_navi_add_tab)
        tl_ac_main_bottom_menu.getTabAt(3)!!.customView = bottomNaviLayout.findViewById(R.id.btn_bottom_navi_like_tab)
        tl_ac_main_bottom_menu.getTabAt(4)!!.customView = bottomNaviLayout.findViewById(R.id.btn_bottom_navi_my_page_tab)



    }
}