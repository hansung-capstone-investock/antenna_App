package com.example.antenna.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.annotation.Nullable
import com.example.antenna.BackResult
import com.example.antenna.R
import com.example.antenna.`interface`.BackService
import com.example.antenna.dataclass.*
import com.example.antenna.sharedPreference.App
import kotlinx.android.synthetic.main.fragment_back.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.CoroutineContext

class BackFragment : Fragment(), CoroutineScope {

    private lateinit var job: Job // 2
    override val coroutineContext: CoroutineContext // 3
        get() = Dispatchers.Main + job

    val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-3-37-87-254.ap-northeast-2.compute.amazonaws.com:8000/") // 장고 서버 주소 입력
            .addConverterFactory(GsonConverterFactory.create()) // Retrofit 객체 생성
            .build()
    // 백테스팅 변수 설정
    var start : String? = null
    var end : String? = null
    var market : ArrayList<Int> = arrayListOf()
    var sector : ArrayList<String> = arrayListOf()

    // 실제 넘겨줄 값
    private val changeCode : ArrayList<Int> = arrayListOf()
    //var conditions = mutableListOf<List<String>>()
    var sellCondition = mutableListOf<Int>()

    var maxValue : Int? = null
    var minValue : Int? = null

    private val PER : ArrayList<Int> = arrayListOf()
    private val PBR : ArrayList<Int> = arrayListOf()
    private val PSR : ArrayList<Int> = arrayListOf()
    private val ROE : ArrayList<Int> = arrayListOf()
    private val ROA : ArrayList<Int> = arrayListOf()

    private val backList = ArrayList<Any>()

    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater,
            @Nullable container: ViewGroup?,
            @Nullable savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_back, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        job = Job()
        // 체크박스 구현
        addCheck()

