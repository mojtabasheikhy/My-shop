package com.example.myshop.View.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.FireStore.FireStore
import com.example.myshop.R
import com.example.myshop.Utils.ConstVal
import com.example.myshop.databinding.ActivityChatBinding
import com.example.myshop.model.Chat_DataClass
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
    lateinit var database:FirebaseDatabase
    var data:ArrayList<Chat_DataClass>?=null
    var pathSend:String? = null
    var pathRecived:String? = null
    var userprofSeller:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         activityChatBinding=DataBindingUtil.setContentView(this, R.layout.activity_chat)


         database = FirebaseDatabase.getInstance()
         buyerUserId=FireStore().GetCurrentUserID()
         getUserSellerInfo()
         pathSend="/User_Message/send/$buyerUserId/$sellerUserid"
         pathRecived="/User_Message/recived/$sellerUserid/$buyerUserId"
         setonclickListener()
         Getmessage()




    }

    private fun getUserSellerInfo() {
        if (intent.hasExtra(ConstVal.putExteraUserIdSeller)){
            sellerUserid=intent.getStringExtra(ConstVal.putExteraUserIdSeller)
            }
        if (intent.hasExtra(ConstVal.putExteraUserSellerProfileImageUri)){
            userprofSeller=intent.getStringExtra(ConstVal.putExteraUserSellerProfileImageUri)
            if (sellerUserid!=null) {
                ConstVal.LoadPicByGlide(
                    this,
                    userprofSeller!!,
                    activityChatBinding!!.circleImageView2
                )
            }
        }
    }

    fun setonclickListener(){
        activityChatBinding?.chatSendMessageBtn?.setOnClickListener(this)
    }

    fun chat(buyerId: String, SellerId: String, message: String){
        var myRef = database.getReference(pathSend!!)
        if (myRef.key!=null){
        var chatDataClass=Chat_DataClass(myRef.key!!, buyerId, SellerId, message, System.currentTimeMillis().toString() ?: "")
            var sending=myRef.push()
            sending.setValue(chatDataClass).addOnSuccessListener {
                Toast.makeText(this, "sd", Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener {
                    Log.e("E", it.message.toString())
                }
        }
        var myRefRecieved = database.getReference(pathRecived!!)
        if (myRefRecieved.key!=null){
            var chatDataClass=Chat_DataClass(myRefRecieved.key!!, buyerId, SellerId, message, System.currentTimeMillis().toString() ?: "")
            var sending=myRefRecieved.push()
            sending.setValue(chatDataClass).addOnSuccessListener {
                Toast.makeText(this, "sd", Toast.LENGTH_SHORT).show()
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

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.chat_send_message_btn -> {
                Toast.makeText(this, "send", Toast.LENGTH_SHORT).show()
                chat(
                    FireStore().GetCurrentUserID(),
                    sellerUserid!!,
                    activityChatBinding!!.chatMessageEdt.text.toString()
                )

            }
        }
    }
}