package com.example.antenna.adpater

import android.annotation.SuppressLint
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        holder.itemView.apply {
            listnum.text = item.num.toString()
            listname.text = item.strName
            listcount.text = item.strCount.toString() + "회"
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)
}