package com.example.myshop.View.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.myshop.FireStore.FireStore
import com.example.myshop.R
import com.example.myshop.databinding.ActivityLoginBinding
import com.example.myshop.model.user
import com.example.myshop.Utils.ConstVal
import com.google.firebase.auth.FirebaseAuth

class Login : Basic(), View.OnClickListener {
    lateinit var login_binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        login_binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        setOnClickListener()

    }

    fun setOnClickListener() {
        login_binding.loginGoToRegisterBtn.setOnClickListener(this)
        login_binding.loginGoToRegisterTv.setOnClickListener(this)
        login_binding.LoginBtnLogin.setOnClickListener(this)
        login_binding.LoginEdtForgotPassword.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.login_go_to_register_btn -> {
                startActivity(Intent(this, Register::class.java))
                overridePendingTransition(R.anim.slide_in_right, R.anim.stay)

            }
            R.id.login_go_to_register_tv -> {
                startActivity(Intent(this, Register::class.java))
                overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
            }
            R.id.Login_Btn_Login -> {
                FirebaseLogin()
            }
            R.id.Login_Edt_ForgotPassword -> {
                startActivity(Intent(this, Forgot_PassWord::class.java))
            }
        }
    }

    private fun FirebaseLogin() {

        val Email_Login = login_binding.LoginEdtEmail.text?.trim().toString()
        val Password_Login = login_binding.LoginEdtPassWord.text?.trim().toString()
        if (check_empty_edt_register()==true) {
            ShowDialog(resources.getString(R.string.wait))
            FirebaseAuth.getInstance().signInWithEmailAndPassword(Email_Login, Password_Login)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        FireStore().GetUserDetailFromFireStore(this)
                        ShowSnackbar(resources.getString(R.string.success_login), true)
                    } else {
                        HideDialog()
                        ShowSnackbar(it.exception?.message.toString(), false)
                    }
                }
        }
    }

    fun check_empty_edt_register(): Boolean {
        val Email_Edt = login_binding.LoginEdtEmail
        val Password_Edt = login_binding.LoginEdtPassWord
        return when {
            TextUtils.isEmpty(Email_Edt.text.toString().trim{ it <= ' ' }) -> {
                ShowSnackbar(resources.getString(R.string.Enter_Email), false)
                false
            }
            TextUtils.isEmpty(Password_Edt.text.toString().trim{ it <= ' ' }) -> {
                ShowSnackbar(resources.getString(R.string.Enter_passwrod), false)
                false
            }
            else -> {
                true
            }
        }
    }

    fun UserLoginSuccess(user: user) {

        val pref=getSharedPreferences(ConstVal.MySharePref, Context.MODE_PRIVATE)
        val editor=pref.edit()
        editor.putString(ConstVal.UserNameKeyPref,user.FirstName)
        editor.apply()
       if (!this@Login.isFinishing) {
           HideDialog()
       }
        if (user.profile_Compelete == 0) {
            val intent_com = Intent(this, CompleteProfile::class.java)
            intent_com.putExtra(ConstVal.putExtra_UserDetail,user)
            intent_com.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK or  Intent.FLAG_ACTIVITY_NEW_TASK

            startActivity(intent_com)

        } else if (user.profile_Compelete==1) {
            val intent_dashbord = Intent(this, Main::class.java)
            intent_dashbord.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK or  Intent.FLAG_ACTIVITY_NEW_TASK

            startActivity(intent_dashbord)


        }
        else
        {
            HideDialog()
            Toast.makeText(this,"please register by another acount",Toast.LENGTH_SHORT).show()
        }


    }
}