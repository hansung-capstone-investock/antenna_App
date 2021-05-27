package com.example.antenna.adpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.antenna.R
import kotlinx.android.synthetic.main.list_item.view.*


class InterAdapter(private val items: MutableList<InterList>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private fun ViewGroup.inflate(layoutRes : Int) : View = LayoutInflater.from(context).inflate(layoutRes, this, false)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecyclerAdapter.ViewHolder(parent.inflate(R.layout.list_inter))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        holder.itemView.apply {
            inter_name.text = item.str_company
            inter_percent.text = item.str_tst
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)
}