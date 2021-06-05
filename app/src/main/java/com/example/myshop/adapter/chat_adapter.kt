package com.letsbuildthatapp.kotlinmessenger.views



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.FireStore.FireStore
import com.example.myshop.databinding.ChatItemBinding
import com.example.myshop.model.Chat_DataClass


class Chat_adapter(var chatDataclass: ArrayList<Chat_DataClass>): RecyclerView.Adapter<Chat_adapter.chatUserviewholder>() {
 inner class chatUserviewholder(var chatUser_view: ChatItemBinding):RecyclerView.ViewHolder(chatUser_view.root){}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):chatUserviewholder {
        var view=ChatItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return chatUserviewholder(view)
    }

    override fun onBindViewHolder(holder: chatUserviewholder, position: Int) {
        holder.chatUser_view.chatdata = chatDataclass[position]
        if (chatDataclass[position].useridbuyer==FireStore().GetCurrentUserID()){
            holder.chatUser_view.textviewFromRecived.visibility=View.GONE
            holder.chatUser_view.textviewFromSender.visibility=View.VISIBLE
        }
        else{
            holder.chatUser_view.textviewFromRecived.visibility=View.VISIBLE
            holder.chatUser_view.textviewFromSender.visibility=View.GONE
        }
    }
    override fun getItemCount(): Int=chatDataclass.size

}


