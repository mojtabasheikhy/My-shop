package com.example.myshop.View.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.FireStore.FireStore
import com.example.myshop.R
import com.example.myshop.Utils.ConstVal
import com.example.myshop.databinding.ActivityChatBinding
import com.example.myshop.model.Chat_DataClass
import com.example.myshop.model.Users
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.letsbuildthatapp.kotlinmessenger.views.Chat_adapter


class Chat : AppCompatActivity(), View.OnClickListener {
    var activityChatBinding:ActivityChatBinding?=null
    var sellerUserid:String?=null
    var messagelist:ArrayList<Chat_DataClass>? = null
    var buyerUserId:String?=null
    var database:FirebaseDatabase? = null
    var data:ArrayList<Chat_DataClass>?=null
    var pathSend:String? = null
    var pathRecived:String? = null
    var userprofSeller:String?=null
    var useridsender:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         activityChatBinding=DataBindingUtil.setContentView(this, R.layout.activity_chat)

        pathSend = "/User_Message/send/$buyerUserId/$sellerUserid"
        pathRecived = "/User_Message/recived/$sellerUserid/$buyerUserId"

         database = FirebaseDatabase.getInstance()
         buyerUserId=FireStore().GetCurrentUserID()
        if (intent.hasExtra(ConstVal.putExteraChatDetail)){
            var userdetail=intent.getParcelableExtra<Users>(ConstVal.putExteraChatDetail)
            useridsender = userdetail?.user_id
            var userProfileimage=userdetail?.Image
            var username=userdetail?.FirstName
            activityChatBinding?.chatUsername?.text=username
            if (userProfileimage != null) {
                ConstVal.LoadPicByGlide(this,userProfileimage,activityChatBinding!!.chatUserProfImage)
            }
            Getmessage_recived()


        }
        else {
            getUserSellerInfo()
            Getmessage()
        }

        setonclickListener()


    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.chat_send_message_btn -> {
                if (intent.hasExtra(ConstVal.putExteraChatDetail)){
                    chat(
                        FireStore().GetCurrentUserID(),
                        useridsender!!,
                        activityChatBinding!!.chatMessageEdt.text.toString()
                    )
                }
                else {
                    chat(
                        FireStore().GetCurrentUserID(),
                        sellerUserid!!,
                        activityChatBinding!!.chatMessageEdt.text.toString()
                    )
                }

            }
        }
    }
    private fun getUserSellerInfo() {
        if (intent.hasExtra(ConstVal.putExteraUserIdSeller)){
            sellerUserid=intent.getStringExtra(ConstVal.putExteraUserIdSeller)
            }
        if (intent.hasExtra(ConstVal.putExteraUserSellerProfileImageUri)){
            userprofSeller=intent.getStringExtra(ConstVal.putExteraUserSellerProfileImageUri)
            if (userprofSeller!=null) { ConstVal.LoadPicByGlide(this, userprofSeller!!, activityChatBinding!!.chatUserProfImage) }
        }
    }
    fun setonclickListener(){
        activityChatBinding?.chatSendMessageBtn?.setOnClickListener(this)
    }
    fun chat(buyerId: String, SellerId: String, message: String){
        var myRef = database?.getReference(pathSend!!)
        if (myRef?.key!=null){
        var chatDataClass=Chat_DataClass(myRef.key!!, buyerId, SellerId, message, System.currentTimeMillis().toString() ?: "")
            var sending=myRef.push()
            sending.setValue(chatDataClass).addOnSuccessListener {
                activityChatBinding?.chatMessageEdt?.setText("")
            }
                .addOnFailureListener {
                    Log.e("E", it.message.toString())
                }
        }
        var myRefRecieved = database?.getReference(pathRecived!!)
        if (myRefRecieved?.key!=null){
            var chatDataClass=Chat_DataClass(myRefRecieved.key!!, buyerId, SellerId, message, System.currentTimeMillis().toString() ?: "")
            var sending=myRefRecieved.push()
            sending.setValue(chatDataClass).addOnSuccessListener {
                activityChatBinding?.chatMessageEdt?.setText("")
            }
                .addOnFailureListener {
                    Log.e("E", it.message.toString())
                }
        }
    }
    fun Getmessage(){
             var ListenNewMessage=FirebaseDatabase.getInstance().getReference(pathSend!!)
              messagelist = ArrayList<Chat_DataClass>()
                ListenNewMessage.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        messagelist!!.clear()
                        for (snapshot1 in snapshot.children) {
                            var message= snapshot1.getValue(Chat_DataClass::class.java)
                            message?.messageKey = snapshot1.key.toString()
                            if (message != null) {
                                messagelist!!.add(message)
                            }
                        }
                        recyclerviewChat()
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
    }
    fun Getmessage_recived(){
        var ListenNewMessage=FirebaseDatabase.getInstance().getReference(pathRecived!!)
        messagelist = ArrayList<Chat_DataClass>()
        ListenNewMessage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messagelist!!.clear()
                for (snapshot1 in snapshot.children) {
                    var message= snapshot1.getValue(Chat_DataClass::class.java)
                    message?.messageKey = snapshot1.key.toString()
                    if (message != null) {
                        messagelist!!.add(message)
                    }
                }
                recyclerviewChat()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    fun recyclerviewChat(){
        if (messagelist!=null) {
            if (messagelist!!.size> 0) {
                activityChatBinding?.chatRecycler?.apply {
                    layoutManager = LinearLayoutManager(this@Chat, RecyclerView.VERTICAL, false)
                    adapter = Chat_adapter(messagelist!!)
                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        activityChatBinding=null
    }
}