package com.example.antenna.adpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.antenna.R
import kotlinx.android.synthetic.main.list_comm.view.*

class CommuityAdapter(private val items : MutableList<CommunityList>):
        RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private fun ViewGroup.inflate(layoutRes : Int) : View = LayoutInflater.from(context).inflate(layoutRes, this, false)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerView.ViewHolder {
        return ViewHolder(parent.inflate(R.layout.list_comm))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        holder.itemView.apply {
            listname.text = item.strName
            listpercent.text = item.strPercent
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)
}