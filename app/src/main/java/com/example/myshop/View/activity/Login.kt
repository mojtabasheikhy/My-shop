package com.example.myshop.View.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.myshop.FireStore.FireStore
import com.example.myshop.R
import com.example.myshop.Utils.ConstVal
import com.example.myshop.databinding.ActivityLoginBinding
import com.example.myshop.model.Users
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.util.*


class Login : Basic(), View.OnClickListener, AdapterView.OnItemClickListener {
    lateinit var login_binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    var callbackManager: CallbackManager? = null
    var language:Array<String>? = null
    var intentgoogle: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        login_binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        setOnClickListener()
    }


    private fun loadLanguages() {
        language = resources.getStringArray(R.array.lanquages)
        var arrayadapter = ArrayAdapter(this, R.layout.languages_dropdown, language!!)
        login_binding.loginChoseLan.setAdapter(arrayadapter)
        login_binding.loginChoseLan.setOnItemClickListener(this)

        val getlangues=getSharedPreferences(ConstVal.MySharePref, Context.MODE_PRIVATE)
        val languagesValues=getlangues.getString(ConstVal.Language,"en")

        if (languagesValues.equals("en")){
            loadLanguage("en") }
        else{
            loadLanguage("fa")

        }
    }

    fun setOnClickListener() {
        login_binding.loginGoToRegisterBtn.setOnClickListener(this)
        login_binding.loginGoToRegisterTv.setOnClickListener(this)
        login_binding.LoginBtnLogin.setOnClickListener(this)
        login_binding.LoginEdtForgotPassword.setOnClickListener(this)
        login_binding.LoginIvGoogle.setOnClickListener(this)
        login_binding.LoginIvFacebook.setOnClickListener(this)
        login_binding.headerTitleTv.setOnClickListener {
           // Custom_Toast(this,)
            Custom_Toast(this, resources.getString(R.string.welcome),R.drawable.ic_checkbox_icon,R.color.text_color)

        }

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
            R.id.Login_Iv_Google -> {
                googleLogin()
            }
            R.id.Login_Iv_Facebook -> {
                loginbyfacebook()
            }
        }
    }

    private fun FirebaseLogin() {

        val Email_Login = login_binding.LoginEdtEmail.text?.trim().toString()
        val Password_Login = login_binding.LoginEdtPassWord.text?.trim().toString()
        if (Validation_login() == true) {
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

    fun Validation_login(): Boolean {
        val Email_Edt = login_binding.LoginEdtEmail
        val Password_Edt = login_binding.LoginEdtPassWord
        return when {
            TextUtils.isEmpty(Email_Edt.text.toString().trim { it <= ' ' }) -> {
                ShowSnackbar(resources.getString(R.string.Enter_Email), false)
                false
            }
            TextUtils.isEmpty(Password_Edt.text.toString().trim { it <= ' ' }) -> {
                ShowSnackbar(resources.getString(R.string.Enter_passwrod), false)
                false
            }
            else -> {
                true
            }
        }
    }

    fun UserLoginSuccess(Users: Users) {

        val pref = getSharedPreferences(ConstVal.MySharePref, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(ConstVal.UserNameKeyPref, Users.FirstName)
        editor.apply()


        if (!this@Login.isFinishing) {
            HideDialog()
        }
        if (Users.profile_Compelete == 0) {
            val intent_com = Intent(this, CompleteProfile::class.java)
            intent_com.putExtra(ConstVal.PutExtra_UserDetail, Users)
            intent_com.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent_com)

        } else if (Users.profile_Compelete == 1) {
            val intent_dashbord = Intent(this, Main::class.java)
            intent_dashbord.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent_dashbord)
        } else {
            HideDialog()
            Toast.makeText(this, "please register by another acount", Toast.LENGTH_SHORT).show()
        }
    }

    fun loginbyfacebook() {
        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create()
        auth = FirebaseAuth.getInstance()
        FacebookSdk.sdkInitialize(this)
        login_binding.LoginIvFacebook.setReadPermissions("email", "public_profile")
        login_binding.LoginIvFacebook.registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("TAG", "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d("TAG", "facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.d("TAG", "facebook:onError", error)
            }
        })
    }

    override fun onResume() {
        loadLanguages()
        super.onResume()

    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        ShowDialog(resources.getString(R.string.wait))
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.e("face", auth.currentUser?.displayName.toString())

                    val userdetail = com.example.myshop.model.Users(
                        user!!.uid,
                        user.displayName.toString(),
                        "",
                        user.email.toString(),
                        user.phoneNumber.toString(),
                        "",
                        0,
                        user.photoUrl.toString()
                    )
                    userdetail.let {
                        Log.e("face", "1")
                        FireStore().RegisterUserToFireStore(this@Login, userdetail)

                    }
                }


            }.addOnFailureListener {
                Log.e("face", it.message.toString())
            }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("sd", "2")
        if (requestCode == ConstVal.googleLogin) {
            Log.e("sd", "2.5")

            var acoutnTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                Log.e("sd", "3")

                var acount = acoutnTask.getResult(ApiException::class.java)
                Toast.makeText(this, acount?.displayName.toString(), Toast.LENGTH_LONG).show()
                firebaseSignUpGoogle(acount)

            } catch (E: java.lang.Exception) {
                Log.e("sd", E.message.toString())
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("sd", "cancel")
            }
            if (resultCode == Activity.RESULT_FIRST_USER) {
                Log.e("sd", "firs")
            }
        }


    }

    fun successRegsiterByfaceBook(userinf: Users) {

        if (userinf.profile_Compelete == 0) {
            val intent_com = Intent(this, CompleteProfile::class.java)
            intent_com.putExtra(ConstVal.PutExtra_UserDetail, userinf)
            intent_com.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent_com)

        } else if (userinf.profile_Compelete == 1) {
            val intent_dashbord = Intent(this, Main::class.java)
            intent_dashbord.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent_dashbord)
        }
    }

    fun failedToLogin(it: Exception) {
        ShowSnackbar(it.message.toString(), false)
    }


    fun googleLogin() {
        Log.e("sd", "1")
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(resources.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        var GoogleSignINclient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()
        intentgoogle = GoogleSignINclient.signInIntent
        setResult(Activity.RESULT_OK)
        startActivityForResult(intentgoogle, ConstVal.googleLogin)
    }

    private fun firebaseSignUpGoogle(acount: GoogleSignInAccount?) {
        Log.e("sd", "4")


        var credential = GoogleAuthProvider.getCredential(acount?.idToken, null)
        ShowDialog(resources.getString(R.string.wait))
        auth.signInWithCredential(credential)

            .addOnCompleteListener {
                if (it.isSuccessful()) {
                    Log.e("sd", "5")
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.getCurrentUser()

                    val userdetail = Users(
                        user!!.uid,
                        user.displayName.toString(),
                        "",
                        user.email.toString(),
                        user.phoneNumber.toString(),
                        "",
                        0,
                        user.photoUrl.toString()
                    )
                    userdetail.let {
                        FireStore().RegisterUserToFireStore(this@Login, userdetail)
                    }

                }
            }.addOnFailureListener {

                it.printStackTrace()
            }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_CANCELED)
    }


     fun loadLanguage(Mylocale:String){
         var LocaleSelected = Locale(Mylocale)
         Locale.setDefault(LocaleSelected)
         var Configuration = Configuration()
         Configuration.locale=LocaleSelected
         baseContext.resources.updateConfiguration(Configuration,baseContext.resources.displayMetrics)
     }

    fun setlocale(Mylocale: String) {
        var LocaleSelected = Locale(Mylocale)
        Locale.setDefault(LocaleSelected)
        var Configuration = Configuration()
        Configuration.locale=LocaleSelected

        baseContext.resources.updateConfiguration(Configuration,baseContext.resources.displayMetrics)
        var shareprf=getSharedPreferences(ConstVal.MySharePref, Context.MODE_PRIVATE )
        var editor=shareprf.edit()
        editor.putString(ConstVal.Language,Mylocale)
        editor.apply()

    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(position){
            0 -> {
                setlocale("en")

                var intent=Intent(this,Login::class.java)
                startActivity(intent)
                finish()

            }
            1 -> {
                setlocale("fa")
                var intent=Intent(this,Login::class.java)
                startActivity(intent)
                finish()

            }
        }
    }


}