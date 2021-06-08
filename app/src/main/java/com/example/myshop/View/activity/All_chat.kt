package com.example.myshop.View.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.FireStore.FireStore
import com.example.myshop.R
import com.example.myshop.adapter.AllChat_adapter
import com.example.myshop.databinding.ActivityAllChatBinding
import com.example.myshop.model.Users
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class All_chat : Basic() {
    var userid_reciver: String? = null
    var allChatBinding: ActivityAllChatBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        allChatBinding = DataBindingUtil.setContentView(this, R.layout.activity_all_chat)
        ToolbarSetup()
        getAllmessage()
        Swip_Setup()
        getAllmessageSend()
    }

    fun ToolbarSetup(){
        setSupportActionBar(allChatBinding?.AllchatToolabr)
        val actionbar_history = supportActionBar
        actionbar_history?.setDisplayHomeAsUpEnabled(true)
        allChatBinding?.AllchatToolabr?.setTitle(resources.getString(R.string.Messages))
        allChatBinding?.AllchatToolabr?.setNavigationIcon(R.drawable.ic_back)
        allChatBinding?.AllchatToolabr?.setNavigationOnClickListener {
            onBackPressed()
            finish()
        }
    }
    fun Swip_Setup(){
        allChatBinding?.AllchatSwip?.setOnRefreshListener {
            getAllmessage()
            getAllmessageSend()
        }
    }
    fun getAllmessage() {
        allChatBinding?.AllchatSpinKit?.visibility=View.VISIBLE
        userid_reciver = FireStore().GetCurrentUserID()
        var pathReciver = "/User_Message/recived/$userid_reciver"
        var ListenNewMessage = FirebaseDatabase.getInstance().getReference(pathReciver)
        var UserS_sender_id = ArrayList<String>()
        ListenNewMessage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
               UserS_sender_id.clear()
                if (!snapshot.equals(null)) {
                    for (snapshot1 in snapshot.children) {
                        var UseridSender = snapshot1.key
                        if (UseridSender != null) {
                            UserS_sender_id.add(UseridSender)
                            Log.e("e",UseridSender)
                        }
                    }
                }
                if (UserS_sender_id.size==0){
                    allChatBinding?.AllchatSpinKit?.visibility=View.GONE
                    allChatBinding?.allchatNoMessage?.visibility = View.VISIBLE
                }
                if (UserS_sender_id.size >0 ){
                    allChatBinding?.allchatNoMessage?.visibility = View.GONE
                    FireStore().chat_get_detailUser_sender(this@All_chat,UserS_sender_id)
                }
            }


            override fun onCancelled(error: DatabaseError) {
            }
        })

    }
    fun getAllmessageSend() {
        userid_reciver = FireStore().GetCurrentUserID()
        allChatBinding?.allchatAnimNodata?.visibility=View.VISIBLE

        var pathReciver = "/User_Message/send/$userid_reciver"
        var ListenNewMessage = FirebaseDatabase.getInstance().getReference(pathReciver)
        var UserS_sender_id = ArrayList<String>()
        ListenNewMessage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                UserS_sender_id.clear()
                if (!snapshot.equals(null)) {
                    for (snapshot1 in snapshot.children) {
                        var UseridSender = snapshot1.key
                        if (UseridSender != null) {
                            UserS_sender_id.add(UseridSender)

                        }
                    }
                }
                if (UserS_sender_id==null){


                }
                if (UserS_sender_id.size==0){
                    allChatBinding?.AllchatSpinKit?.visibility=View.GONE
                    allChatBinding?.allchatAnimNodata?.visibility=View.VISIBLE
                    allChatBinding?.allchatNoMessage?.visibility = View.VISIBLE
                }
                if (UserS_sender_id.size >0 ){
                    allChatBinding?.AllchatSpinKit?.visibility=View.GONE
                    allChatBinding?.allchatAnimNodata?.visibility=View.GONE
                    allChatBinding?.allchatNoMessage?.visibility = View.GONE
                    FireStore().chat_get_detailUser_sender(this@All_chat,UserS_sender_id)

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }
    fun successGettingInfo(usersDetail: java.util.ArrayList<Users>) {
        HideDialog()
        if (usersDetail.isNotEmpty()) {
            allChatBinding?.AllchatSwip?.isRefreshing=false
            allChatBinding?.AllChatRecycler?.apply {
                layoutManager = LinearLayoutManager(this@All_chat, RecyclerView.VERTICAL, false)
                adapter = AllChat_adapter(this@All_chat,usersDetail)
            }
        }
        allChatBinding?.AllchatSwip?.isRefreshing=false
        allChatBinding?.AllchatSpinKit?.visibility=View.GONE
    }
    override fun onDestroy() {
        super.onDestroy()
        allChatBinding = null
    }

}