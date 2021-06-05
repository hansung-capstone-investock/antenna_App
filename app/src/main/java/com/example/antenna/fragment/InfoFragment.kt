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

    val list : List<String>? = null

    val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-13-125-236-101.ap-northeast-2.compute.amazonaws.com:8000/") // 장고 서버 주소 입력
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
                Log.d("companyData count", companyData?.count().toString())

                if(companyData != null && response.isSuccessful){
                    App.prefs.saveArrayList1(companyData!![0])
                    App.prefs.saveArrayList2(companyData!![1])
                    App.prefs.saveArrayList3(companyData!![2])
                    Log.d("saveArrayList1", App.prefs.getArrayList1().toString())
                    Log.d("saveArrayList2", App.prefs.getArrayList2().toString())
                    Log.d("saveArrayList3", App.prefs.getArrayList3().toString())
                } else{
                    Log.e("ERROR MESSAGE", response.errorBody().toString())
                }
            }
            override fun onFailure(call: Call<LoadData>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }
}