package com.example.myshop.View.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.myshop.FireStore.FireStore
import com.example.myshop.R
import com.example.myshop.databinding.ActivityRegisterBinding
import com.example.myshop.model.user
import com.google.firebase.auth.FirebaseAuth

class Register : Basic(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    lateinit var register_binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        register_binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        setonclicklistener()

    }

    fun setonclicklistener() {
        register_binding.registerToLogIn.setOnClickListener(this)
        register_binding.registerToLogInTv.setOnClickListener(this)
        register_binding.RegisterBtnRegister.setOnClickListener(this)
        register_binding.RegisterCbAgree.setOnCheckedChangeListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.Register_Btn_Register -> {
                send_data_user()
            }
            R.id.register_to_log_in -> {
                onBackPressed()
            }
            R.id.register_to_log_in_tv -> {
                onBackPressed()
            }
        }
    }

    fun check_empty_edt(): Boolean {
        val Register_Name_Edt = register_binding.RegisterEdtName
        val Register_email_Edt = register_binding.RegisterEdtEmail
        val Register_phonenumber_Edt = register_binding.RegisterEdtPhone
        val Register_Password_Edt = register_binding.RegisterEdtPassWord
        val Register_Confirmpassword_Edt = register_binding.RegisterEdtConfirmPassword
        return when {
            TextUtils.isEmpty(Register_Name_Edt.text.toString().trim({ it <= ' ' })) -> {
                ShowSnackbar(resources.getString(R.string.Enter_Name), false)
                false
            }
            TextUtils.isEmpty(Register_email_Edt.text.toString().trim({ it <= ' ' })) -> {
                ShowSnackbar(resources.getString(R.string.Enter_Email), false)
                false
            }
            TextUtils.isEmpty(Register_phonenumber_Edt.text.toString().trim({ it <= ' ' })) -> {
                ShowSnackbar(resources.getString(R.string.Enter_phone), false)
                false
            }
            TextUtils.isEmpty(Register_Password_Edt.text.toString().trim({ it <= ' ' })) -> {
                ShowSnackbar(resources.getString(R.string.Enter_passwrod), false)
                false
            }
            TextUtils.isEmpty(Register_Confirmpassword_Edt.text.toString().trim({ it <= ' ' })) -> {
                ShowSnackbar(resources.getString(R.string.confirm_password), false)
                false
            }
            Register_Password_Edt.text.toString()
                .trim({ it <= ' ' }) != Register_Confirmpassword_Edt.text.toString()
                .trim({ it <= ' ' }) -> {
                ShowSnackbar(resources.getString(R.string.Enter_match_password), false)
                false
            }
            !register_binding.RegisterCbAgree.isChecked -> {
                ShowSnackbar(resources.getString(R.string.Enter_terms), false)
                register_binding.RegisterCbAgree.setTextColor(ContextCompat.getColor(this,R.color.red))
                false
            }
            else -> {
                true
            }
        }
    }

    fun send_data_user() {
        var email = register_binding.RegisterEdtEmail.text.toString().trim({ it <= ' ' })
        var password = register_binding.RegisterEdtPassWord.text.toString().trim({ it <= ' ' })
        if (check_empty_edt()) {
            ShowDialog(resources.getString(R.string.wait))
            register_binding.RegisterCbAgree.setTextColor(ContextCompat.getColor(this,R.color.green))
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener() {
                    if (it.isSuccessful) {
                        val name = register_binding.RegisterEdtName.text.toString().trim()
                        val emai_user = register_binding.RegisterEdtEmail.text.toString().trim()
                        val phone = register_binding.RegisterEdtPhone.text.toString().trim()
                        val user = it.result?.user?.let { it1 ->
                            user(it1.uid, name, "", emai_user, phone.toLong(), "", 0, "")
                        }
                        user?.let { it1 -> FireStore().RegisterUserToFireStore(this, it1) }

                    } else {
                        HideDialog()
                        ShowSnackbar(it.exception?.message.toString(), false)
                    }

                }
        }
    }

    fun RegisterSuccess() {
        Toast.makeText(this, resources.getString(R.string.success_register), Toast.LENGTH_SHORT).show()
        if (!this.isFinishing) {
            HideDialog()
        }
        var intent=Intent(this, Login::class.java)
        startActivity(intent)
        finish()
        FirebaseAuth.getInstance().signOut()
        if (!this.isFinishing) {
            HideDialog()
        }
    }

    fun RegisterFailed() {
        if (!this.isFinishing ) {
            HideDialog()
        }
        ShowSnackbar(resources.getString(R.string.ErrorInregister),false)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

        if ( register_binding.RegisterCbAgree.isChecked)
        register_binding.RegisterCbAgree.setTextColor(ContextCompat.getColor(this,R.color.green))
        else
            register_binding.RegisterCbAgree.setTextColor(ContextCompat.getColor(this,R.color.second_text_color))


    }
}