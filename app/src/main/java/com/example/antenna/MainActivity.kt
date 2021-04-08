package com.example.antenna

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.viewpager.widget.ViewPager
import com.example.antenna.Adapter.MainFragmentStatePagerAdapter
import com.google.android.material.tabs.TabLayout
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView
import org.jetbrains.anko.startActivity
import kotlin.math.log
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.example.antenna.Fragment.ClickFragment
import org.w3c.dom.Text


class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 상단바
        setSupportActionBar(findViewById(R.id.toolbar))
        val ab = supportActionBar!!
        ab.setDisplayHomeAsUpEnabled(true)

        configureBottomNavigation()
    }

    // 뒤로가기 버튼
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // 하단바 연결
    private fun configureBottomNavigation(){
        val vp_ac_main_frag_pager = findViewById<ViewPager>(R.id.vp_ac_main_frag_pager)
        vp_ac_main_frag_pager.adapter = MainFragmentStatePagerAdapter(supportFragmentManager, 5)

        val tl_ac_main_bottom_menu = findViewById<TabLayout>(R.id.tl_ac_main_bottom_menu)
        tl_ac_main_bottom_menu.setupWithViewPager(vp_ac_main_frag_pager)

        val bottomNaviLayout: View = this.layoutInflater.inflate(R.layout.bottom_navigation_tab, null, false)

        /*val home = findViewById<ImageView>(R.id.home_img)
        val click = findViewById<ImageView>(R.id.click_img)
        val txt = findViewById<TextView>(R.id.toolbar_text)

        val intent = Intent(this, ClickFragment::class.java)
        intent.putExtra("click", click)
*/

        tl_ac_main_bottom_menu.getTabAt(0)!!.customView = bottomNaviLayout.findViewById(R.id.btn_bottom_navi_home_tab)
        tl_ac_main_bottom_menu.getTabAt(1)!!.customView = bottomNaviLayout.findViewById(R.id.btn_bottom_navi_search_tab)
        tl_ac_main_bottom_menu.getTabAt(2)!!.customView = bottomNaviLayout.findViewById(R.id.btn_bottom_navi_add_tab)
        tl_ac_main_bottom_menu.getTabAt(3)!!.customView = bottomNaviLayout.findViewById(R.id.btn_bottom_navi_like_tab)
        tl_ac_main_bottom_menu.getTabAt(4)!!.customView = bottomNaviLayout.findViewById(R.id.btn_bottom_navi_my_page_tab)
    }
}