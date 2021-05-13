package com.example.antenna.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager.widget.ViewPager
import com.example.antenna.MainActivity
import com.example.antenna.R
import com.example.antenna.adpater.DataList
import com.example.antenna.adpater.RecyclerAdapter
import com.example.antenna.adpater.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    // 뷰페이저
    private val adapter by lazy { activity?.let { ViewPagerAdapter(it.supportFragmentManager) } }

    private val list = mutableListOf<DataList>()
    private val adapter1 = RecyclerAdapter(list)

    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater,
            @Nullable container: ViewGroup?,
            @Nullable savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val intent = Intent(activity, MainFragment::class.java)


        if(intent.hasExtra("id")){ // intent id 값이 있으면...
            list.add(DataList((activity as MainActivity).getDrawable(R.mipmap.samsung), "삼성전자", "-2.0%"))
            /*list.add(DataList(null, "삼성생명", "2.0%"))
            list.add(DataList(null, "삼성전자우", "-0.2%"))
            list.add(DataList(null, "삼성SDI", "0.0%"))
            list.add(DataList(null, "삼성SDS", "-2.0%"))
            list.add(DataList(null, "현대", "-1.0%"))*/

            rv_data.adapter = adapter1
        }
        else{
            list.add(DataList(null, " ", " "))
            list.add(DataList(null, " ", " "))

            rv_data.adapter = adapter1
        }

        viewPager_main.adapter = adapter
        viewPager_main.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(p0 : Int, p1 : Float, p2 : Int) {}

            override fun onPageSelected(p0: Int) {
                indicator0_iv_main.setImageDrawable(ResourcesCompat.getDrawable(activity!!.resources, R.drawable.shape_circle_gray, null))
                indicator1_iv_main.setImageDrawable(ResourcesCompat.getDrawable(activity!!.resources, R.drawable.shape_circle_gray, null))
                indicator2_iv_main.setImageDrawable(ResourcesCompat.getDrawable(activity!!.resources, R.drawable.shape_circle_gray, null))

                when(p0){
                    0 -> indicator0_iv_main.setImageDrawable(ResourcesCompat.getDrawable(activity!!.resources, R.drawable.shape_circle_purple, null))
                    1 -> indicator1_iv_main.setImageDrawable(ResourcesCompat.getDrawable(activity!!.resources, R.drawable.shape_circle_purple, null))
                    2 -> indicator2_iv_main.setImageDrawable(ResourcesCompat.getDrawable(activity!!.resources, R.drawable.shape_circle_purple, null))
                }
            }

            override fun onPageScrollStateChanged(p0: Int) {}
        })
    }

    fun refreshAdapter() {
        adapter1.setTaskList(list)
        adapter1.notifyDataSetChanged()
    }
}