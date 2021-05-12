package com.example.antenna.adpater

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.antenna.R
import com.example.antenna.interest.InterestActivity
import com.example.antenna.login.LoginActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.list_item.view.*

class RecyclerAdapter(private val items: ArrayList<DataList>):
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_ITEM : Int = 1
    private val TYPE_FOOTER : Int = 2

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

                holder.itemView.apply {
                    xml_image.setImageDrawable(item.imgProfile)
                    xml_name.text = item.strName
                    xml_percent.text = item.strNumber
                }
            }
        }
    }

    // 내부 데이터 전체 값 갱신 함수
    fun setTaskList(list : ArrayList<DataList>){
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
    fun removeDL(position: Int){
        items.removeAt(position - 1)

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
}