package com.example.antenna.adpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.antenna.R

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

        }
    }

    override fun getItemCount(): Int = items.size

    class MainHolder(v: View) : RecyclerView.ViewHolder(v)
}