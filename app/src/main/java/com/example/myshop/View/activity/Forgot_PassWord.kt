package com.example.myshop.View.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.myshop.R
import com.example.myshop.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class Forgot_PassWord : Basic(), View.OnClickListener {
    lateinit var forgotPasswordBinding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forgotPasswordBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)
        forgotPasswordBinding.ForgotpasswordBtnSend.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.Forgotpassword_Btn_Send -> {
                SendEmailToChangePassword()
            }
        }
    }

    private fun SendEmailToChangePassword() {
        if (Validate_Email()){
            ShowDialog(resources.getString(R.string.sending))
            FirebaseAuth.getInstance().sendPasswordResetEmail(forgotPasswordBinding.ForgotpasswordEdtEmail.text.toString().trim()).addOnCompleteListener({
                HideDialog()
                if (it.isSuccessful){
                    ShowSnackbar(resources.getString(R.string.send_forgot_password_email),true)
                }
                else
                    ShowSnackbar(it.exception?.message.toString(),false)
            })
        }
        else
            ShowSnackbar(resources.getString(R.string.Enter_Email),false)

    }

    private fun Validate_Email(): Boolean {
        return when {
            TextUtils.isEmpty(forgotPasswordBinding.ForgotpasswordEdtEmail.text.toString().trim({ it <= ' ' })) -> {
                false
            }
            else -> true
        }
    }
}