        launch {
            // 백테스팅 시작
            backbutton.setOnClickListener {
                // 값들 가져오기
                start = editStart.text.toString()
                end = editEnd.text.toString()

                sellCondition.clear()
                maxValue = MaxValue.text.toString().toInt()
                minValue = MinValue.text.toString().toInt()
                sellCondition.add(maxValue!!)
                sellCondition.add(minValue!!)

                if(PER.isEmpty()){
                    println("empty")
                    PER.add(-9999)
                    println(PER)
                    println(PER[0])
                }
                if(PBR.isEmpty()){
                    println("empty")
                    PBR.add(-9999)
                    println(PBR)
                    println(PBR[0])
                }
                if(PSR.isEmpty()){
                    println("empty")
                    PSR.add(-9999)
                    println(PSR)
                    println(PSR[0])
                }
                if(ROA.isEmpty()){
                    println("empty")
                    ROA.add(-9999)
                    println(ROA)
                    println(ROA[0])
                }
                if(ROE.isEmpty()){
                    println("empty")
                    ROE.add(-9999)
                    println(ROE)
                    println(ROE[0])
                }

                val intentBack = Intent(context, BackResult::class.java)



                Log.d("APP id :", App.prefs.id.toString())
                Log.d("start :", start.toString())
                Log.d("end :", end.toString())
                Log.d("market :", market.toString())
                Log.d("changeCode :", changeCode.toString())
                Log.d("sellCondition :", sellCondition.toString())
                Log.d("PER :", PER.toString())
                Log.d("PBR :", PBR.toString())
                Log.d("PSR :", PSR.toString())
                Log.d("ROA :", ROA.toString())
                Log.d("ROE :", ROE.toString())

                startBack(App.prefs.id!!, start!!, end!!, market, sellCondition, changeCode, PER, PBR, PSR, ROA, ROE)
                super.onViewCreated(view, savedInstanceState)

                //startActivity(intentBack)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancel()
    }
    // 세터값 변환
    inner class CheckBoxListener : CompoundButton.OnCheckedChangeListener{
        val code : ArrayList<Int> = arrayListOf(1005, 1006, 1007, 1008, 1009, 1010, 1011, 1012, 1013,
                1014, 1015, 1016, 1017, 1018, 1019, 1020, 1021, 1022, 1024, 1025, 1026, 1027,
                2015, 2024, 2026, 2027, 2029, 2031, 2037, 2041, 2042,
                2043, 2056, 2058, 2062, 2063, 2065, 2066, 2067, 2068, 2070, 2072,
                2074, 2075, 2077, 2151, 2152, 2153, 2154, 2155, 2156, 2157, 2158, 2159, 2160)
        override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
            if(p1)
                if (p0 != null) {
                    sector.add(p0.text as String)
                    Log.d("ADD : " , sector.toString())
                    // 코드로 변환

                    // 변환 담아갈 배열
                    for(i in 0 until sector.count()){
                        if(sector[i] == "음식료품"){
                            sector.clear()
                            changeCode.add(code[0])
                            Log.e("변환 완료 : ", changeCode.toString())
                            
                        } else if(sector[i] == "섬유의복") {
                            sector.clear()
                            changeCode.add(code[1])
                            Log.e("변환 완료 : ", changeCode.toString())
                            
                        } else if(sector[i] == "종이목재") {
                            sector.clear()
                            changeCode.add(code[2])
                            Log.e("변환 완료 : ", changeCode.toString())
                            
                        } else if(sector[i] == "화학") {
                            sector.clear()
                            changeCode.add(code[3])
                            Log.e("변환 완료 : ", changeCode.toString())
                            
                        } else if(sector[i] == "의약품") {
                            sector.clear()
                            changeCode.add(code[4])
                            Log.e("변환 완료 : ", changeCode.toString())
                            
                        } else if(sector[i] == "비금속광물") {
                            sector.clear()
                            changeCode.add(code[5])
                            Log.e("변환 완료 : ", changeCode.toString())
                            
                        } else if(sector[i] == "철강금속") {
                            sector.clear()
                            changeCode.add(code[6])
                            Log.e("변환 완료 : ", changeCode.toString())
                            
                        } else if(sector[i] == "기계") {
                            sector.clear()
                            changeCode.add(code[7])
                            Log.e("변환 완료 : ", changeCode.toString())
                            
                        } else if(sector[i] == "전기전자") {
                            sector.clear()
                            changeCode.add(code[8])
                            Log.e("변환 완료 : ", changeCode.toString())

                        } else if(sector[i] == "의료정밀") {
                            sector.clear()
                            changeCode.add(code[9])
                            Log.e("변환 완료 : ", changeCode.toString())

                        } else if(sector[i] == "운수장비") {
                            sector.clear()
                            changeCode.add(code[10])
                            Log.e("변환 완료 : ", changeCode.toString())

                        } else if(sector[i] == "유통업") {
                            sector.clear()
                            changeCode.add(code[11])
                            Log.e("변환 완료 : ", changeCode.toString())
                            
                        } else if(sector[i] == "전기가스업") {
                            sector.clear()
                            changeCode.add(code[12])
                            Log.e("변환 완료 : ", changeCode.toString())
                            
                        } else if(sector[i] == "건설업") {
                            sector.clear()
                            changeCode.add(code[13])
                            Log.e("변환 완료 : ", changeCode.toString())
                            
                        } else if(sector[i] == "운수창고업") {
                            sector.clear()
                            changeCode.add(code[14])
                            Log.e("변환 완료 : ", changeCode.toString())
                            
                        } else if(sector[i] == "통신업") {
                            sector.clear()
                            changeCode.add(code[15])
                            Log.e("변환 완료 : ", changeCode.toString())
                            
                        } else if(sector[i] == "금융업") {
                            sector.clear()
                            changeCode.add(code[16])
                            Log.e("변환 완료 : ", changeCode.toString())
                            
                        } else if(sector[i] == "은행") {
                            sector.clear()
                            changeCode.add(code[17])
                            Log.e("변환 완료 : ", changeCode.toString())
                            
                        } else if(sector[i] == "증권") {
                            sector.clear()
                            changeCode.add(code[18])
                            Log.e("변환 완료 : ", changeCode.toString())
                            
                        } else if(sector[i] == "보험") {
                            sector.clear()
                            changeCode.add(code[19])
                            Log.e("변환 완료 : ", changeCode.toString())

                        } else if(sector[i] == "서비스업") {
                            sector.clear()
                            changeCode.add(code[20])
                            Log.e("변환 완료 : ", changeCode.toString())

                        } else if(sector[i] == "제조업") {
                            sector.clear()
                            changeCode.add(code[21])
                            Log.e("변환 완료 : ", changeCode.toString())
                            // --------------------------------------------------//

                        } else if(sector[i] == "코스닥 IT") {
                            sector.clear()
                            changeCode.add(code[22])
                            Log.e("변환 완료 : ", changeCode.toString())

                        } else if(sector[i] == "제조업") {
                            sector.clear()
                            changeCode.add(code[23])
                            Log.e("변환 완료 : ", changeCode.toString())
                            
                        } else if(sector[i] == "건설") {
                            sector.clear()
                            changeCode.add(code[24])
                            Log.e("변환 완료 : ", changeCode.toString())
                            
                        } else if(sector[i] == "유통") {
                            sector.clear()
                            changeCode.add(code[25])
                            Log.e("변환 완료 : ", changeCode.toString())
                            
                        } else if(sector[i] == "운송") {
                            sector.clear()
                            changeCode.add(code[26])
                            Log.e("변환 완료 : ", changeCode.toString())

                        } else if(sector[i] == "금융") {
                            sector.clear()
                            changeCode.add(code[27])
                            Log.e("변환 완료 : ", changeCode.toString())
                            
                        } else if(sector[i] == "오락·문화") {
                            sector.clear()
                            changeCode.add(code[28])
                            Log.e("변환 완료 : ", changeCode.toString())
                        }

                        else if(sector[i] == "통신방송서비스") {
                            sector.clear()
                            changeCode.add(code[29])
                            Log.e("변환 완료 : ", changeCode.toString())
                        }

                        else if(sector[i] == "IT S/W & SVC") {
                            sector.clear()
                            changeCode.add(code[30])
                            Log.e("변환 완료 : ", changeCode.toString())
                        }

                        else if(sector[i] == "IT H/W") {
                            sector.clear()
                            changeCode.add(code[31])
                            Log.e("변환 완료 : ", changeCode.toString())
                        }

                        else if(sector[i] == "음식료·담배") {
                            sector.clear()
                            changeCode.add(code[32])
                            Log.e("변환 완료 : ", changeCode.toString())
                        }

                        else if(sector[i] == "섬유·의류") {
                            sector.clear()
                            changeCode.add(code[33])
                            Log.e("변환 완료 : ", changeCode.toString())
                        }

                        else if(sector[i] == "종이·목재") {
                            sector.clear()
                            changeCode.add(code[34])
                            Log.e("변환 완료 : ", changeCode.toString())
                        }

                        else if(sector[i] == "출판·매체복제") {
                            sector.clear()
                            changeCode.add(code[35])
                            Log.e("변환 완료 : ", changeCode.toString())
                        }

                        else if(sector[i] == "화학") {
                            sector.clear()
                            changeCode.add(code[36])
                            Log.e("변환 완료 : ", changeCode.toString())
                        }

                        else if(sector[i] == "제약") {
                            sector.clear()
                            changeCode.add(code[37])
                            Log.e("변환 완료 : ", changeCode.toString())
                        }

                        else if(sector[i] == "비금속") {
                            sector.clear()
                            changeCode.add(code[38])
                            Log.e("변환 완료 : ", changeCode.toString())
                        }

                        else if(sector[i] == "금속") {
                            sector.clear()
                            changeCode.add(code[39])
                            Log.e("변환 완료 : ", changeCode.toString())
                        }

                        else if(sector[i] == "기계·장비") {
                            sector.clear()
                            changeCode.add(code[40])
                            Log.e("변환 완료 : ", changeCode.toString())
                        }
                        else if(sector[i] == "일반전기전자") {
                            sector.clear()
                            changeCode.add(code[41])
                            Log.e("변환 완료 : ", changeCode.toString())
                        }

                        else if(sector[i] == "의료·정밀기기") {
                            sector.clear()
                            changeCode.add(code[42])
                            Log.e("변환 완료 : ", changeCode.toString())
                        }

                        else if(sector[i] == "운송장비·부품") {
                            sector.clear()
                            changeCode.add(code[43])
                            Log.e("변환 완료 : ", changeCode.toString())
                        }

                        else if(sector[i] == "기타제조") {
                            sector.clear()
                            changeCode.add(code[44])
                            Log.e("변환 완료 : ", changeCode.toString())
                        }
                        else if(sector[i] == "통신서비스") {
                            sector.clear()
                            changeCode.add(code[45])
                            Log.e("변환 완료 : ", changeCode.toString())
                        }

                        else if(sector[i] == "방송서비스") {
                            sector.clear()
                            changeCode.add(code[46])
                            Log.e("변환 완료 : ", changeCode.toString())
                        }

                        else if(sector[i] == "인터넷") {
                            sector.clear()
                            changeCode.add(code[47])
                            Log.e("변환 완료 : ", changeCode.toString())
                        }

                        else if(sector[i] == "디지털컨텐츠") {
                            sector.clear()
                            changeCode.add(code[48])
                            Log.e("변환 완료 : ", changeCode.toString())
                        }

                        else if(sector[i] == "소프트웨어") {
                            sector.clear()
                            changeCode.add(code[49])
                            Log.e("변환 완료 : ", changeCode.toString())
                        }

                        else if(sector[i] == "컴퓨터서비스") {
                            sector.clear()
                            changeCode.add(code[50])
                            Log.e("변환 완료 : ", changeCode.toString())
                        }

                        else if(sector[i] == "통신장비") {
                            sector.clear()
                            changeCode.add(code[51])
                            Log.e("변환 완료 : ", changeCode.toString())
                        }
                        else if(sector[i] == "정보기기") {
                            sector.clear()
                            changeCode.add(code[52])
                            Log.e("변환 완료 : ", changeCode.toString())
                        }
                        else if(sector[i] == "반도체") {
                            sector.clear()
                            changeCode.add(code[53])
                            Log.e("변환 완료 : ", changeCode.toString())
                        }
                        else if(sector[i] == "IT부품") {
                            sector.clear()
                            changeCode.add(code[54])
                            Log.e("변환 완료 : ", changeCode.toString())
                        }


                        else {
                            println("실패")
                        }
                    }

                } else{
                    println("LIST $sector")
                }
            else {
                Log.d("SIZE : " , sector.count().toString())
                // 변환 담아갈 배열
                for(i in 0 until changeCode.count()){
                    if(p0?.text as String == "음식료품"){
                        changeCode.remove(code[0])
                        Log.e("삭제 완료 : ", changeCode.toString())

                        sector.remove(p0.text as String)

                    } else if(p0.text as String == "섬유의복") {
                        changeCode.remove(code[1])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "종이목재") {
                        changeCode.remove(code[2])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    
                    else if(p0.text as String == "화학") {
                        changeCode.remove(code[3])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    
                    else if(p0.text as String == "의약품") {
                        changeCode.remove(code[4])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    
                    else if(p0.text as String == "비금속광물") {
                        changeCode.remove(code[5])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    
                    else if(p0.text as String == "철강금속") {
                        changeCode.remove(code[6])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    
                    else if(p0.text as String == "기계") {
                        changeCode.remove(code[7])
                        Log.e("삭제 완료 : ", changeCode.toString())
                        
                    }else if(p0.text as String == "전기전자") {
                        changeCode.remove(code[8])
                        Log.e("삭제 완료 : ", changeCode.toString())
                        
                    }
                    else if(p0.text as String == "의료정밀") {
                        changeCode.remove(code[9])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }

                    else if(p0.text as String == "운수장비") {
                        changeCode.remove(code[10])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "유통업") {
                        changeCode.remove(code[11])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    
                    else if(p0.text as String == "전기가스업") {
                        changeCode.remove(code[12])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "건설업") {
                        changeCode.remove(code[13])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "운수창고업") {
                        changeCode.remove(code[14])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }

                    else if(p0.text as String == "통신업") {
                        changeCode.remove(code[15])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "금융업") {
                        changeCode.remove(code[16])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "은행") {
                        changeCode.remove(code[17])
                        Log.e("삭제 완료 : ", changeCode.toString())

                    }else if(p0.text as String == "증권") {
                        changeCode.remove(code[18])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "보험") {
                        changeCode.remove(code[19])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "서비스업") {
                        changeCode.remove(code[20])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "제조업") {
                        changeCode.remove(code[21])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    // -----------------------------------------------------------//
                    else if(p0.text as String == "코스닥 IT") {
                        changeCode.remove(code[22])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "제조업") {
                        changeCode.remove(code[23])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "건설") {
                        changeCode.remove(code[24])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "유통") {
                        changeCode.remove(code[25])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "운송") {
                        changeCode.remove(code[26])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "금융") {
                        changeCode.remove(code[27])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }

                    else if(p0.text as String == "오락·문화") {
                        changeCode.remove(code[28])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "통신방송서비스") {
                        changeCode.remove(code[29])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }

                    else if(p0.text as String == "IT S/W & SVC") {
                        changeCode.remove(code[30])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "IT H/W") {
                        changeCode.remove(code[31])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }

                    else if(p0.text as String == "음식료·담배") {
                        changeCode.remove(code[32])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }else if(p0.text as String == "섬유·의류") {
                        changeCode.remove(code[33])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "종이·목재") {
                        changeCode.remove(code[34])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "출판·매체복제") {
                        changeCode.remove(code[35])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "화학") {
                        changeCode.remove(code[36])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "제약") {
                        changeCode.remove(code[37])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "비금속") {
                        changeCode.remove(code[38])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "금속") {
                        changeCode.remove(code[39])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }else if(p0.text as String == "기계·장비") {
                        changeCode.remove(code[40])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }

                    else if(p0.text as String == "일반전기전자") {
                        changeCode.remove(code[41])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "의료·정밀기기") {
                        changeCode.remove(code[42])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "운송장비·부품") {
                        changeCode.remove(code[43])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "기타제조") {
                        changeCode.remove(code[44])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }else if(p0.text as String == "통신서비스") {
                        changeCode.remove(code[45])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "방송서비스") {
                        changeCode.remove(code[46])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "인터넷") {
                        changeCode.remove(code[47])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }else if(p0.text as String == "디지털컨텐츠") {
                        changeCode.remove(code[48])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "소프트웨어") {
                        changeCode.remove(code[49])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "컴퓨터서비스") {
                        changeCode.remove(code[50])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "통신장비") {
                        changeCode.remove(code[51])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "정보기기") {
                        changeCode.remove(code[52])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "반도체") {
                        changeCode.remove(code[53])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else if(p0.text as String == "IT부품") {
                        changeCode.remove(code[54])
                        Log.e("삭제 완료 : ", changeCode.toString())
                    }
                    else {
                        println("실패")
                    }
                }


            }
        }
    }
    // 코스피 코스닥 타입 변환
    inner class CheckBoxListener1 : CompoundButton.OnCheckedChangeListener{
        val code : ArrayList<Int> = arrayListOf(1, 2)
        override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
            if(p1)
                if (p0 != null) {
                    if(p0.text as String == "코스피"){
                        market.add(code[0])
                        Log.d("market : " , market.toString())
                    } else if(p0.text as String == "코스닥"){
                        market.add(code[1])
                        Log.d("market : " , market.toString())
                    }
                } else{
                    println("LIST $market")
                }
            else {
                if(p0?.text as String == "코스피"){
                    market.remove(code[0])
                    Log.d("remove : " , market.toString())
                } else if(p0?.text as String == "코스닥"){
                    market.remove(code[1])
                    Log.d("remove : " , market.toString())
                }
            }
        }
    }
    // 매수 우선순위 변경
    inner class CheckBoxListener2 : CompoundButton.OnCheckedChangeListener{

        override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
            if(p1)
                if (p0 != null) {
                    if(p0.text as String == "PER"){
                        PER.add(PERMin.text.toString().toInt())
                        PER.add(PERMax.text.toString().toInt())
                        PER.add(PERedit.text.toString().toInt())
                        Log.d("PER : " , PER.toString())
                    } else if(p0.text as String == "PBR"){
                        PBR.add(PBRMin.text.toString().toInt())
                        PBR.add(PBRMax.text.toString().toInt())
                        PBR.add(PBRedit.text.toString().toInt())
                        Log.d("PBR : " , PBR.toString())
                    } else if(p0.text as String == "PSR"){
                        PSR.add(PSRMin.text.toString().toInt())
                        PSR.add(PSRMax.text.toString().toInt())
                        PSR.add(PSRedit.text.toString().toInt())
                        Log.d("PSR : " , PSR.toString())
                    } else if(p0.text as String == "ROE(단위:%)"){
                        ROE.add(ROEMin.text.toString().toInt())
                        ROE.add(ROEMax.text.toString().toInt())
                        ROE.add(ROEedit.text.toString().toInt())
                        Log.d("ROE : " , ROE.toString())
                    } else if(p0.text as String == "ROA(단위:%)"){
                        ROA.add(ROAMin.text.toString().toInt())
                        ROA.add(ROAMax.text.toString().toInt())
                        ROA.add(ROAedit.text.toString().toInt())
                        Log.d("ROA : " , ROA.toString())
                    }
                } else{
                    println("LIST $market")
                }
            else {
                if(p0?.text as String == "PER"){
                    PER.clear()
                    Log.d("PER : " , PER.toString())
                    println("PER CLEAR")
                } else if(p0.text as String == "PBR"){
                    PBR.clear()
                    Log.d("PBR : " , PBR.toString())
                    println("PBR CLEAR")
                } else if(p0.text as String == "PSR"){
                    PSR.clear()
                    Log.d("PSR : " , PSR.toString())
                    println("PSR CLEAR")
                } else if(p0.text as String == "ROE(단위:%)"){
                    ROE.clear()
                    Log.d("ROE : " , ROE.toString())
                    println("ROE CLEAR")
                } else if(p0.text as String == "ROA(단위:%)"){
                    ROA.clear()
                    Log.d("ROA : " , ROA.toString())
                    println("ROA CLEAR")
                }
            }
        }
    }

    // 체크 박스
    private fun addCheck(){
        val subListener = CheckBoxListener()
        val market = CheckBoxListener1()
        val condition = CheckBoxListener2()

        // Kospi 체크박스 연결
        btn1005.setOnCheckedChangeListener(subListener)
        btn1006.setOnCheckedChangeListener(subListener)
        btn1007.setOnCheckedChangeListener(subListener)
        btn1008.setOnCheckedChangeListener(subListener)
        btn1009.setOnCheckedChangeListener(subListener)
        btn1010.setOnCheckedChangeListener(subListener)
        btn1011.setOnCheckedChangeListener(subListener)
        btn1012.setOnCheckedChangeListener(subListener)
        btn1013.setOnCheckedChangeListener(subListener)
        btn1014.setOnCheckedChangeListener(subListener)
        btn1015.setOnCheckedChangeListener(subListener)
        btn1016.setOnCheckedChangeListener(subListener)
        btn1017.setOnCheckedChangeListener(subListener)
        btn1018.setOnCheckedChangeListener(subListener)
        btn1019.setOnCheckedChangeListener(subListener)
        btn1020.setOnCheckedChangeListener(subListener)
        btn1021.setOnCheckedChangeListener(subListener)
        btn1022.setOnCheckedChangeListener(subListener)
        btn1024.setOnCheckedChangeListener(subListener)
        btn1025.setOnCheckedChangeListener(subListener)
        btn1026.setOnCheckedChangeListener(subListener)
        btn1027.setOnCheckedChangeListener(subListener)
        // Kosdaq 연결
        btn2015.setOnCheckedChangeListener(subListener)
        btn2024.setOnCheckedChangeListener(subListener)
        btn2026.setOnCheckedChangeListener(subListener)
        btn2027.setOnCheckedChangeListener(subListener)
        btn2029.setOnCheckedChangeListener(subListener)
        btn2031.setOnCheckedChangeListener(subListener)
        btn2037.setOnCheckedChangeListener(subListener)
        btn2041.setOnCheckedChangeListener(subListener)
        btn2042.setOnCheckedChangeListener(subListener)
        btn2043.setOnCheckedChangeListener(subListener)
        btn2056.setOnCheckedChangeListener(subListener)
        btn2058.setOnCheckedChangeListener(subListener)
        btn2062.setOnCheckedChangeListener(subListener)
        btn2063.setOnCheckedChangeListener(subListener)
        btn2065.setOnCheckedChangeListener(subListener)
        btn2067.setOnCheckedChangeListener(subListener)
        btn2068.setOnCheckedChangeListener(subListener)
        btn2070.setOnCheckedChangeListener(subListener)
        btn2072.setOnCheckedChangeListener(subListener)
        btn2074.setOnCheckedChangeListener(subListener)
        btn2075.setOnCheckedChangeListener(subListener)
        btn2077.setOnCheckedChangeListener(subListener)
        btn2151.setOnCheckedChangeListener(subListener)
        btn2152.setOnCheckedChangeListener(subListener)
        btn2153.setOnCheckedChangeListener(subListener)
        btn2154.setOnCheckedChangeListener(subListener)
        btn2155.setOnCheckedChangeListener(subListener)
        btn2156.setOnCheckedChangeListener(subListener)
        btn2157.setOnCheckedChangeListener(subListener)
        btn2158.setOnCheckedChangeListener(subListener)
        btn2159.setOnCheckedChangeListener(subListener)
        btn2160.setOnCheckedChangeListener(subListener)

        Kospicheck.setOnCheckedChangeListener(market)
        Kosdaqcheck.setOnCheckedChangeListener(market)

        PERcheckBox.setOnCheckedChangeListener(condition)
        PBRcheckBox.setOnCheckedChangeListener(condition)
        PSRcheckBox.setOnCheckedChangeListener(condition)
        ROEcheckBox.setOnCheckedChangeListener(condition)
        ROAcheckBox.setOnCheckedChangeListener(condition)
    }

    private fun startBack(id : String, start: String, end: String, market: ArrayList<Int>,  sellCondition: MutableList<Int>, sector: ArrayList<Int>, perconditions: ArrayList<Int>, psrconditions: ArrayList<Int>,
            pbrconditions: ArrayList<Int>, roeconditions: ArrayList<Int>, roaconditions: ArrayList<Int>){
        val backService : BackService = retrofit.create(BackService::class.java)
        val backData : BackData? = null

        Log.d("ID: " , App.prefs.id.toString())

        backService.requestBack(BackDataX(id, start, end, market, sellCondition, sector, perconditions, psrconditions, pbrconditions, roaconditions, roeconditions)).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val data1 = response.body()!!.string()
                /*val data2 = data1.toString().split(",").associate {
                    val (key,value) = it.split(":")
                    key to value.rangeTo()

                }*/
                val data = response.body()
                if (response.isSuccessful) {
                    Log.d("success :", data.toString())
                    Log.d("data1", data1.toString())
                    print(data1)
                   // Log.d("data2", data2.toString())

                    /*for(i in 0 until data.count()){
                        Log.e("antennaData : ", data[i].toString())
                    }*/
                    /*backList.add(data!!.x20210302)
                    backList.add(data.x20210303)
                    backList.add(data.x20210304)
                    backList.add(data.x20210305)
                    backList.add(data.x20210308)
                    backList.add(data.x20210309)
                    backList.add(data.x20210310)
                    backList.add(data.x20210311)
                    backList.add(data.x20210312)
                    backList.add(data.x20210315)
                    backList.add(data.x20210316)
                    backList.add(data.x20210317)
                    backList.add(data.x20210318)
                    backList.add(data.x20210309)
                    backList.add(data.x20210322)
                    backList.add(data.x20210323)
                    backList.add(data.x20210324)
                    backList.add(data.x20210325)
                    backList.add(data.x20210326)
                    backList.add(data.x20210329)
                    backList.add(data.x20210330)
                    backList.add(data.x20210331)
                    backList.add(data.x20210401)
                    backList.add(data.x20210402)

                    App.prefs.backSV(backList)

                    Log.d("BackList", App.prefs.backGET().toString())*/

                } else {
                    Log.e("antennaData : ", response.errorBody().toString())
                    Log.e("antennaData : ", response.code().toString())
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}