package com.example.antenna.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager.widget.ViewPager
import com.example.antenna.R
import com.example.antenna.adpater.*
import com.example.antenna.sharedPreference.App
import kotlinx.android.synthetic.main.fragment_main.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainFragment : Fragment() {

    // 뷰페이저
    private val adapter by lazy { activity?.let { ViewPagerAdapter(it.supportFragmentManager) } }

    private val list = mutableListOf<DataList>()
    private val commList = mutableListOf<CommunityList>()

    private val adapter1 = RecyclerAdapter(list)

    // 커뮤니티 가져오기
    private val adapter2 = CommuityAdapter(commList)

    private val CommunityRetrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-13-125-236-101.ap-northeast-2.compute.amazonaws.com:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    // 서비스 정의하기

    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater,
            @Nullable container: ViewGroup?,
            @Nullable savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity?.let {
            super.onViewCreated(view, savedInstanceState)

            val id = App.prefs.id
            Log.d("SHARED NAME", id.toString())

            if (App.prefs.id.isNullOrBlank()) {
                Username.text = "로그인을 해주세요"
            } else {
                Username.text = id.toString() + "님 안녕하세요"

                /*list.add(DataList(App.prefs.getArrayList1().companies.company2.toString(), ""))
            list.add(DataList(App.prefs.getArrayList1().companies.company3.toString(), ""))
            list.add(DataList(App.prefs.getArrayList1().companies.company4.toString(), ""))
            list.add(DataList(App.prefs.getArrayList1().companies.company5.toString(), ""))
            list.add(DataList(App.prefs.getArrayList1().companies.company6.toString(), ""))
            list.add(DataList(App.prefs.getArrayList1().companies.company7.toString(), ""))
            list.add(DataList(App.prefs.getArrayList1().companies.company8.toString(), ""))
            list.add(DataList(App.prefs.getArrayList1().companies.company9.toString(), ""))
            list.add(DataList(App.prefs.getArrayList1().companies.company10.toString(), ""))*/

                rv_data.adapter = adapter1

                refreshButton.setOnClickListener {
                    println("refrsh button click")
                    App.prefs.getArrayList1()
                }
            }

            viewPager_main.adapter = adapter
            viewPager_main.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

                override fun onPageSelected(p0: Int) {
                    indicator0_iv_main.setImageDrawable(ResourcesCompat.getDrawable(activity!!.resources, R.drawable.shape_circle_gray, null))
                    indicator1_iv_main.setImageDrawable(ResourcesCompat.getDrawable(activity!!.resources, R.drawable.shape_circle_gray, null))
                    indicator2_iv_main.setImageDrawable(ResourcesCompat.getDrawable(activity!!.resources, R.drawable.shape_circle_gray, null))

                    when (p0) {
                        0 -> indicator0_iv_main.setImageDrawable(ResourcesCompat.getDrawable(activity!!.resources, R.drawable.shape_circle_purple, null))
                        1 -> indicator1_iv_main.setImageDrawable(ResourcesCompat.getDrawable(activity!!.resources, R.drawable.shape_circle_purple, null))
                        2 -> indicator2_iv_main.setImageDrawable(ResourcesCompat.getDrawable(activity!!.resources, R.drawable.shape_circle_purple, null))
                    }
                }

                override fun onPageScrollStateChanged(p0: Int) {}
            })
        }
    }
    fun refreshAdapter() {
        // 아예 초기화
        adapter1.setTaskList(list)
        adapter1.notifyDataSetChanged()
    }

    fun loadCommunity(){

    }
}