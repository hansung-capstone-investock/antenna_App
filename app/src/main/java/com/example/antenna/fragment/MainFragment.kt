package com.example.antenna.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.service.quicksettings.Tile
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.setFragmentResultListener
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

    private var r_name : String = ""
    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater,
            @Nullable container: ViewGroup?,
            @Nullable savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


            setFragmentResultListener("requestKey") { requestKey, bundle ->
                val k_name = bundle.getString("id")

                if (k_name != null) {
                    r_name = k_name
                }
            }

        list.add(DataList((activity as MainActivity).getDrawable(R.mipmap.samsung), "삼성전자", "-2.0%"))
        rv_data.adapter = adapter1

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