package com.example.myshop.View.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.myshop.FireStore.FireStore
import com.example.myshop.model.user
import com.example.myshop.R
import com.example.myshop.Utils.ConstVal
import com.example.myshop.databinding.ActivityCompleteProfileBinding
import java.lang.Exception

class CompleteProfile : Basic(), View.OnClickListener {
    lateinit var Complete_profile: ActivityCompleteProfileBinding
    var actionbar_complete: androidx.appcompat.app.ActionBar? = null
    var ImageUriSelected: Uri? = null
    var downloadAble: String? = null
    var UserDetail: user? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Complete_profile = DataBindingUtil.setContentView(this, R.layout.activity_complete_profile)
        setUpActionbar()
        GetUserDetailFromRegister()
        SetOnClickListener()

    }

    fun setUpActionbar() {
        setSupportActionBar(Complete_profile.compeleteToolbar)
        actionbar_complete?.setDisplayHomeAsUpEnabled(true)
        actionbar_complete = supportActionBar
    }

    private fun Disable_Edt_Which_HasData() {
        Complete_profile.CompleteEdtName.isEnabled = false
        Complete_profile.completeEdtPhoneNumber.isEnabled = false
        Complete_profile.completeEdtEmail.isEnabled = false

    }

    fun SetOnClickListener() {
        Complete_profile.CompleteIvUserprofile.setOnClickListener(this)
        Complete_profile.CompleteBtnSave.setOnClickListener(this)
    }

    private fun GetUserDetailFromRegister() {

        val Intent_GetUser_Detail = intent
        if (Intent_GetUser_Detail.hasExtra(ConstVal.PutExtra_UserDetail)) {
            UserDetail =
                Intent_GetUser_Detail.getParcelableExtra<user>(ConstVal.PutExtra_UserDetail)

            if (UserDetail != null) {
                if (UserDetail?.profile_Compelete == 0) {
                    Disable_Edt_Which_HasData()
                    Complete_profile.CompleteRdGenderFemale.isChecked = true
                    actionbar_complete?.setTitle(resources.getString(R.string.Compeleteprofile))

                } else {
                    Complete_profile.completeEdtEmail.isEnabled = false
                    actionbar_complete?.setTitle(resources.getString(R.string.edit))
                    if (UserDetail?.Image != null) {
                        Load_Pic_Profile(UserDetail!!.Image)
                    }
                    Complete_profile.completeEdtLastName.setText(UserDetail?.lastName)

                    val gender = UserDetail?.gender
                    if (gender == ConstVal.Male) {
                        Complete_profile.CompleteRdGenderMale.isChecked = true
                    } else {
                        Complete_profile.CompleteRdGenderFemale.isChecked = true
                    }
                }
            }

        }
        SetDataToEdtGiveBefore(UserDetail)
        Complete_profile.compeleteToolbar.setNavigationIcon(R.drawable.ic_back)
        Complete_profile.compeleteToolbar.setNavigationOnClickListener {
            onBackPressed()
            finish()
        }
    }

    private fun SetDataToEdtGiveBefore(userDetail: user?) {
        Complete_profile.completeEdtEmail.setText(userDetail?.Email)
        Complete_profile.CompleteEdtName.setText(userDetail?.FirstName)
        Complete_profile.completeEdtPhoneNumber.setText(userDetail?.Mobile.toString())
        if (userDetail?.profile_Compelete == 1) {
            Complete_profile.completeEdtLastName.setText(userDetail.lastName)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.Complete_Iv_userprofile -> {
                CheckPermission()
            }
            R.id.Complete_Btn_save -> {

                if (ValidationLastName()) {
                    GiveData_To_completeSetup()
                }
            }

        }
    }

    private fun GiveData_To_completeSetup() {
        if (ImageUriSelected != null) {
            FireStore().UploadImageToCloudStore(this, ImageUriSelected!!, ConstVal.UserProfileImage)
            ShowDialog(resources.getString(R.string.WaitToUpadteAndSendPic))

        } else {
            ShowDialog(resources.getString(R.string.WaitToUpadte))
            UpdateUsersDetail()
        }
    }

    private fun UpdateUsersDetail() {
        val LastName = Complete_profile.completeEdtLastName.text.toString()
        val firstname = Complete_profile.CompleteEdtName.text.toString()
        val mobile = Complete_profile.completeEdtPhoneNumber.text.toString()
        val UserHashMap = HashMap<String, Any>()



        if (firstname.isNotEmpty() && firstname != UserDetail?.FirstName) {
            UserHashMap[ConstVal.name] = firstname
        }
        if (mobile.isNotEmpty() && mobile != UserDetail?.Mobile.toString()) {
            UserHashMap[ConstVal.phonenumber] = mobile.toLong()
        }
        if (LastName.isNotEmpty() && LastName != UserDetail?.lastName) {
            UserHashMap[ConstVal.Lastname] = LastName
        }
        if (downloadAble != null && !downloadAble.equals(UserDetail?.Image)) {
            UserHashMap[ConstVal.imageColumn] = downloadAble.toString()
        }

        val gender = if (Complete_profile.CompleteRdGenderFemale.isChecked) {
            ConstVal.Female
        } else {
            ConstVal.Male
        }


        if (gender.isNotEmpty() && gender != UserDetail?.gender) {
            UserHashMap[ConstVal.gender] = gender
        }

        UserHashMap[ConstVal.usercompelete] = 1
        FireStore().Update_User_Detail(this, UserHashMap)


    }

    private fun ValidationLastName(): Boolean {
        return when {
            TextUtils.isEmpty(
                Complete_profile.completeEdtLastName.text.toString().trim { it <= ' ' }) -> {
                ShowSnackbar(resources.getString(R.string.Enter_lastname), false)
                false
            }
            else -> true
        }
    }

    private fun CheckPermission() {
        //this  ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            ConstVal.ChoseImageFromGallery(this)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                ConstVal.RequestCode_Permission
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ConstVal.RequestCode_Gallery) {
                if (data != null) {
                    try {
                        ImageUriSelected = data.data
                        // Complete_profile.CompleteIvUserprofile.setImageURI(Uri.parse(SelectedImage))
                        ImageUriSelected?.let {
                            Load_Pic_Profile(it)
                            ShowSnackbar(resources.getString(R.string.alerttouploadpic), true)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        ShowSnackbar(resources.getString(R.string.faildSelectedImage), false)

                    }

                }
            }
        }
    }

    fun Load_Pic_Profile(image: Any) {
        ConstVal.LoadPicByGlide(this, image, Complete_profile.CompleteIvUserprofile)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ConstVal.RequestCode_Permission) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ConstVal.ChoseImageFromGallery(this)
            } else {
                ShowSnackbar(resources.getString(R.string.pleaseAllowPermision), false)
            }
        }
    }


    fun uploadImageSuccess(it: Uri?) {
        /**ارسال ادرس عکس آپلود شده به دیتا بیس*/
        if (it!=null) {
            downloadAble = it.toString()
            val pref = getSharedPreferences(ConstVal.MySharePref, Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.putString(ConstVal.userImageuri, it.toString())
            editor.apply()
        }
        UpdateUsersDetail()

    }

    fun UpdateUserDetailSuccessfully() {
        if (!this@CompleteProfile.isFinishing) {
            HideDialog()
        }
        if (UserDetail != null) {
            if (UserDetail?.profile_Compelete == 0) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.welcome) + UserDetail?.FirstName,
                    Toast.LENGTH_LONG
                ).show()
                startActivity(Intent(this, Main::class.java))
                finish()
            }
            if (UserDetail?.profile_Compelete == 1) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.SuccessWhenUpadteData),
                    Toast.LENGTH_LONG
                ).show()
            }
        }


    }


}