package com.example.antenna.adpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.antenna.R
import kotlinx.android.synthetic.main.list_item.view.*

class RecyclerAdapter(private val items:ArrayList<DataList>):
        RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)

        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val listener = View.OnClickListener {
            it-> Toast.makeText(it.context, "Clicked"+item.strName, Toast.LENGTH_SHORT).show()
        }
        holder.apply {
            bind(listener, item)
            itemView.tag = item
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(v:View) : RecyclerView.ViewHolder(v){
        private var view : View = v

        fun bind(listener: View.OnClickListener, item: DataList){
            view.xml_image.setImageDrawable(item.imgProfile)
            view.xml_name.text = item.strName
            view.xml_percent.text = item.strNumber
            view.setOnClickListener(listener)
        }
    }

}