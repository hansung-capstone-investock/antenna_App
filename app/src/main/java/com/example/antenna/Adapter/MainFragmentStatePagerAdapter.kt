package com.example.antenna.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.antenna.Fragment.*

class MainFragmentStatePagerAdapter(fm : FragmentManager, private val fragmentCount : Int) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> MainFragment()
            1 -> ClickFragment()
            2 -> NewsFragment()
            3 -> AssetFragment()
            else -> InfoFragment()
        }
    }

    override fun getCount(): Int = fragmentCount // 자바에서는 { return fragmentCount }

}