package com.example.antenna.adpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.antenna.R
import kotlinx.android.synthetic.main.list_main.view.*

class MainNewsAdapter(private val items : MutableList<MainNewsList>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private fun ViewGroup.inflate(layoutRes : Int) : View
    = LayoutInflater.from(context).inflate(layoutRes, this, false)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MainHolder(parent.inflate(R.layout.list_main))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        holder.itemView.apply {
            main_title.text = item.main_title
            main_summary.text = item.main_summary
            main_publishDay.text = item.main_publishDay
        }
    }

    override fun getItemCount(): Int = items.size

    class MainHolder(v: View) : RecyclerView.ViewHolder(v)
}