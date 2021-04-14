package com.example.antenna

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.antenna.Fragment.*


class MainActivity : AppCompatActivity() {
    private var mBottomNV: BottomNavigationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mBottomNV = findViewById(R.id.nav_view)
        mBottomNV!!.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->

            //NavigationItemSelecte
            BottomNavigate(menuItem.itemId)
            true
        })
        mBottomNV!!.selectedItemId = R.id.navigation_1
    }

    @SuppressLint("SetTextI18n")
    private fun BottomNavigate(id: Int) {  //BottomNavigation 페이지 변경


        val tag = id.toString()
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val currentFragment: Fragment? = fragmentManager.primaryNavigationFragment
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment)
        }
        var fragment: Fragment? = fragmentManager.findFragmentByTag(tag)
        if (fragment == null) {
            fragment = if (id == R.id.navigation_1)
                MainFragment()
            else if (id == R.id.navigation_2)
                ClickFragment()
            else if (id == R.id.navigation_3)
                NewsFragment()
            else if (id == R.id.navigation_4)
                AssetFragment()
            else
                InfoFragment()

            fragmentTransaction.add(R.id.content_layout, fragment, tag)
        } else {
            fragmentTransaction.show(fragment)
        }
        fragmentTransaction.setPrimaryNavigationFragment(fragment)
        fragmentTransaction.setReorderingAllowed(true)
        fragmentTransaction.commitNow()
    }
}