package com.example.myshop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.databinding.AllChatItemBinding
import com.example.myshop.model.Users

class AllChat_adapter(var users:ArrayList<Users>):RecyclerView.Adapter<AllChat_adapter.Allchat_viewHolder>()
{
    inner class Allchat_viewHolder(var itemChatBinding: AllChatItemBinding):RecyclerView.ViewHolder(itemChatBinding.root){}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Allchat_viewHolder {
        var view=AllChatItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Allchat_viewHolder(view)
    }
    override fun onBindViewHolder(holder: Allchat_viewHolder, position: Int) {
        holder.itemChatBinding.user=users[position]
    }
    override fun getItemCount(): Int = users.size
}