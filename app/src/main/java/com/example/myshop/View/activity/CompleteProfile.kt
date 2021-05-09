package com.example.myshop.View.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
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
    lateinit var Compelte_profile: ActivityCompleteProfileBinding
    var actionbar_compelte: androidx.appcompat.app.ActionBar? = null
    var ImageUriSelceted: Uri? = null
    var downloadAble: String? = null
    var UserDetail: user? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Compelte_profile = DataBindingUtil.setContentView(this, R.layout.activity_complete_profile)
        GetUserDetailFromRegister()
        SetOnClickListener()

    }

    private fun DisableEdtHasData() {
        Compelte_profile.CompleteEdtName.isEnabled = false
        Compelte_profile.completeEdtPhoneNumber.isEnabled = false
        Compelte_profile.completeEdtEmail.isEnabled = false

    }


    private fun GetUserDetailFromRegister() {

        setSupportActionBar(Compelte_profile.compeleteToolbar)
        actionbar_compelte = supportActionBar
        var Intent_GetUser_Detail = intent
        if (Intent_GetUser_Detail.hasExtra(ConstVal.putExtra_UserDetail)) {
            UserDetail = Intent_GetUser_Detail.getParcelableExtra<user>(ConstVal.putExtra_UserDetail)
            SetDataToEdtGiveBefore(UserDetail)
            if (UserDetail?.profile_Compelete == 0) {
                DisableEdtHasData()
                if (actionbar_compelte != null) {
                    Compelte_profile.CompleteRdGenderFemale.isChecked = true
                    actionbar_compelte!!.setDisplayHomeAsUpEnabled(true)
                    actionbar_compelte?.setTitle(resources.getString(R.string.Compeleteprofile))
                }

            } else {
                Compelte_profile.completeEdtEmail.isEnabled=false
                if (actionbar_compelte != null) {
                    actionbar_compelte!!.setDisplayHomeAsUpEnabled(true)
                    actionbar_compelte?.setTitle(resources.getString(R.string.edit))
                }

                if (UserDetail?.Image != null) {
                    LoadPicprofile(UserDetail!!.Image)
                }
                Compelte_profile.completeEdtLastName.setText(UserDetail?.lastName)
                var gender = UserDetail?.gender
                if (gender == ConstVal.Male) {
                    Compelte_profile.CompleteRdGenderMale.isChecked = true
                } else {
                    Compelte_profile.CompleteRdGenderFemale.isChecked = true
                }
            }

        }
        Compelte_profile.compeleteToolbar.setNavigationIcon(R.drawable.ic_back)
        Compelte_profile.compeleteToolbar.setNavigationOnClickListener {
            onBackPressed()
            finish()
        }
    }


    private fun SetDataToEdtGiveBefore(userDetail: user?) {
        Compelte_profile.completeEdtEmail.setText(userDetail?.Email)
        Compelte_profile.CompleteEdtName.setText(userDetail?.FirstName)
        Compelte_profile.completeEdtPhoneNumber.setText(userDetail?.Mobile.toString())
    }

    fun SetOnClickListener() {
        Compelte_profile.CompleteIvUserprofile.setOnClickListener(this)
        Compelte_profile.CompleteBtnSave.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.Complete_Iv_userprofile -> {
                //TODO choose picture
                CheckPermision()
            }
            R.id.Complete_Btn_save -> {
                //TODO validate write lastname
                if (ValidationLastName()) {
                    GiveDataTocompleteSetup()
                }
            }

        }
    }

    private fun GiveDataTocompleteSetup() {

        if (ImageUriSelceted != null) {

            FireStore().UploadImageToCloudStore(this, ImageUriSelceted!!,ConstVal.UserProfileImage)
            ShowDialog(resources.getString(R.string.WaitToUpadteAndSendPic))
            Toast.makeText(this, ImageUriSelceted.toString(), Toast.LENGTH_LONG).show()
        } else {
            ShowDialog(resources.getString(R.string.WaitToUpadte))
            UpdateUsersDetial()
        }
    }

    private fun UpdateUsersDetial() {
        val LastName = Compelte_profile.completeEdtLastName.text.toString()
        var firstname=Compelte_profile.CompleteEdtName.text.toString()
        val mobile=Compelte_profile.completeEdtPhoneNumber.text.toString()
        val UserHashMap = HashMap<String, Any>()



        if (firstname.isNotEmpty() && firstname!=UserDetail?.FirstName) {
            UserHashMap[ConstVal.name] = firstname.toString()
        }
        if (mobile.isNotEmpty()&&mobile!=UserDetail?.Mobile.toString()){
            UserHashMap[ConstVal.phonenumber]=mobile.toLong()
        }

        if (downloadAble != null &&!downloadAble.equals(UserDetail?.Image)) {
            UserHashMap[ConstVal.imageColumn] = downloadAble.toString()
        }

        val gender = if (Compelte_profile.CompleteRdGenderFemale.isChecked) {
            ConstVal.Female
        } else {
            ConstVal.Male
        }

        if (LastName.isNotEmpty()&&LastName==UserDetail?.lastName) {
            UserHashMap[ConstVal.Lastname] = LastName
        }
        if (gender.isNotEmpty() && gender!=UserDetail?.gender){
            UserHashMap[ConstVal.gender] = gender
        }

        UserHashMap[ConstVal.usercompelete] = 1
        FireStore().Update_User_Detail(this, UserHashMap)

    }

    private fun ValidationLastName(): Boolean {
        return when {
            TextUtils.isEmpty(
                Compelte_profile.completeEdtLastName.text.toString().trim { it <= ' ' }) -> {
                ShowSnackbar(resources.getString(R.string.Enter_lastname), false)
                false
            }
            else -> true
        }
    }

    private fun CheckPermision() {
        //this  ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "shoma ghablan ejaze dadin", Toast.LENGTH_SHORT).show()

            ConstVal.ChoseImageFromGallery(this)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), ConstVal.RequestCode_Gallery)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            Log.e("pic", "1")
            if (requestCode == ConstVal.RequestCode_Permission) {
                Log.e("pic", "2")
                if (data != null) {
                    try {
                        Log.e("pic", "3")
                        ImageUriSelceted = data.data
                        // Compelte_profile.CompleteIvUserprofile.setImageURI(Uri.parse(SelectedImage))
                        ImageUriSelceted?.let {

                            Log.e("pic", "4")
                            LoadPicprofile(it)
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

    fun LoadPicprofile(image: Any) {
        Log.e("pic", image.toString())
        ConstVal.LoadPicByGlide(this, image, Compelte_profile.CompleteIvUserprofile)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ConstVal.RequestCode_Gallery) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ConstVal.ChoseImageFromGallery(this)
            } else {
                ShowSnackbar(resources.getString(R.string.pleaseAllowPermision), false)
            }
        }
    }


    fun UploadImageSuccess(it: Uri?) {
        /**ارسال ادرس عکس آپلود شده به دیتا بیس*/

        Log.e("pic", it.toString())
        downloadAble = it.toString()
        UpdateUsersDetial()

    }

    fun UpdateUserDetailSuccessfully() {

        if (!this@CompleteProfile.isFinishing) {
            HideDialog()
        }
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