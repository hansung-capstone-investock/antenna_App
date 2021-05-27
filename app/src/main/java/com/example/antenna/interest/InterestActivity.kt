package com.example.antenna.interest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.antenna.R
import com.example.antenna.adpater.DataList
import com.example.antenna.adpater.InterAdapter
import com.example.antenna.adpater.InterList
import com.example.antenna.adpater.RecyclerAdapter
import kotlinx.android.synthetic.main.add_company.*

class InterestActivity : AppCompatActivity(){

    private val list = mutableListOf<InterList>()
    private val adapter1 = InterAdapter(list)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_company)

        // searchView 검색 및 변경 처리
        // search_searchView.isSubmitButtonEnabled = true
        search_searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                // 검색 버튼을 누르면 호출 되는 함수

                Log.d("Change SEARCH TEXT : ", query.toString())

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                Log.d("onQueryTextChange : ", newText.toString())

                // 검색창에서 글자가 변경이 일어날 떄마다 호출되는 함수
                
                return true
            }
        })

        list.add(InterList("DRB동일", "004840"))
        list.add(InterList("DSR", "155660"))
        list.add(InterList("GS글로벌", "001250"))
        list.add(InterList("HDC현대산업개발", "294870"))
        list.add(InterList("KEC", "092220"))
        list.add(InterList("KG동부제철", "016380"))
        list.add(InterList("KG케미칼", "001390"))
        list.add(InterList("KTis", "058860"))

        rv_data.adapter = adapter1

        // Retrofit 연결을 통해 초기값 가져오기
        /*val firstRetrofit: Retrofit = Retrofit.Builder()
                .baseUrl("")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                var regionServer : FirstService? = firstRetrofit.create(FirstService::class.java)

        regionServer?.getInfo("json", "")?.enqueue(object : Callback<FirstData>{
            override fun onResponse(call: Call<FirstData>, response: Response<FirstData>) {
                var name = response.body()?.body?.name
                var address = response.body()?.body?.address
                var phoneNum = response.body()?.body?.phone_number

            }

            override fun onFailure(call: Call<FirstData>, t: Throwable) {
                t.printStackTrace()
            }

        })*/
    }

    /*override fun onBackPressed() {
        if (!search_searchView.isIconified) {
            search_searchView.isIconified = true
        } else {
            super.onBackPressed()
        }
    }*/
}