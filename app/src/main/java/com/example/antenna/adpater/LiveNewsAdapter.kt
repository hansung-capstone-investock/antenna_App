package com.example.antenna.adpater

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.antenna.R
import kotlinx.android.synthetic.main.list_live.view.*

class LiveNewsAdapter(private val items : MutableList<LiveNewsList>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private fun ViewGroup.inflate_live(layoutRes : Int) : View
            = LayoutInflater.from(context).inflate(layoutRes, this, false)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LiveHolder(parent.inflate_live(R.layout.list_live))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item_live = items[position]

        holder.itemView.setOnClickListener {
            val i_live = Intent(Intent.ACTION_VIEW)
            i_live.data = Uri.parse(item_live.live_link)
            ContextCompat.startActivity(holder.itemView.context, i_live, null)
        }

        holder.itemView.apply {
            live_title.text = item_live.live_title
            live_summary.text = item_live.live_summary
            live_publishDay.text = item_live.live_publishDay
        }
    }

    override fun getItemCount(): Int = items.size

    class LiveHolder(v: View) : RecyclerView.ViewHolder(v)
}