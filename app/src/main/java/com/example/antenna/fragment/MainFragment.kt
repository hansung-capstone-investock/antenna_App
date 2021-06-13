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
import com.example.antenna.`interface`.CommunityService
import com.example.antenna.adpater.*
import com.example.antenna.dataclass.CommunityData
import com.example.antenna.sharedPreference.App
import kotlinx.android.synthetic.main.fragment_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainFragment : Fragment() {

    // 뷰페이저
    private val adapter by lazy { activity?.let { ViewPagerAdapter(it.supportFragmentManager) } }

    private val list = mutableListOf<DataList>()
    private val adapter1 = RecyclerAdapter(list)

    // 커뮤니티 언급 단어 리스트
    private val commList = mutableListOf<CommunityList>()

    // 커뮤니티 가져오기
    private val adapter2 = CommuityAdapter(commList)

    private val communityRetrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-3-37-87-254.ap-northeast-2.compute.amazonaws.com:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    // 서비스 정의하기
    private val communityServer : CommunityService? = communityRetrofit.create(CommunityService::class.java)



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

                list.add(DataList(App.prefs.getArrayList1().companies.company2.toString(), ""))
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
            loadCommunity()
        }
    }
    fun refreshAdapter() {
        // 아예 초기화
        adapter1.setTaskList(list)
        adapter1.notifyDataSetChanged()
    }

    private fun loadCommunity(){
        communityServer?.getCommunity()?.enqueue(object : Callback<List<CommunityData>>{
            override fun onResponse(call: Call<List<CommunityData>>, response: Response<List<CommunityData>>) {
                // Log.e("body 출력 : " , body.toString())
                for(i in 0 until 5){
                    val hotList : String? = response.body()?.elementAt(i)?.title
                    val hotCount : Int? = response.body()?.elementAt(i)?.count
                    commList.add(CommunityList(i+1, hotList.toString(), hotCount.toString()))
                }
                rv_data_community.adapter = adapter2
            }

            override fun onFailure(call: Call<List<CommunityData>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}