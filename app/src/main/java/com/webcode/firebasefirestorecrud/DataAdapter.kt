package com.webcode.firebasefirestorecrud

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DataAdapter(private val data:List<Data>, private  val itemClickListener: ItemClickListener): RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    interface ItemClickListener {
        fun onEditItemClick(data: Data)
        fun onDeleteItemClick(data: Data)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id = itemView.findViewById<TextView>(R.id.StudentId)
        val name = itemView.findViewById<TextView>(R.id.StudentName)
        val subject = itemView.findViewById<ImageButton>(R.id.StudentSub)
        val email = itemView.findViewById<ImageButton>(R.id.StudentEmail)
        val birth = itemView.findViewById<ImageButton>(R.id.StudentBirth)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.student_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.id.text = item.title
        holder.name.text = item.description

        holder.edit.setOnClickListener {
            itemClickListener.onEditItemClick(item)
        }
        holder.delete.setOnClickListener {
            itemClickListener.onDeleteItemClick(item)
        }
    }
}
