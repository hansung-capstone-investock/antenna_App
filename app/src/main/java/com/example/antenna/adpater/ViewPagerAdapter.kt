package com.example.antenna.adpater

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.antenna.fragment.KosdaqFragment
import com.example.antenna.fragment.KospiFragment

@SuppressLint("WrongConstant")
class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount() = 3

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> KospiFragment()
            1 -> KosdaqFragment()
            else -> KosdaqFragment()
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }

}