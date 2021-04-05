package com.example.antenna.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.antenna.Fragment.*

class MainFragmentStatePagerAdapter(fm : FragmentManager, val fragmentCount : Int) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> return ClickFragment()
            1 -> return InfoFragment()
            2 -> return AssetFragment()
            3 -> return NewsFragment()
            else -> return MypageFragment()
        }
    }

    override fun getCount(): Int = fragmentCount // 자바에서는 { return fragmentCount }

}