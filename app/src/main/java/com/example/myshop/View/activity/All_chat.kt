package com.example.myshop.View.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.FireStore.FireStore
import com.example.myshop.R
import com.example.myshop.adapter.AllChat_adapter
import com.example.myshop.databinding.ActivityAllChatBinding
import com.example.myshop.model.Chat_DataClass
import com.example.myshop.model.Users
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class All_chat :Basic(){
    var userid_reciver:String?=null
    var messagelist:ArrayList<Chat_DataClass>? = null
    var allChatBinding:ActivityAllChatBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        allChatBinding=DataBindingUtil.setContentView(this, R.layout.activity_all_chat)
        getAllmessage()


    }
    fun getAllmessage(){
        ShowDialog(resources.getString(R.string.wait))
        userid_reciver = FireStore().GetCurrentUserID()
        var pathReciver="/User_Message/recived/recived/$userid_reciver"
        var ListenNewMessage= FirebaseDatabase.getInstance().getReference(pathReciver)
        var userS_sender = ArrayList<String>()
        ListenNewMessage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messagelist!!.clear()
                for (snapshot1 in snapshot.children) {
                    var useridSender= snapshot1.getValue(String::class.java)
                    if (useridSender != null) {
                       userS_sender.add(useridSender)
                    }
                }
                FireStore().chat_get_detailUser_sender(this@All_chat,userS_sender)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun successGettingInfo(usersDetail: java.util.ArrayList<Users>) {
        HideDialog()
        allChatBinding?.AllChatRecycler?.apply {
            layoutManager  = LinearLayoutManager(this@All_chat,RecyclerView.VERTICAL,false)
            adapter=AllChat_adapter(usersDetail)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        allChatBinding=null
    }

}