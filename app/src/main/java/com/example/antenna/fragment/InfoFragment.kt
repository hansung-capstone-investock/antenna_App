package com.example.antenna.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.antenna.MainActivity
import com.example.antenna.R
import com.example.antenna.`interface`.LoadService
import com.example.antenna.dataclass.LoadData
import com.example.antenna.sharedPreference.App
import com.example.antenna.dataclass.LoginData
import com.example.antenna.sign.LoginService
import com.example.antenna.sign.SignActivity
import kotlinx.android.synthetic.main.fragment_info.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InfoFragment : Fragment() {
    var loginData: LoginData? = null
    var companyData : LoadData? = null

    // 관심종목 콜렉션
    var groupName1 : String? = null
    val list1 = mutableListOf<String>()

    var groupName2 : String? = null
    val list2 = mutableListOf<String>()

    var groupName3 : String? = null
    val list3 = mutableListOf<String>()

    val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-3-37-87-254.ap-northeast-2.compute.amazonaws.com:8000/") // 장고 서버 주소 입력
            .addConverterFactory(GsonConverterFactory.create()) // Retrofit 객체 생성
            .build()
    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater,
            @Nullable container: ViewGroup?,
            @Nullable savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginService : LoginService = retrofit.create(LoginService::class.java)

        Login_button.setOnClickListener {
            activity?.let{
                val intentLogin  = Intent(context, MainActivity::class.java)
                Log.d(loginService.toString(), "loginService button!!!!!!!!!!!!!!!!!")

                val id = userid.text.toString()
                val pw = userpw.text.toString()

                val dialog = context?.let { it1 -> AlertDialog.Builder(it1) }

                loginService.requestLogin(id, pw).enqueue(object : Callback<LoginData> {

                    // 웹 통신 실패했을 떄 실행
                    override fun onFailure(call: Call<LoginData>, t: Throwable) {

                        t.message?.let { it1 -> Log.e("LOGIN", it1) }
                        Log.d(loginService.toString(), "FA!!!!!!!!!!!!!!!!!")
                        dialog?.setTitle("실패!")
                        dialog?.setMessage("통신에 실패하였습니다")
                        dialog?.show()
                    }

                    // 웹 통신 성공시 실행
                    override fun onResponse(call: Call<LoginData>, response: Response<LoginData>) {
                        loginData = response.body() // code, msg 값을 객체로 넣음
                        // output 정의를 가져옴
                        Log.d(response.toString(), "..ERROR")
                        Log.e("RES", response.message())
                        Log.d("RESPONSE MEG", loginData?.code.toString())

                        if (loginData?.code.toString() == "0000") {
                            Log.d("INFO ID", id)
                            returnCompany(id)
                            App.prefs.id = id

                            userid.text.clear()
                            userpw.text.clear()

                            startActivity(intentLogin)

                        } else { // login.code == 1001
                            dialog?.setTitle("실패")
                            dialog?.setMessage("입력된 정보가 일치하지 않습니다")
                            dialog?.show()
                        }
                        // 로그인 성공했을 경우 원래 액티비티로 돌아가기
                    }
                })
            }
        }

        Sign_button.setOnClickListener {
            activity?.let{
                val intent = Intent(context, SignActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun returnCompany(name : String) {
        val company : LoadService = retrofit.create(LoadService::class.java)
        company.requestCompany(name).enqueue(object : Callback<LoadData>{
            @RequiresApi(Build.VERSION_CODES.P)
            override fun onResponse(call: Call<LoadData>, response: Response<LoadData>) {

                companyData = response.body()

                var id1 : Int? = null
                var id2 : Int? = null
                var id3 : Int? = null
                var groupName : String? = null
                var group1 : String? = null
                var group2 : String? = null
                var group3 : String? = null
                var group4 : String? = null
                var group5 : String? = null
                var group6 : String? = null
                var group7 : String? = null
                var group8 : String? = null
                var group9 : String? = null
                var group10 : String? = null

                id1 = companyData!![0].id
                groupName = companyData!![0].group.toString()
                group1 = companyData!![0].companies.company1.toString()
                group2 = companyData!![0].companies.company2.toString()
                group3 = companyData!![0].companies.company3.toString()
                group4 = companyData!![0].companies.company4.toString()
                group5 = companyData!![0].companies.company5.toString()
                group6 = companyData!![0].companies.company6.toString()
                group7 = companyData!![0].companies.company7.toString()
                group8 = companyData!![0].companies.company8.toString()
                group9 = companyData!![0].companies.company9.toString()
                group10 = companyData!![0].companies.company10.toString()

                groupName1 = groupName
                list1.add(group1)
                list1.add(group2)
                list1.add(group3)
                list1.add(group4)
                list1.add(group5)
                list1.add(group6)
                list1.add(group7)
                list1.add(group8)
                list1.add(group9)
                list1.add(group10)

                id2 = companyData!![1].id
                groupName = companyData!![1].group.toString()
                group1 = companyData!![1].companies.company1.toString()
                group2 = companyData!![1].companies.company2.toString()
                group3 = companyData!![1].companies.company3.toString()
                group4 = companyData!![1].companies.company4.toString()
                group5 = companyData!![1].companies.company5.toString()
                group6 = companyData!![1].companies.company6.toString()
                group7 = companyData!![1].companies.company7.toString()
                group8 = companyData!![1].companies.company8.toString()
                group9 = companyData!![1].companies.company9.toString()
                group10 = companyData!![1].companies.company10.toString()

                id3= companyData!![2].id
                groupName2 = groupName
                list2.add(group1)
                list2.add(group2)
                list2.add(group3)
                list2.add(group4)
                list2.add(group5)
                list2.add(group6)
                list2.add(group7)
                list2.add(group8)
                list2.add(group9)
                list2.add(group10)

                groupName = companyData!![2].group.toString()
                group1 = companyData!![2].companies.company1.toString()
                group2 = companyData!![2].companies.company2.toString()
                group3 = companyData!![2].companies.company3.toString()
                group4 = companyData!![2].companies.company4.toString()
                group5 = companyData!![2].companies.company5.toString()
                group6 = companyData!![2].companies.company6.toString()
                group7 = companyData!![2].companies.company7.toString()
                group8 = companyData!![2].companies.company8.toString()
                group9 = companyData!![2].companies.company9.toString()
                group10 = companyData!![2].companies.company10.toString()

                groupName3 = groupName
                list3.add(group1)
                list3.add(group2)
                list3.add(group3)
                list3.add(group4)
                list3.add(group5)
                list3.add(group6)
                list3.add(group7)
                list3.add(group8)
                list3.add(group9)
                list3.add(group10)

                // 저장
                if(response.isSuccessful) {
                    App.prefs.saveArrayList1(list1)
                    App.prefs.saveArrayList2(list2)
                    App.prefs.saveArrayList3(list3)

                    App.prefs.group1 = groupName1
                    App.prefs.group2 = groupName2
                    App.prefs.group3 = groupName3

                    App.prefs.idGroup1 = id1.toString()
                    App.prefs.idGroup2 = id2.toString()
                    App.prefs.idGroup3 = id3.toString()

                    Log.d("saveArrayList1 : ", App.prefs.getArrayList1().toString())
                    Log.d("saveArrayList2 : ", App.prefs.getArrayList2().toString())
                    Log.d("saveArrayList3 : ", App.prefs.getArrayList3().toString())
                }
            }
            override fun onFailure(call: Call<LoadData>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }
}