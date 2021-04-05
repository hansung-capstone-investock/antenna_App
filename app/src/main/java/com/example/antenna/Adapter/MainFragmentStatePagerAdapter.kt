package com.example.antenna.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.antenna.Fragment.*

@Suppress("DEPRECATION")
class MainFragmentStatePagerAdapter(fm : FragmentManager, val fragmentCount :
Int) : FragmentStatePagerAdapter(fm)
{
    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> return ClickFragment()
            1 -> return NewsFragment()
            2 -> return InfoFragment()
            3 -> return MyFragment()
            else -> return MpageFragment()
        }
    }

    override fun getCount(): Int =fragmentCount
}