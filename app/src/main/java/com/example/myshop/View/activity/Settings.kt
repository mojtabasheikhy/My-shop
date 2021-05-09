package com.example.myshop.View.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.myshop.FireStore.FireStore
import com.example.myshop.model.user
import com.example.myshop.R
import com.example.myshop.Utils.ConstVal
import com.example.myshop.databinding.ActivitySettingsBinding
import com.google.firebase.auth.FirebaseAuth

class Settings : Basic(), View.OnClickListener {
    var User_Gave:user?=null
    lateinit var settings_bind:ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        settings_bind= DataBindingUtil.setContentView(this, R.layout.activity_settings)
        setSupportActionBar(settings_bind.settingsToolabr)
        var actionbar_history = supportActionBar
        if (actionbar_history != null) {
            actionbar_history.setDisplayHomeAsUpEnabled(true)
            actionbar_history.title=resources.getString(R.string.settings)

        }
        settings_bind.settingsToolabr.setNavigationIcon(R.drawable.ic_back)
        settings_bind.settingsToolabr.setNavigationOnClickListener {
            onBackPressed()
            finish()
        }
        SetOnClickListener()

    }

    private fun SetOnClickListener() {
        settings_bind.settingsBtnEdit.setOnClickListener(this)
        settings_bind.settingsBtnLogout.setOnClickListener(this)
    }

    fun GetUserDetailSettings(){
        ShowDialog(resources.getString(R.string.wait))
        FireStore().GetUserDetailFromFireStore(this)

    }
    fun GetUserDetailSettingsSuccess(user: user) {
        User_Gave = user
        HideDialog()
        ConstVal.LoadPicByGlide(this,user.Image,settings_bind.settingsIvProfile)
        settings_bind.settingsTvEmail.text=user.Email
        settings_bind.settingsTvName.text=user.FirstName
        settings_bind.settingsTvPhone.text=user.Mobile.toString()
        if (user.gender==ConstVal.Male)
        {
            settings_bind.settingsIvGender.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_man))
        }
        else
            settings_bind.settingsIvGender.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_woman))

    }

    override fun onResume() {
        GetUserDetailSettings()
        super.onResume()

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.settings_Btn_logout->{
               FirebaseAuth.getInstance().signOut()
                var intent=Intent(this,Login::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK or  Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
             }
            R.id.settings_btn_edit ->{
                var intent=Intent(this,CompleteProfile::class.java)
                intent.putExtra(ConstVal.putExtra_UserDetail,User_Gave)
                startActivity(intent)
            }
        }
    }
}