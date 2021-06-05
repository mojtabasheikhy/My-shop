package com.example.myshop.View.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.myshop.FireStore.FireStore
import com.example.myshop.R
import com.example.myshop.Utils.ConstVal
import com.example.myshop.databinding.ActivitySettingsBinding
import com.example.myshop.model.Users
import com.google.firebase.auth.FirebaseAuth

class Settings : Basic(), View.OnClickListener {
    var users_Gave:Users?=null
    lateinit var settings_bind:ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings_bind= DataBindingUtil.setContentView(this, R.layout.activity_settings)
        Actionbar_Setup()
        SetOnClickListener()

    }
    fun Actionbar_Setup(){
        setSupportActionBar(settings_bind.settingsToolbar)
        val actionbar_history = supportActionBar
        if (actionbar_history != null) {
            actionbar_history.setDisplayHomeAsUpEnabled(true)
            actionbar_history.title=resources.getString(R.string.settings)

        }
        settings_bind.settingsToolbar.setNavigationIcon(R.drawable.ic_back)
        settings_bind.settingsToolbar.setNavigationOnClickListener {
            onBackPressed()
            finish()
        }
    }

    private fun SetOnClickListener() {
        settings_bind.settingsBtnEdit.setOnClickListener(this)
        settings_bind.settingsBtnLogout.setOnClickListener(this)
        settings_bind.settingsEdtAddressList.setOnClickListener (this)
        settings_bind.settingsIvDeleteAcout.setOnClickListener(this)
    }

    fun GetUserDetailSettings(){
        ShowDialog(resources.getString(R.string.wait))
        FireStore().GetUserDetailFromFireStore(this)

    }

    fun GetUserDetailSettingsSuccess(Users: Users) {
        HideDialog()
        users_Gave = Users

        if (users_Gave!=null) {
            ConstVal.LoadPicByGlide(this,Users.Image,settings_bind.settingsIvProfile)
            settings_bind.settingsTvEmail.text = Users.Email
            settings_bind.settingsTvName.text = Users.FirstName
            if (Users.Mobile.equals("null")){
                settings_bind.settingsTvPhone.setText(resources.getString(R.string.Enter_phone))
            }
            else {
                settings_bind.settingsTvPhone.text = Users.Mobile
            }
            if (Users.gender == ConstVal.Male) {
                settings_bind.settingsIvGender.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_man
                    )
                )
            } else
                settings_bind.settingsIvGender.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_woman
                    )
                )
        }

    }

    override fun onResume() {
        GetUserDetailSettings()
        super.onResume()

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.settings_Btn_logout->{
                FirebaseAuth.getInstance().signOut()
                val intent=Intent(this,Login::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK or  Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
             }
            R.id.settings_btn_edit ->{
                val intent=Intent(this,CompleteProfile::class.java)
                intent.putExtra(ConstVal.PutExtra_UserDetail,users_Gave)
                startActivity(intent)
            }
            R.id.settings_Edt_addressList ->{
                val intent=Intent(this,Address::class.java)
                startActivity(intent)
            }
            R.id.settings_iv_deleteAcout -> {
               FireStore().deleteUserFormFireStore(this)
            }
        }
    }
    fun failedDeleteuser(){
        Toast.makeText(this,"delete not success",Toast.LENGTH_LONG).show()
    }
   fun successDeleteAcount(){
       var user=FirebaseAuth.getInstance().currentUser
           user?.delete()?.addOnCompleteListener {
               HideDialog()
               FirebaseAuth.getInstance().signOut()
               val intent=Intent(this,Login::class.java)
               intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK or  Intent.FLAG_ACTIVITY_NEW_TASK
               startActivity(intent)
               finish()
           }
   }
}