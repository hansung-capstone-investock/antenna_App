package com.example.antenna.adpater

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.antenna.R
import com.example.antenna.`interface`.UpdateService
import com.example.antenna.dataclass.UpdateData
import com.example.antenna.interest.InterCompany
import com.example.antenna.interest.InterestActivity
import com.example.antenna.sharedPreference.App
import kotlinx.android.synthetic.main.list_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecyclerAdapter(private val items: MutableList<DataList>):
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_ITEM : Int = 1
    private val TYPE_FOOTER : Int = 2

    val listGruop1 = mutableListOf<String>()

    val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-3-37-87-254.ap-northeast-2.compute.amazonaws.com:8000/") // 장고 서버 주소 입력
            .addConverterFactory(GsonConverterFactory.create()) // Retrofit 객체 생성
            .build()

    private var companyDataRecycler : UpdateData? = null

    private fun ViewGroup.inflate(layoutRes : Int) : View = LayoutInflater.from(context).inflate(layoutRes, this, false)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerView.ViewHolder {
        return when (viewType){
            TYPE_FOOTER -> FooterViewHolder(parent.inflate(R.layout.footer_recy))
            else -> ViewHolder(parent.inflate(R.layout.list_item))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder){
            is FooterViewHolder -> {
                holder.itemView.setOnClickListener {
                    val intent = Intent(holder.itemView.context, InterestActivity::class.java)
                    startActivity(holder.itemView.context, intent, null)
                }
            }
            else -> {
                val item = items[position]
                /*holder.itemView.setOnClickListener {
                    val intentInfo = Intent(holder.itemView.context, InterCompany::class.java)
                    intentInfo.putExtra("name", item.strName)
                    // 코드
                    startActivity(holder.itemView.context, intentInfo, null)
                }*/

                holder.itemView.removebutton.setOnClickListener {

                    removeDL(position)
                    updateData(App.prefs.idGroup1!!.toInt(), App.prefs.id.toString(), App.prefs.group1.toString(), App.prefs.getArrayList1()[0],
                            App.prefs.getArrayList1()[1], App.prefs.getArrayList1()[2], App.prefs.getArrayList1()[3], App.prefs.getArrayList1()[4],
                            App.prefs.getArrayList1()[5], App.prefs.getArrayList1()[6], App.prefs.getArrayList1()[7],
                            App.prefs.getArrayList1()[8], App.prefs.getArrayList1()[9])
                }

                holder.itemView.apply {
                    if(item.strName == "null"){
                        inter_name.text.isEmpty()
                    } else{
                        inter_name.text = item.strName
                    }
                }
            }
        }
    }

    // 내부 데이터 전체 값 갱신 함수
    fun setTaskList(list : MutableList<DataList>){
        items.clear()
        items.addAll(list)

        notifyDataSetChanged()
    }

    // 내부 데이터 추가 함수
    fun addDataList(DL : DataList){
        items.add(DL)
        notifyDataSetChanged()
    }

    // 내부 데이터 값 제거 함수
    private fun removeDL(position: Int){
        Log.d("position : " , position.toString())
        listGruop1.clear()
        for(i in 0 until App.prefs.getArrayList1().count()){
            listGruop1.add(App.prefs.getArrayList1()[i])
            Log.d("getArrayList1 : " , App.prefs.getArrayList1()[i])
        }

        for(i in 0 until App.prefs.getArrayList1().count()){
            Log.d("listGruop1", listGruop1[i])
            Log.d("itemspos", items[position].strName)
            if(listGruop1[i] == items[position].strName){
                listGruop1[i] = "null"
                items.removeAt(position)
                break
            }
        }

        App.prefs.saveArrayList1(listGruop1)
        Log.e("getArrayList1 : ", App.prefs.getArrayList1().toString())
        notifyDataSetChanged()
    }
    
    // 아이템 전체 개수 함수 .. 아이템 + Footer(1) 추가
    override fun getItemCount(): Int = items.size + 1

    // 아이템의 타입 반환하는 함수
    override fun getItemViewType(position: Int): Int {
        return when (position){
            itemCount - 1 -> TYPE_FOOTER
            else -> TYPE_ITEM
        }
    }


    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)
    class FooterViewHolder(item:View) : RecyclerView.ViewHolder(item)

    private fun updateData(id : Int, name : String, group : String, company1 : String, company2 : String, company3 : String, company4 : String, company5 : String,
                           company6 : String, company7 : String, company8 : String, company9 : String, company10 : String,) {
        val companyService : UpdateService = retrofit.create(UpdateService::class.java)

        companyService.updateCompany(id, name, group, company1, company2, company3,company4, company5,company6,company7,
                company8, company9, company10).enqueue(object : Callback<UpdateData>{
            override fun onResponse(call: Call<UpdateData>, response: Response<UpdateData>) {
                val removeData = response.body()

                Log.d("response : " , removeData.toString())
            }

            override fun onFailure(call: Call<UpdateData>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }
}