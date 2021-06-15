package com.example.antenna.adpater

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.antenna.R
import com.example.antenna.interest.InterCompany
import kotlinx.android.synthetic.main.listcompany_item.view.*
import java.util.*
import kotlin.collections.ArrayList


class CompanyAdapter(private var items: ArrayList<CompanyList>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    private fun ViewGroup.inflate(layoutRes : Int) : View = LayoutInflater.from(context).inflate(layoutRes, this, false)

    var companyFilterList = ArrayList<CompanyList>()

    init{
        companyFilterList = items as ArrayList<CompanyList>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return RecyclerAdapter.ViewHolder(parent.inflate(R.layout.listcompany_item))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        val intent = Intent(holder.itemView.context, InterCompany::class.java)
        // 필터 행에 대한 항목 가져오기
        holder.itemView.commpanyName.text = companyFilterList[position].str_company
        holder.itemView.commpanyCode.text = companyFilterList[position].str_tst

        // 종목 클릭시 이동
        holder.itemView.setOnClickListener {
            intent.putExtra("name", item.str_company)
            intent.putExtra("code", item.str_tst)

            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }

        holder.itemView.apply {
            commpanyName.text = item.str_company
            commpanyCode.text = item.str_tst
        }
    }

    override fun getItemCount(): Int {
        return companyFilterList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                Log.e("constraint : ", constraint.toString())
                val charSearch = constraint.toString()
                if(charSearch.isEmpty()){
                    companyFilterList = items as ArrayList<CompanyList>
                    println("not search")
                } else {
                    println(charSearch)
                    val resultList = ArrayList<CompanyList>()
                    for(row in items){
                        if(row.str_tst.contains(charSearch)) {
                            resultList.add(row)
                        }
                    }
                    companyFilterList = resultList
                }
                val filterResult = FilterResults()
                filterResult.values = companyFilterList
                return filterResult
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                companyFilterList = results?.values as ArrayList<CompanyList>
                notifyDataSetChanged()
            }
        }
    }



}
