package com.example.mybird

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RankAdapter(private val userList: List<User>): RecyclerView.Adapter<RankAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val index: TextView = itemView.findViewById(R.id.item_indexTxt)
        val name: TextView = itemView.findViewById(R.id.item_nameAccountTxt)
        val mark: TextView = itemView.findViewById(R.id.item_markTxt)
        val email: TextView = itemView.findViewById(R.id.item_emailTxt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_main, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.index.text = userList[position].index
        holder.name.text = userList[position].name
        holder.mark.text = userList[position].mark
        holder.email.text = userList[position].email
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}